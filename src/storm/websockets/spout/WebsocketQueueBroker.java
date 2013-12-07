/**
 * WebsocketQueueServer.java
 */

package storm.websockets.spout;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.simple.JSONObject;

import com.netiq.websocket.WebSocket;
import com.netiq.websocket.WebSocketServer;

/**
 * A queue broker which accepts messages from Websockets clients and shovels them onto an in-memory data store (for speed).
 * 
 * @author Christopher Kilding
 * @date 29/11/2012
 */
public class WebsocketQueueBroker extends WebSocketServer {

  /** Queue to mediate between WS server and the class user. May need to be replaced with something more robust. */
  private final BlockingQueue<String> queue = new LinkedBlockingQueue<String>();

  /**
   * Start the queue server listening on the default WS port.
   */
  public WebsocketQueueBroker() {
    super();
  }

  /**
   * Start queue server.
   * 
   * @param port
   *          a particular port to use for WS
   */
  public WebsocketQueueBroker(int port) {
    super(port);
  }

  /**
   * @param conn
   * 
   * @see com.netiq.websocket.WebSocketServer#onClientClose(com.netiq.websocket.WebSocket)
   */
  @Override
  public void onClientClose(WebSocket conn) {
    // TODO Auto-generated method stub

  }

  /**
   * Takes a new message from a WS client and pushes it into the data store.
   * 
   * @param conn
   *          the connection
   * @param message
   *          the message to store
   * 
   * @see com.netiq.websocket.WebSocketServer#onClientMessage(com.netiq.websocket.WebSocket, java.lang.String)
   */
  @Override
  public void onClientMessage(WebSocket conn, String message) {
    // System.out.println(message);
    // new JSONParser().parse(message);
    
    // Use add(), which is faster as it does not do capacity checks
    try {
      this.queue.add(message);
    }
    catch (IllegalStateException e) {
      System.err.println(e.getMessage());
      // don't care
    }
  }

  /**
   * A new client opens a connection to this server. We need to greet them.
   * 
   * @param conn
   *          the client's connection
   * 
   * @see com.netiq.websocket.WebSocketServer#onClientOpen(com.netiq.websocket.WebSocket)
   */
  @Override
  public void onClientOpen(WebSocket conn) {
    JSONObject msg = new JSONObject();

    // meet n greet
    msg.put("welcome", "client");

    // a handle for the client to get at the output data from Storm
    // i.e. socket.id
    msg.put("outputDataHandle", "1");

    try {
      conn.send(msg.toJSONString());
    }
    catch (IOException e) {
      // Forward to error method
      this.onError(e);
    }
  }

  /**
   * Handles exceptions that may be triggered by WS activities
   * 
   * @param ex
   *          the exception
   * 
   * @see com.netiq.websocket.WebSocketServer#onError(java.lang.Throwable)
   */
  @Override
  public void onError(Throwable ex) {
    System.err.println("Error in WS queue broker");
    System.err.println(ex.getMessage());
  }

  /**
   * 
   * @return the next element that needs processing
   */
  public String poll() {
    return this.queue.poll();
  }

}
