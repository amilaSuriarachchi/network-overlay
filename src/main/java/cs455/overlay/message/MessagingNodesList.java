package cs455.overlay.message;

import cs455.overlay.util.Constants;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 1/31/14
 * Time: 3:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessagingNodesList extends AbstractMessage {

    private List<Node> nodes;

    public MessagingNodesList() {
        this.nodes = new ArrayList<Node>();
    }

    public void addNode(Node node){
        this.nodes.add(node);
    }

    public int getMessageType() {
        return Constants.MESSAGING_NODES_LIST_MESSAGE_TYPE;
    }

    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        try {
            dataOutput.writeInt(getMessageType());
            dataOutput.writeInt(this.nodes.size());
            for (Node node : this.nodes) {
                node.serialize(dataOutput);
            }
        } catch (IOException e) {
            throw new MessageProcessingException("Can not write to out put ");
        }
    }

    public void parse(DataInput dataInput) throws MessageProcessingException {
        try {
            int numberOfNodes = dataInput.readInt();
            for (int i = 0; i < numberOfNodes; i++) {
                Node node = new Node(dataInput.readUTF(),dataInput.readInt());
                this.nodes.add(node);
            }
        } catch (IOException e) {
            throw new MessageProcessingException("Can not read from input stream");
        }
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }
}
