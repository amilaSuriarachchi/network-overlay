package cs455.overlay.node;

import cs455.overlay.connection.MessageCallback;
import cs455.overlay.connection.ConnectionException;
import cs455.overlay.connection.ConnectionManager;
import cs455.overlay.message.*;
import cs455.overlay.node.dijkstra.Algorithum;
import cs455.overlay.node.dijkstra.Vertex;
import cs455.overlay.node.dijkstra.VertexLink;
import cs455.overlay.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * class that process the messages for messaging node. This class implements the Message callback to receive
 * messages from lower layer and do the message handling part.
 */
public class MessagingNode implements MessageCallback {

    Logger logger = Logger.getLogger(MessagingNode.class.getName());

    private NodeInfo nodeInfo;
    private ConnectionManager connectionManager;
    private Algorithum algorithum;

    private int sendTracker;
    private int receiveTracker;
    private int relayTracker;
    private long sendSummantion;
    private long receiveSummation;

    public MessagingNode(NodeInfo nodeInfo) {
        this.nodeInfo = nodeInfo;
    }

    // at this method messaging node suppose to do the initialization stuff.
    public void start() throws MessageProcessingException, ConnectionException {
        // first sends a register request message
        RegisterRequest registerRequest =
                new RegisterRequest(this.nodeInfo.getIpAddress(),
                        this.nodeInfo.getPortNumber());
        this.connectionManager.sendMessage(registerRequest,
                this.nodeInfo.getRegistryHost(),
                this.nodeInfo.getRegistryPort());


    }

    public void messageReceived(Message message, String ip) {
        switch (message.getMessageType()) {
            case Constants.REG_RES_MESSAGE_TYPE: {
                RegisterResponse registerResponse = (RegisterResponse) message;
                logger.log(Level.INFO, registerResponse.getAdditionalInfo());
                break;
            }
            case Constants.DEREG_RES_MESSAGE_TYPE: {
                DeRegisterResponse deRegisterResponse = (DeRegisterResponse) message;
                logger.log(Level.INFO, deRegisterResponse.getInfo());
                break;
            }
            case Constants.MESSAGING_NODES_LIST_MESSAGE_TYPE: {

                MessagingNodesList messagingNodesList = (MessagingNodesList) message;
                processMessageNodesList(messagingNodesList);
                break;
            }
            case Constants.CON_REQ_MESSAGE_TYPE: {
                ConnectionRequest connectionRequest = (ConnectionRequest) message;
                logger.log(Level.INFO, "Connection request received from " + connectionRequest.getIpAddress() + ":" + connectionRequest.getPortNumber());
                ConnectionResponse connectionResponse = new ConnectionResponse(this.nodeInfo.getIpAddress(), this.nodeInfo.getPortNumber());
                try {
                    this.connectionManager.sendMessage(
                            connectionResponse, connectionRequest.getIpAddress(), connectionRequest.getPortNumber());
                } catch (MessageProcessingException e) {
                    logger.log(Level.INFO, "Can not process the message ", e);
                } catch (ConnectionException e) {
                    logger.log(Level.INFO, "Can not connect to the remote server", e);
                }
                break;
            }
            case Constants.CON_RES_MESSAGE_TYPE: {
                ConnectionResponse connectionResponse = (ConnectionResponse) message;
                logger.log(Level.INFO, "Connection response received from "
                        + connectionResponse.getIpAddress() + " port " + connectionResponse.getPort());
                break;
            }
            case Constants.LINK_WEIGHTS_MESSAGE_TYPE: {
                processLinkWeightMessage((LinkWeights) message);
                break;
            }
            case Constants.TASK_INITIATE_MESSAGE_TYPE: {
                processStartMessage();
                break;
            }
            case Constants.DATA_MESSAGE_MESSAGE_TYPE: {
                processDataMessage((DataMessage) message);
                break;
            }
            case Constants.PULL_TRAFFIC_SUMMARY_MESSAGE_TYPE: {
                processPullTrafficSummary();
                break;
            }

        }
    }

    public void processPullTrafficSummary() {
        TrafficSummary trafficSummary =
                new TrafficSummary(this.nodeInfo.getIpAddress(), this.nodeInfo.getPortNumber());
        trafficSummary.setSendTracker(this.sendTracker);
        trafficSummary.setSendSummation(this.sendSummantion);
        trafficSummary.setReceiveTracker(this.receiveTracker);
        trafficSummary.setReceiveSummation(this.receiveSummation);
        trafficSummary.setRelayTracker(this.relayTracker);

        try {
            this.connectionManager.sendMessage(trafficSummary,
                               this.nodeInfo.getRegistryHost(), this.nodeInfo.getRegistryPort());
        } catch (MessageProcessingException e) {
            logger.log(Level.SEVERE, "Can not process the message ", e);
        } catch (ConnectionException e) {
            logger.log(Level.SEVERE, "Can not connect to node ", e);
        }


    }

