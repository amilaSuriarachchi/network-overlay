package cs455.overlay.connection;

import cs455.overlay.message.Message;
import cs455.overlay.message.MessageProcessingException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Connection manager handles the tcp connections among the nodes. Connection manager keeps a list of connections
 * opened to other nodes with their ip address and the port number. Typically when two node communicate there are two
 * tcp connections established to send and receive messages. Basically sending part initiate the connection and buffers
 * that. When a receiving side receives a connection it starts a thread to read messages continuously read from that.
 */
public class ConnectionManager implements ConnectionCallback {

    // port of this node.
    private int port;
    // call back class to pass the messages to upper layer
    private MessageCallback messageCallback;
    // connection cache
    private Map<RemoteSocket, Socket> connectionsMap;

    public ConnectionManager(int port, MessageCallback messageCallback) {
        this.port = port;
        this.messageCallback = messageCallback;
        this.connectionsMap = new ConcurrentHashMap<RemoteSocket, Socket>();
    }

    /**
     * this method open a connection if there is no connection to that ip address from and to that port. Otherwise use
     * existing one.
     * @param message
     * @param ipAddress
     * @param port
     * @throws MessageProcessingException
     * @throws ConnectionException
     */
    public void sendMessage(Message message, String ipAddress, int port)
            throws MessageProcessingException, ConnectionException {
        try {
            RemoteSocket remoteSocket = new RemoteSocket(ipAddress, port);
            if (!this.connectionsMap.containsKey(remoteSocket)) {
                synchronized (this.connectionsMap) {
                    if (!this.connectionsMap.containsKey(remoteSocket)) {
                        Socket socket = new Socket(ipAddress, port);
                        // start a receiver for back channel
                        this.connectionsMap.put(remoteSocket, socket);
                    }
                }
            }
            Socket socket = this.connectionsMap.get(remoteSocket);
            synchronized (socket) {
                OutputStream outputStream = socket.getOutputStream();
                message.serialize(outputStream);
                outputStream.flush();
            }
        } catch (IOException e) {
            throw new ConnectionException("Can not connect to server ", e);
        }
    }

    /**
     * starts the connection listener and wait until it sets the correct port.
     */
    public void start() {
        ConnectionListener connectionListener = new ConnectionListener(this.port, this);
        Thread thread = new Thread(connectionListener);
        thread.start();
        // we need to wait here until connection manager start listing to port and determine the correct port.
        synchronized (connectionListener){
            try {
                connectionListener.wait();
            } catch (InterruptedException e) {}
        }
        this.port = connectionListener.getPort();
    }

    public String getIP() throws ConnectionException {
        String ip = null;
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (!inetAddress.getHostAddress().equals("127.0.0.1") &&
                            !inetAddress.getHostAddress().equals("0:0:0:0:0:0:0:1%1") &&
                                 inetAddress.getHostAddress().matches("[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}")) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
            return ip;
        } catch (SocketException e) {
            throw new ConnectionException("Can not get the network interfaces");
        }
    }

    /**
     * When the connection listener accept a connection for a client it passes the socket to connection call back.
     * Once we get a socket connection we start a thread to receive packets countinuouly from that.
     * @param socket
     * @throws ConnectionException
     */
    public void connectionAccepted(Socket socket) throws ConnectionException {
        MessageListener messageListener =
                new MessageListener(socket, this.messageCallback);
        Thread thread = new Thread(messageListener);
        thread.start();

    }

    public int getPort() {

        return port;

    }

}
