/**
 * WebsocketSpout.java
 */

package storm.websockets.spout;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

/**
 * A spout to handle messages pushed over Websockets connections.
 * 
 * @author Christopher Kilding
 * @date 17/11/2012
 */
public class WebsocketSpout extends BaseRichSpout {

  /** serialVersionUID */
  private static final long                     serialVersionUID = 1L;

  /** A WebSocket server. Don't serialize it, it'll be gone when you deserialize! */
  private transient final SimpleWebSocketServer server           = new SimpleWebSocketServer(new WSMessageListener() {

                                                                   @Override
                                                                   public void onMessageReceived(String message) {
                                                                     // Push message onto queue
                                                                     // DO NOT WAIT if space not available - latency is critical
                                                                     // if it can't do this immediately, just drop it
                                                                     WebsocketSpout.this.queue.offer(message);
                                                                   }
                                                                 });

  /** Output collector as supplied in the open() call. */
  private SpoutOutputCollector                  collector;

  /** Queue to mediate between WS server and nextTuple(). May need to be replaced with something more robust. */
  private final BlockingQueue<String>           queue            = new LinkedBlockingQueue<String>();

  /**
   * THIS IS NOT GUARANTEED TO BE CALLED. Often the spout process just gets `kill`ed.
   * 
   * @see backtype.storm.spout.ISpout#close()
   */
  @Override
  public void close() {
    // Shut the show down
    try {
      this.server.stop();
    }
    catch (IOException e) {
      System.err.println("Just got an exception when I tried to stop a WS server.");
      e.printStackTrace();
    }
  }

  /**
   * @param declarer
   * 
   * @see backtype.storm.topology.IComponent#declareOutputFields(backtype.storm.topology.OutputFieldsDeclarer)
   */
  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    declarer.declare(new Fields("message"));
  }

  @Override
  public void nextTuple() {
    // Pop latest thing off the queue
    String nextObj = this.queue.poll();
    if (nextObj == null) {
      // Silent return, and sleep to avoid spamming the CPU
      Utils.sleep(1L);
    }
    else {
      // Only emit if we actually have an object to send
      // i.e. DON'T EMIT NULLS
      this.collector.emit(new Values(nextObj));
    }
  }

  /**
   * @param conf
   * @param context
   * @param collector
   * 
   * @see backtype.storm.spout.ISpout#open(java.util.Map, backtype.storm.task.TopologyContext,
   *      backtype.storm.spout.SpoutOutputCollector)
   */
  @Override
  public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
    this.collector = collector;

    // Fire up the server
    this.server.start();
  }
}