    private void processDataMessage(DataMessage dataMessage) {
        if (!dataMessage.isDestination()) {
            synchronized (this) {
                this.relayTracker++;
            }
            forwardMessage(dataMessage);
        } else {
            synchronized (this) {
                this.receiveTracker++;
                this.receiveSummation += dataMessage.getMessage();
            }
        }
    }

    private void processStartMessage() {
        for (int i = 0; i < 5000; i++) {
            sendMessage();
        }

        //send the task complete message
        TaskComplete taskComplete = new TaskComplete(this.nodeInfo.getIpAddress(), this.nodeInfo.getPortNumber());
        try {
            this.connectionManager.sendMessage(taskComplete,
                    this.nodeInfo.getRegistryHost(), this.nodeInfo.getRegistryPort());
        } catch (MessageProcessingException e) {
            logger.log(Level.SEVERE, "Can not process the message ", e);
        } catch (ConnectionException e) {
            logger.log(Level.SEVERE, "Can not connect to other end ", e);
        }
    }

    private void sendMessage() {
        Vertex nodeToSend = this.algorithum.getRandomNode();
        List<Vertex> shortestPath = this.algorithum.shortestPath(nodeToSend);
        shortestPath.remove(0); // remove the first message since this is the first one.
        for (int i = 0; i < 5; i++) {
            DataMessage dataMessage = new DataMessage();
            for (Vertex vertex : shortestPath) {
                dataMessage.addNode(new Node(vertex.getIpAddress(), vertex.getPort()));
            }
            synchronized (this) {
                this.sendTracker++;
                this.sendSummantion += dataMessage.getMessage();
            }
            forwardMessage(dataMessage);
        }


    }

    private void forwardMessage(DataMessage dataMessage) {
        try {
            Node nodeToSend = dataMessage.getNodeToSend();
            this.connectionManager.sendMessage(dataMessage, nodeToSend.getIpAddress(), nodeToSend.getPort());
        } catch (MessageProcessingException e) {
            logger.log(Level.SEVERE, "Can not process the message ", e);
        } catch (ConnectionException e) {
            logger.log(Level.SEVERE, "Connection creation problem ", e);
        }
    }

    private void processLinkWeightMessage(LinkWeights linkWeights) {

        //creating the graph out of that.
        List<Vertex> graph = new ArrayList<Vertex>();
        for (NodeLink nodeLink : linkWeights.getNodeLinks()) {
            Vertex vertex1 = getVertex(graph, nodeLink.getNode1().getIpAddress(), nodeLink.getNode1().getPort());
            Vertex vertex2 = getVertex(graph, nodeLink.getNode2().getIpAddress(), nodeLink.getNode2().getPort());
            VertexLink vertexLink1 = new VertexLink(nodeLink.getWeight(), vertex2);
            vertex1.addLink(vertexLink1);
            VertexLink vertexLink2 = new VertexLink(nodeLink.getWeight(), vertex1);
            vertex2.addLink(vertexLink2);
        }

        this.algorithum = new Algorithum(graph, getVertex(graph, this.nodeInfo.getIpAddress(), this.nodeInfo.getPortNumber()));
        this.algorithum.calculateDistace();
        this.algorithum.displayNodes();
        this.algorithum.displayShortestPaths();

    }

    private Vertex getVertex(List<Vertex> graph, String ipAddres, int port) {
        Vertex vertex = new Vertex(ipAddres, port);
        if (!graph.contains(vertex)) {
            graph.add(vertex);
        } else {
            vertex = graph.get(graph.indexOf(vertex));
        }
        return vertex;
    }

    private void processMessageNodesList(MessagingNodesList messagingNodesList) {
        for (Node node : messagingNodesList.getNodes()) {
            ConnectionRequest connectionRequest =
                    new ConnectionRequest(this.nodeInfo.getIpAddress(), this.nodeInfo.getPortNumber());
            try {
                logger.log(Level.INFO, "Sending connection request to " + node.getIpAddress() + " port " + node.getPort());
                this.connectionManager.sendMessage(connectionRequest, node.getIpAddress(), node.getPort());
            } catch (MessageProcessingException e) {
                logger.log(Level.SEVERE, "Can not process the message ", e);
            } catch (ConnectionException e) {
                logger.log(Level.SEVERE, "Can not connect to the server ", e);
            }
        }
    }

    public void exitOverlay() {
        DeRegisterRequest deRegisterRequest =
                new DeRegisterRequest(this.nodeInfo.getIpAddress(), this.nodeInfo.getPortNumber());
        try {
            this.connectionManager.sendMessage(
                    deRegisterRequest, this.nodeInfo.getRegistryHost(), this.nodeInfo.getRegistryPort());
        } catch (MessageProcessingException e) {
            logger.log(Level.SEVERE, "Can not process the message ", e);
        } catch (ConnectionException e) {
            logger.log(Level.SEVERE, "Can not send the message ", e);
        }

    }

    public void printShortestPath() {
         this.algorithum.displayShortestPaths();
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
}
