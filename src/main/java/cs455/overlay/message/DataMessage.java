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
 * Date: 2/5/14
 * Time: 12:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataMessage extends AbstractMessage {

    private int message;
    private List<Node> path;


    public DataMessage() {
        this.message = (int)((Math.random() - 0.5) * 4294967295l);
        this.path = new ArrayList<Node>();

    }

    public void addNode(Node node){
        this.path.add(node);
    }

    public boolean isDestination(){
        return (this.path.size() == 0);
    }

    public Node getNodeToSend(){
        return this.path.remove(0);
    }

    public int getMessageType() {
        return Constants.DATA_MESSAGE_MESSAGE_TYPE;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        try {
            dataOutput.writeInt(getMessageType());
            dataOutput.writeInt(this.message);
            dataOutput.writeInt(this.path.size());
            for (Node node : this.path) {
                node.serialize(dataOutput);
            }
        } catch (IOException e) {
            throw new MessageProcessingException("Can not write the data to out put stream ");
        }
    }

    public void parse(DataInput dataInput) throws MessageProcessingException {
        try {
            this.message = dataInput.readInt();
            int nodes = dataInput.readInt();
            for (int i = 0; i < nodes; i++) {
                Node node = new Node();
                node.parse(dataInput);
                this.path.add(node);
            }
        } catch (IOException e) {
            throw new MessageProcessingException("Can not read the message");
        }
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }
}
