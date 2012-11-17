/**
 * WebsocketSpout.java
 */

package org.com3001.ck00071.motionstorm;

import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;


/**
 * A custom spout written to handle messages pushed over Websockets from a browser.
 * This will cut the overhead from converting to a Kestrel / AMQP / whatever queue format first.
 * 
 * @author ck00071
 * @date 17/11/2012
 */
public class WebsocketSpout implements IRichSpout {

  /**
   * @param conf
   * @param context
   * @param collector
   *
   * @see backtype.storm.spout.ISpout#open(java.util.Map, backtype.storm.task.TopologyContext, backtype.storm.spout.SpoutOutputCollector)
   */
  @Override
  public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
    // TODO Auto-generated method stub

  }

  /**
   * 
   *
   * @see backtype.storm.spout.ISpout#close()
   */
  @Override
  public void close() {
    // TODO Auto-generated method stub

  }

  /**
   * 
   *
   * @see backtype.storm.spout.ISpout#activate()
   */
  @Override
  public void activate() {
    // TODO Auto-generated method stub

  }

  /**
   * 
   *
   * @see backtype.storm.spout.ISpout#deactivate()
   */
  @Override
  public void deactivate() {
    // TODO Auto-generated method stub

  }

  /**
   * 
   *
   * @see backtype.storm.spout.ISpout#nextTuple()
   */
  @Override
  public void nextTuple() {
    // TODO Auto-generated method stub

  }

  /**
   * @param msgId
   *
   * @see backtype.storm.spout.ISpout#ack(java.lang.Object)
   */
  @Override
  public void ack(Object msgId) {
    // TODO Auto-generated method stub

  }

  /**
   * @param msgId
   *
   * @see backtype.storm.spout.ISpout#fail(java.lang.Object)
   */
  @Override
  public void fail(Object msgId) {
    // TODO Auto-generated method stub

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

  /**
   * @return
   *
   * @see backtype.storm.topology.IComponent#getComponentConfiguration()
   */
  @Override
  public Map<String, Object> getComponentConfiguration() {
    // TODO Auto-generated method stub
    return null;
  }

}
