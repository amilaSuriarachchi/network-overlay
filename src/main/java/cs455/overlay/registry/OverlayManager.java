package cs455.overlay.registry;

import cs455.overlay.connection.ConnectionException;
import cs455.overlay.connection.ConnectionManager;
import cs455.overlay.message.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * calculates the overlay configuration for network
 */
public class OverlayManager {

    Logger logger = Logger.getLogger(RegistryNode.class.getName());

    private List<MemberNode> memberNodes;
    private int connections;

    public OverlayManager(List<MemberNode> memberNodes, int connections) {
        this.memberNodes = memberNodes;
        this.connections = connections;
    }

    /**
     * this algorithm first connect all nodes to avoid any partitions.
     * then increase one link at a time. When adding links randomly there can be situations where we have to connect
     * two nodes which already has a connection. For those situations we can go back and find a node that is not connect
     * to the other node. Then can create a new connection.
     */
    public void setUpOverlay() {

        //first set the connections between the next node in the list
        for (int i = 0; i < this.memberNodes.size() - 1; i++) {
            createLink(this.memberNodes.get(i), this.memberNodes.get(i + 1), true);
        }
        createLink(this.memberNodes.get(this.memberNodes.size() - 1), this.memberNodes.get(0), true);

        for (int i = 3; i < this.connections + 1; i++) {
             incrementLinkCountTo(i);
        }

    }


    private void incrementLinkCountTo(int links) {
        MemberNode currentNode = null;
        MemberNode linkedNode = null;
        for (int i = 0; i < this.memberNodes.size(); i++) {
            currentNode = this.memberNodes.get(i);
            while (currentNode.getNumberOfNodes() < links) {
                int random = getRandom(i + 1, this.memberNodes.size());
                int returnedRandom = random;
                while (random < this.memberNodes.size()) {
                    linkedNode = this.memberNodes.get(random);
                    if ((linkedNode.getNumberOfNodes() < links) && (!currentNode.isLinkedTo(linkedNode))) {
                        createLink(currentNode, linkedNode, false);
                        break;
                    }
                    random++;
                }
                if ((random == this.memberNodes.size() && (returnedRandom == i + 1))) {
                    // this means this node can not be connect to any free nodes. So we need to go back and edit an existing link to fix that.
                    // first find a node which is not connect to at least on above nodes.
                    boolean isLinkCreated = false;
                    int backCount = i - 1;
                    while (backCount > -1) {
                        MemberNode backNode = this.memberNodes.get(backCount);
                        // go through the next nodes and see whether a none connecting node.
                        int nextNodes = i + 1;
                        while (nextNodes < this.memberNodes.size()) {
                            MemberNode forwadNode = this.memberNodes.get(nextNodes);
                            if (!backNode.isLinkedTo(forwadNode) && (forwadNode.getNumberOfNodes() < 4)) {
                                // we found a candidate node. This node should be link to a node which is not linked to our current node
                                List<MemberLink> adjecentLinks = backNode.getAdjecentMembers();
                                for (int k = 0; k < adjecentLinks.size(); k++) {
                                    MemberLink memberLink = adjecentLinks.get(k);
                                    if (!memberLink.getMemberNode().isLinkedTo(currentNode) && !memberLink.isInitialLink()) {
                                        MemberNode backMember = memberLink.getMemberNode();
                                        MemberLink backLink = memberLink.getBackLink();
                                        backNode.getAdjecentMembers().remove(memberLink);
                                        backMember.getAdjecentMembers().remove(backLink);
                                        createLink(backNode, forwadNode, false);
                                        createLink(backMember, currentNode, false);
                                        isLinkCreated = true;
                                        break;

                                    }
                                }
                            }
                            if (isLinkCreated) {
                                break;
                            } else {
                                nextNodes++;
                            }

                        }
                        if (isLinkCreated) {
                            break;
                        } else {
                            backCount--;
                        }

                    }
                }
            }
        }
    }

