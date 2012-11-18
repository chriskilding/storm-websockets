/**
 * WSMessageListener.java
 */

package storm.websockets.spout;

/**
 * Intermediary interface, which prevents a server that needs to push to a queue from having to know explicitly about that queue.
 * (The class that builds the server object will pass an implementation of this interface to the server.) 
 * 
 * @author Christopher Kilding
 * @date 17/11/2012
 */
public interface WSMessageListener {

  public void onMessageReceived(String message);
}
