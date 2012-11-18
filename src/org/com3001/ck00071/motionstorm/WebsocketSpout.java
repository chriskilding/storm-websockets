/**
 * WebsocketSpout.java
 */

package org.com3001.ck00071.motionstorm;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import org.json.simple.JSONObject;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;
/**
 * A spout to handle messages pushed over Websockets connections.
 * 
 * @author Christopher Kilding
 * @date 17/11/2012
 */
public class WebsocketSpout extends BaseRichSpout {

  private SimpleWebSocketServer server;
  
  private SpoutOutputCollector collector;
  
  private BlockingQueue<JSONObject> queue;
  
  /**
   * @param conf
   * @param context
   * @param collector
   *
   * @see backtype.storm.spout.ISpout#open(java.util.Map, backtype.storm.task.TopologyContext, backtype.storm.spout.SpoutOutputCollector)
   */
  @Override
  public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {    
    WSMessageListener ls = new WSMessageListener() {
      
      @Override
      public void onMessageReceived(String message) {
        // Push message onto queue
        // DO NOT WAIT if space not available - latency is critical
        // if it can't do this immediately, just drop it
        queue.offer(new JSONObject());
      }
    };
    
    this.server = new SimpleWebSocketServer(ls);
    this.collector = collector;
    
    // Fire up the server
    this.server.start();
        
  }

  /**
   * THIS IS NOT GUARANTEED TO BE CALLED.
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

 
  @Override
  public void nextTuple() {
    // Pop latest thing off the queue
    JSONObject nextObj = this.queue.poll();
    if (nextObj == null) {
      // Silent return, and sleep to avoid spamming the CPU
      Utils.sleep(1);
    } else {
      // Only emit if we actually have an object to send
      // i.e. DON'T EMIT NULLS
      this.collector.emit(new Values(nextObj));
    }
  }

  /**
   * @param declarer
   *
   * @see backtype.storm.topology.IComponent#declareOutputFields(backtype.storm.topology.OutputFieldsDeclarer)
   */
  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    // TODO Auto-generated method stub
    
  }
}
