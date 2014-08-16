package cs455.overlay.registry;

import cs455.overlay.connection.ConnectionException;
import cs455.overlay.connection.MessageCallback;
import cs455.overlay.connection.ConnectionManager;
import cs455.overlay.message.*;
import cs455.overlay.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * handles the message for Registry.
 */
public class RegistryNode implements MessageCallback {

    Logger logger = Logger.getLogger(RegistryNode.class.getName());

    private ConnectionManager connectionManager;
    private RegistryInfo registryInfo;

    private Map<NodeKey, MemberNode> memberNodes;
    private OverlayManager overlayManager;
    private List<NodeKey> completedNodes;
    private List<TrafficSummary> trafficSummaries;

    public RegistryNode(RegistryInfo registryInfo) {
        this.registryInfo = registryInfo;
        this.memberNodes = new ConcurrentHashMap<NodeKey, MemberNode>();
        this.completedNodes = new Vector<NodeKey>();
        this.trafficSummaries = new Vector<TrafficSummary>();
    }

    public void messageReceived(Message message, String ip) {
        int messageType = message.getMessageType();
        switch (messageType) {
            case Constants.REG_REQ_MESSAGE_TYPE: {
                processRegistryRequestMessage((RegisterRequest) message, ip);
                break;
            }
            case Constants.DEREG_REQ_MESSAGE_TYPE: {
                processDeregistrationRequestMessage((DeRegisterRequest) message, ip);
                break;
            }
            case Constants.TASK_COMPLETE_MESSAGE_TYPE: {
                processTaskComplete((TaskComplete) message);
                break;
            }
            case Constants.TRAFFIC_SUMMARY_MESSAGE_TYPE: {
                processTrafficSummary((TrafficSummary) message);
                break;
            }

        }
    }

    private void processTrafficSummary(TrafficSummary trafficSummary) {
        this.trafficSummaries.add(trafficSummary);

        if (this.trafficSummaries.size() == this.memberNodes.size()) {
            //i.e we have got all traffic summeries. display the results.
            int sendSum = 0;
            int receiveSum = 0;
            int sendMessageSum = 0;
            int receiveMessageSum = 0;
            System.out.println("|---------------------------------------------------------------------------------------------------------------\t|");
            System.out.println("|Node   \t|Number of     \t|Number of         \t|Summation of        \t|Summation of        \t|Number of        \t|");
            System.out.println("|       \t|messages sent \t|messages received \t|sent messages       \t|received messages   \t|messages relayed \t|");
            System.out.println("|---------------------------------------------------------------------------------------------------------------\t|");
            for (TrafficSummary summary : this.trafficSummaries) {
                System.out.println("|" + summary.getIpAddress() + ":" + summary.getPortNumber()
                        + " \t|" + summary.getSendTracker() + "         \t|" + summary.getReceiveTracker() + "             \t|" +
                        summary.getSendSummation() + " \t|" + summary.getReceiveSummation() + " \t|" + summary.getRelayTracker() + "            \t|");
                System.out.println("|---------------------------------------------------------------------------------------------------------------\t|");

                sendSum += summary.getSendTracker();
                receiveSum += summary.getReceiveTracker();
                sendMessageSum += summary.getSendSummation();
                receiveMessageSum += summary.getReceiveSummation();
            }
            System.out.println("|Sum \t|" + sendSum + "         \t|" + receiveSum + "             \t|" +
                    sendMessageSum + " \t|" + receiveMessageSum + " \t|");

        }
    }

