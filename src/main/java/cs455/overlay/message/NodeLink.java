package cs455.overlay.message;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/4/14
 * Time: 9:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class NodeLink extends AbstractMessage {

    private int weight;
    private Node node1;
    private Node node2;

    public int getMessageType() {
        return 0;
    }

    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        try {
            dataOutput.writeInt(this.weight);
            node1.serialize(dataOutput);
            node2.serialize(dataOutput);
        } catch (IOException e) {
            throw new MessageProcessingException("Can not write to the data out put");
        }
    }

    public void parse(DataInput dataInput) throws MessageProcessingException {
        try {
            this.weight = dataInput.readInt();
            this.node1 = new Node();
            this.node1.parse(dataInput);
            this.node2 = new Node();
            this.node2.parse(dataInput);
        } catch (IOException e) {
            throw new MessageProcessingException("Can not read the message from input");
        }
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Node getNode1() {
        return node1;
    }

    public void setNode1(Node node1) {
        this.node1 = node1;
    }

    public Node getNode2() {
        return node2;
    }

    public void setNode2(Node node2) {
        this.node2 = node2;
    }
}
