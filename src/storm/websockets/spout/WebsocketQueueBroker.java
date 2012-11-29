/**
 * WebsocketQueueServer.java
 */

package storm.websockets.spout;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.simple.JSONObject;

import com.netiq.websocket.Draft;
import com.netiq.websocket.WebSocket;
import com.netiq.websocket.WebSocketServer;


/**
 * @author chris
 */
public class WebsocketQueueBroker extends WebSocketServer {

  /** Queue to mediate between WS server and the class user. May need to be replaced with something more robust. */
  private final BlockingQueue<String>    queue            = new LinkedBlockingQueue<String>();
  
  /**
   * Start the queue server listening on the default WS port.
   */
  public WebsocketQueueBroker() {
    super();
  }

  /**
   * Start queue server.
   * @param port a particular port to use for WS
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
   * @param conn the connection
   * @param message the message to store
   *
   * @see com.netiq.websocket.WebSocketServer#onClientMessage(com.netiq.websocket.WebSocket, java.lang.String)
   */
  @Override
  public void onClientMessage(WebSocket conn, String message) {
    System.out.println("WS queue broker got something");
    this.queue.offer(message);
  }

  /**
   * A new client opens a connection to this server. We need to greet them.
   * 
   * @param conn the client's connection
   *
   * @see com.netiq.websocket.WebSocketServer#onClientOpen(com.netiq.websocket.WebSocket)
   */
  @Override
  public void onClientOpen(WebSocket conn) {
    // Meet n greet
    JSONObject msg = new JSONObject();
    msg.put("welcome", "client");
    
    // a handle for the client to get at the output data from Storm
    // msg.put("outputDataHandle", conn.);
    //  socket.id 
    
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
   * @param ex the exception
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
