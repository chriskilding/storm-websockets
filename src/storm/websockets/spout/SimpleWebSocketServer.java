/**
 * StormWebSocketServer.java
 */

package storm.websockets.spout;

import java.io.IOException;

import com.netiq.websocket.WebSocket;
import com.netiq.websocket.WebSocketServer;


/**
 * Wrapper round the WebSocketServer class.
 * Provides a simplified WebSocketServer which hides the details.
 * 
 * @author Christopher Kilding
 * @date 17/11/2012
 */
public class SimpleWebSocketServer {

  private WSMessageListener listener;
  
  private WebSocketServer ws = new WebSocketServer() {
    
    @Override
    public void onError(Throwable ex) {
      // TODO Auto-generated method stub
      
    }
    
    @Override
    public void onClientOpen(WebSocket conn) {
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
      listener.onMessageReceived(message);
    }
    
    @Override
    public void onClientClose(WebSocket conn) {
      // TODO Auto-generated method stub
      
    }
  };
  
  /**
   * @param listener
   *  a list of functions that will get executed by this server
   */
  public SimpleWebSocketServer(WSMessageListener listener) {
    this.listener = listener;
  }

  public void start() {
    this.ws.start();
  }
  
  public void stop() throws IOException {
    this.ws.stop();
  }

}
