package cs455.overlay;

import cs455.overlay.connection.ConnectionException;
import cs455.overlay.connection.ConnectionManager;
import cs455.overlay.message.MessageProcessingException;
import cs455.overlay.node.MessagingNode;
import cs455.overlay.node.NodeInfo;
import cs455.overlay.util.Constants;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * this class does the Message Node initiating. It start the connection manager and listen for connections.
 */
public class Messaging {

    private static Logger logger = Logger.getLogger(Messaging.class.getName());

    public static void main(String[] args) {

        try {
            NodeInfo nodeInfo = new NodeInfo(args[0], Integer.parseInt(args[1]));
            MessagingNode messagingNode = new MessagingNode(nodeInfo);

            //we pass 4000 as the initial number.
            ConnectionManager connectionManager = new ConnectionManager(4000, messagingNode);
            messagingNode.setConnectionManager(connectionManager);
            connectionManager.start();
            nodeInfo.setIpAddress(connectionManager.getIP());
            nodeInfo.setPortNumber(connectionManager.getPort());
            messagingNode.start();


            //read the in put from the command line and execute the commands.
            while (true) {
                System.out.print("\n>>");
                BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
                try {
                    String commandString = bufferRead.readLine();
                    if (Constants.PRINT_SHORTEST_PATH_CMD.equals(commandString)) {
                        messagingNode.printShortestPath();
                    } else if (Constants.EXIT_OVERLAY.equals(commandString)) {
                        messagingNode.exitOverlay();
                    } else {
                        System.out.println("Unknown command please enter correct command ...");
                    }
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Can not read command from the console ", e);
                }
            }
        } catch (MessageProcessingException e) {
            logger.log(Level.SEVERE, "Can not start the Messaging node ", e);
        } catch (ConnectionException e) {
            logger.log(Level.SEVERE, "Can not start the Messaging node ", e);
        }

    }
}