    private void processTaskComplete(TaskComplete taskComplete) {
        this.completedNodes.add(new NodeKey(taskComplete.getIpAddress(), taskComplete.getPort()));
        if (this.completedNodes.size() == this.memberNodes.size()) {
            //i.e every one has completed.
            // wait a minite just to make sure every one completed.
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
            }
            // send pull traffic summary message.
            PullTrafficSummary pullTrafficSummary = new PullTrafficSummary();
            for (NodeKey nodeKey : this.memberNodes.keySet()) {
                try {
                    this.connectionManager.sendMessage(pullTrafficSummary, nodeKey.getIp(), nodeKey.getPort());
                } catch (MessageProcessingException e) {
                    logger.log(Level.SEVERE, "Can not process the message ", e);
                } catch (ConnectionException e) {
                    logger.log(Level.SEVERE, "Can not connect to the other end ", e);
                }
            }
        }
    }

    private void processDeregistrationRequestMessage(DeRegisterRequest deRegisterRequest, String ip) {
        NodeKey nodeKey = new NodeKey(deRegisterRequest.getIpAddress(), deRegisterRequest.getPort());
        if (!deRegisterRequest.getIpAddress().equals(ip)) {
            sendDeRegisterResponse(
                    ip, deRegisterRequest.getPort(), "Socket ip address and message ip address does not match");
        } else if (!this.memberNodes.containsKey(nodeKey)) {
            sendDeRegisterResponse(ip, deRegisterRequest.getPort(),
                    "There is no entry with " + deRegisterRequest.getIpAddress() + " " + deRegisterRequest.getPort());
        } else {
            logger.log(Level.INFO, "Removing the node with ip " + deRegisterRequest.getIpAddress() + " " + deRegisterRequest.getPort());
            this.memberNodes.remove(nodeKey);
        }
    }

    private void sendDeRegisterResponse(String ip, int port, String message) {
        DeRegisterResponse deRegisterResponse = new DeRegisterResponse(message);
        try {
            this.connectionManager.sendMessage(deRegisterResponse, ip, port);
        } catch (MessageProcessingException e) {
            logger.log(Level.SEVERE, "Can not process the response message ", e);
        } catch (ConnectionException e) {
            logger.log(Level.SEVERE, "Can not send the response message ", e);
        }
    }

    private void processRegistryRequestMessage(RegisterRequest registerRequest, String ip) {

        logger.log(Level.INFO, "Register request message received from "
                + registerRequest.getIpAddress() + " port " + registerRequest.getPortNumber());
        RegisterResponse registerResponse = new RegisterResponse();
        NodeKey nodeKey = new NodeKey(registerRequest.getIpAddress(), registerRequest.getPortNumber());
        if (!registerRequest.getIpAddress().equals(ip)) {
            //this is an already existing node.
            registerResponse.setStatusCode(Constants.STATUS_FAILUER);
            registerResponse.setAdditionalInfo("Socket ip addreses and the message ip address does not match");
        } else if (this.memberNodes.containsKey(nodeKey)) {
            //this is an already existing node.
            registerResponse.setStatusCode(Constants.STATUS_FAILUER);
            registerResponse.setAdditionalInfo("Member id " + nodeKey.toString() + " already exists");
        } else {
            registerResponse.setStatusCode(Constants.STATUS_SUCESS);
            registerResponse.setAdditionalInfo("Registreation request is sucessfull. " +
                    "The number of messaging nodes currently constituting the " +
                    "overlay is (" + this.memberNodes.size() + ")");
        }
        try {
            this.connectionManager.sendMessage(registerResponse, ip, registerRequest.getPortNumber());
            MemberNode memberNode =
                    new MemberNode(registerRequest.getIpAddress(),
                            registerRequest.getPortNumber());
            this.memberNodes.put(nodeKey, memberNode);

        } catch (MessageProcessingException e) {
            logger.log(Level.SEVERE, "Can not process the response message ", e);
        } catch (ConnectionException e) {
            logger.log(Level.SEVERE, "Can not send the response message ", e);
        }
    }

    public void setupOverlay(int connections) {
        List<MemberNode> memberNodes = new ArrayList<MemberNode>(this.memberNodes.values());
        this.overlayManager = new OverlayManager(memberNodes, connections);
        this.overlayManager.setUpOverlay();

        //Now send the messages to other nodes to connect.
        this.overlayManager.sendConnectionMessages(this.connectionManager);
    }

    public void listMessagingNodes() {
        for (NodeKey nodeKey : this.memberNodes.keySet()) {
            System.out.println("Host name " + nodeKey.getIp() + " port " + nodeKey.getPort());
        }
    }

    public void sendOverlayLinkWeights() {
        Message linkWeights = this.overlayManager.getLinkWeights();
        for (NodeKey nodeKey : this.memberNodes.keySet()) {
            try {
                this.connectionManager.sendMessage(linkWeights, nodeKey.getIp(), nodeKey.getPort());
            } catch (MessageProcessingException e) {
                logger.log(Level.SEVERE, "Can not process the message ", e);
            } catch (ConnectionException e) {
                logger.log(Level.SEVERE, "Can not send the message ", e);
            }
        }
    }

    public void start() {
        //start the network message sending.
        for (NodeKey key : this.memberNodes.keySet()) {
            try {
                this.connectionManager.sendMessage(new TaskInitiate(), key.getIp(), key.getPort());
            } catch (MessageProcessingException e) {
                logger.log(Level.SEVERE, "Can not process the message ", e);
            } catch (ConnectionException e) {
                logger.log(Level.SEVERE, "Can not connect to the back end", e);
            }
        }

    }

    public void listWeights() {
        this.overlayManager.displayLinks();
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
}
