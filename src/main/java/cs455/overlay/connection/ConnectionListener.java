package cs455.overlay.connection;

import cs455.overlay.message.Message;
import cs455.overlay.message.MessageFactory;
import cs455.overlay.message.MessageProcessingException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * this class starts a server sockets and listens to that. Once a connection happens it passes the socket
 * to Connection manager through call back.
 */
public class ConnectionListener implements Runnable {

    Logger logger = Logger.getLogger(ConnectionListener.class.getName());

    private int port;
    private ConnectionCallback connectionCallback;

    public ConnectionListener(int port, ConnectionCallback connectionCallback) {
        this.port = port;
        this.connectionCallback = connectionCallback;
    }

    public void run() {
        try {
            ServerSocket serverSocket = null;
            while (true){

                try {
                    serverSocket = new ServerSocket(this.port);
                    logger.log(Level.INFO, "Sucessfully connected to port " + this.port);
                    synchronized (this){
                        this.notifyAll();
                    }
                    break;
                } catch (IOException e) {
                    this.port++;
                }
            }

            while (true) {
                Socket socket = serverSocket.accept();
                this.connectionCallback.connectionAccepted(socket);
            }

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Can not accept connections for port " + this.port, e);
        } catch (ConnectionException e) {
            logger.log(Level.SEVERE, "Can not process the message ", e);
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