    private int getRandom(int min, int max) {
        return min + (int) Math.floor((max - min) * Math.random());
    }

    private void createLink(MemberNode node1, MemberNode node2, boolean isInitial) {
        int weight = (int) Math.ceil(10 * Math.random());
        MemberLink memberLink1 = new MemberLink(isInitial, weight, node2);
        node1.addLink(memberLink1);

        MemberLink memberLink2 = new MemberLink(isInitial, weight, node1);
        node2.addLink(memberLink2);
        memberLink1.setBackLink(memberLink2);
        memberLink2.setBackLink(memberLink1);

    }

    public void displayNetwork() {
        for (MemberNode memberNode : this.memberNodes) {
            memberNode.displayLinks();
        }
    }

    public void sendConnectionMessages(ConnectionManager connectionManager) {
        this.resetRetrived();
        for (MemberNode memberNode : this.memberNodes) {
            MessagingNodesList messagingNodesList = new MessagingNodesList();
            List<MemberLink> adjecentLinks = memberNode.getAdjecentMembers();
            for (MemberLink memberLink : adjecentLinks) {
                if (!memberLink.isRetrived()) {
                    MemberNode linkedNode = memberLink.getMemberNode();
                    messagingNodesList.addNode(new Node(linkedNode.getIpAddress(), linkedNode.getPortNumber()));
                    logger.log(Level.INFO, "Linking the node " + memberNode.getIpAddress() + ":" + memberNode.getPortNumber() +
                                            " with " + linkedNode.getIpAddress() + ":" + linkedNode.getPortNumber());
                    memberLink.setRetrived(true);
                    memberLink.getBackLink().setRetrived(true);
                }
            }
            try {
                logger.log(Level.INFO, "Sending the Message node list message to "
                        + memberNode.getIpAddress() + " port " + memberNode.getPortNumber());
                connectionManager.sendMessage(messagingNodesList,
                        memberNode.getIpAddress(), memberNode.getPortNumber());
            } catch (MessageProcessingException e) {
                logger.log(Level.SEVERE, "Can not send the message");
            } catch (ConnectionException e) {
                logger.log(Level.SEVERE, "Can not connect to the back end server");
            }

        }
    }

    public void displayLinks(){
        this.resetRetrived();
        for (MemberNode memberNode : this.memberNodes){
             for (MemberLink memberLink : memberNode.getAdjecentMembers()){
                 if (!memberLink.isRetrived()){
                     System.out.println(memberNode.getIpAddress() + ":" + memberNode.getPortNumber() + " "
                             + memberLink.getMemberNode().getIpAddress() + ":" + memberLink.getMemberNode().getPortNumber()
                             + " " + memberLink.getWeight());
                     memberLink.setRetrived(true);
                     memberLink.getBackLink().setRetrived(true);
                 }
             }
        }

    }

    private void resetRetrived(){
        for (MemberNode memberNode : this.memberNodes){
            for (MemberLink memberLink : memberNode.getAdjecentMembers()){
                memberLink.setRetrived(false);
            }
        }
    }

    public LinkWeights getLinkWeights(){
        this.resetRetrived();

        LinkWeights linkWeights = new LinkWeights();

        for (MemberNode memberNode : this.memberNodes){
            for (MemberLink memberLink : memberNode.getAdjecentMembers()){
                if (!memberLink.isRetrived()){
                    NodeLink nodeLink = new NodeLink();
                    nodeLink.setNode1(new Node(memberNode.getIpAddress(), memberNode.getPortNumber()));
                    nodeLink.setNode2(new Node(memberLink.getMemberNode().getIpAddress(), memberLink.getMemberNode().getPortNumber()));
                    nodeLink.setWeight(memberLink.getWeight());
                    linkWeights.addLink(nodeLink);
                    memberLink.setRetrived(true);
                    memberLink.getBackLink().setRetrived(true);
                }
            }
        }
        return  linkWeights;
    }


}
