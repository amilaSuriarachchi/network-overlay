package cs455.overlay.connection;

import cs455.overlay.message.Message;

/**
 * this interface is used to pass the messages back to Registry and message node classes.
 */
public interface MessageCallback {

    public void messageReceived(Message message, String ip);
}
