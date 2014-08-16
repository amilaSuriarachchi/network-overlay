package cs455.overlay.connection;

import cs455.overlay.message.Message;
import cs455.overlay.message.MessageFactory;
import cs455.overlay.message.MessageProcessingException;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * this class continuously listens to the incoming messages and pass them back to higher layers.
 * first it create the message using message factory and pass that back to call back.
 */
public class MessageListener implements Runnable {

    Logger logger = Logger.getLogger(MessageListener.class.getName());

    private Socket socket;
    private MessageCallback messageCallback;

    public MessageListener(Socket socket, MessageCallback messageCallback) {
        this.socket = socket;
        this.messageCallback = messageCallback;
    }

    public void run() {
        while (true){
            try {
                Message message = MessageFactory.getMessage(socket.getInputStream());
                this.messageCallback.messageReceived(message, socket.getInetAddress().getHostAddress());
            } catch (MessageProcessingException e) {
                //TODO: think what to do ? do we need to close the connection or clean it up.
                this.logger.log(Level.SEVERE, "Can not process the message");
                break;
            } catch (IOException e) {
                //TODO: think what to do ? do we need to close the connection or clean it up.
                this.logger.log(Level.SEVERE, "Can not process the message");
                break;
            }
        }
    }
}
