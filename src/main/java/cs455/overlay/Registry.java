package cs455.overlay;

import cs455.overlay.connection.ConnectionManager;
import cs455.overlay.registry.RegistryInfo;
import cs455.overlay.registry.RegistryNode;
import cs455.overlay.util.Constants;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * this class initiate the registry proccess. It reads the commnd line arguments initiate the connection manager
 * and listen for other commands.
 */
public class Registry {

    private static Logger logger = Logger.getLogger(Messaging.class.getName());

    public static void main(String[] args) {
        RegistryInfo registryInfo = new RegistryInfo(Integer.parseInt(args[0]));
        RegistryNode registryNode = new RegistryNode(registryInfo);
        ConnectionManager connectionManager =
                new ConnectionManager(registryInfo.getPort(), registryNode);
        registryNode.setConnectionManager(connectionManager);
        connectionManager.start();

        //read the in put from the command line and execute the commands.
        while (true) {
            System.out.print("\n>>");
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            try {
                String commandString = bufferRead.readLine();
                if (commandString.equals(Constants.LIST_MESSAGING_NODES)) {
                    registryNode.listMessagingNodes();
                } else if (commandString.equals(Constants.LIST_WEIGHTS)) {
                    registryNode.listWeights();
                } else if (commandString.startsWith(Constants.SETUP_OVERLAY)) {
                    String[] parameters = commandString.trim().split("\\s+");
                    registryNode.setupOverlay(Integer.parseInt(parameters[1]));
                } else if (commandString.equals(Constants.SEND_OVERLAY_LINK_WEIGHTS)){
                    registryNode.sendOverlayLinkWeights();
                } else if (commandString.equals(Constants.START)){
                    registryNode.start();
                } else {
                    System.out.println("Unknown command please enter correct command ...");
                }

            } catch (Exception e) {
                logger.log(Level.SEVERE, "Can not read command from the console ", e);
            }
        }
    }
}
