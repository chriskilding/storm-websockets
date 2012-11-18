/**
 * WSMessageListener.java
 */

package storm.websockets.spout;


/**
 * @author Christopher Kilding
 * @date 17/11/2012
 */
public interface WSMessageListener {
  public void onMessageReceived(String message);
}
