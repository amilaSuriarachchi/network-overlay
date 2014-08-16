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
 * Date: 2/4/14
 * Time: 9:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class LinkWeights extends AbstractMessage {

    private List<NodeLink> nodeLinks;

    public LinkWeights() {
        this.nodeLinks = new ArrayList<NodeLink>();
    }

    public int getMessageType() {
        return Constants.LINK_WEIGHTS_MESSAGE_TYPE;
    }

    public void addLink(NodeLink nodeLink){
        this.nodeLinks.add(nodeLink);
    }

    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        try {
            dataOutput.writeInt(getMessageType());
            dataOutput.writeInt(this.nodeLinks.size());
            for (NodeLink nodeLink : this.nodeLinks){
                nodeLink.serialize(dataOutput);
            }
        } catch (IOException e) {
            throw new MessageProcessingException("Can not write to the data out put");
        }
    }

    public void parse(DataInput dataInput) throws MessageProcessingException {
        try {
            int numberOfLinks = dataInput.readInt();
            for (int i = 0 ; i < numberOfLinks; i++){
                NodeLink nodeLink = new NodeLink();
                nodeLink.parse(dataInput);
                this.nodeLinks.add(nodeLink);
            }
        } catch (IOException e) {
            throw new MessageProcessingException("Can not read from the data input");
        }
    }

    public List<NodeLink> getNodeLinks() {
        return nodeLinks;
    }

    public void setNodeLinks(List<NodeLink> nodeLinks) {
        this.nodeLinks = nodeLinks;
    }
}
