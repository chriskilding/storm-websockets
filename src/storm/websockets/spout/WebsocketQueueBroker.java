/**
 * WebsocketQueueServer.java
 */

package storm.websockets.spout;

import java.util.concurrent.BlockingQueue;

import com.netiq.websocket.Draft;
import com.netiq.websocket.WebSocket;
import com.netiq.websocket.WebSocketServer;


/**
 * @author chris
 */
public class WebsocketQueueBroker extends WebSocketServer {

  private final BlockingQueue<String> queue;
  
  /**
   * Start the queue server listening on the default WS port.
   */
  public WebsocketQueueBroker(BlockingQueue<String> queue) {
    super();
    this.queue = queue;
  }

  /**
   * Start queue server.
   * @param port a particular port to use for WS
   */
  public WebsocketQueueBroker(int port, BlockingQueue<String> queue) {
    super(port);
    this.queue = queue;
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
   * @param conn
   * @param message
   *
   * @see com.netiq.websocket.WebSocketServer#onClientMessage(com.netiq.websocket.WebSocket, java.lang.String)
   */
  @Override
  public void onClientMessage(WebSocket conn, String message) {
    this.queue.offer(message);
  }

  /**
   * @param conn
   *
   * @see com.netiq.websocket.WebSocketServer#onClientOpen(com.netiq.websocket.WebSocket)
   */
  @Override
  public void onClientOpen(WebSocket conn) {
    // TODO Auto-generated method stub

  }

  /**
   * @param ex
   *
   * @see com.netiq.websocket.WebSocketServer#onError(java.lang.Throwable)
   */
  @Override
  public void onError(Throwable ex) {
    // TODO Auto-generated method stub

  }

}
