/**
 * WebsocketSpout.java
 */

package com.github.themasterchef.storm_websockets;

import java.io.IOException;
import java.util.Map;

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
  private static final long              serialVersionUID = -6858503694954676815L;

  /** A WebSocket queue broker. Don't serialize it, it'll be gone when you deserialize! */
  private transient WebsocketQueueBroker ws;

  /** Output collector as supplied in the open() call. */
  private SpoutOutputCollector           collector;

  /**
   * THIS IS NOT GUARANTEED TO BE CALLED. Often the spout process just gets `kill`ed.
   * 
   * @see backtype.storm.spout.ISpout#close()
   */
  @Override
  public void close() {
    // Shut the show down
    try {
      this.ws.stop();
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
    String nextObj = this.ws.poll();
    if (nextObj == null) {
      // Silent return, and sleep to avoid spamming the CPU
      Utils.sleep(5L);
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

    // Need to instantiate - it's transient, remember!
    this.ws = new WebsocketQueueBroker(3000);
    // Fire up the server
    this.ws.start();
  }
}
