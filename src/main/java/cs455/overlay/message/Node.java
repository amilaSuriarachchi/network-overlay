package cs455.overlay.message;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 1/31/14
 * Time: 3:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class Node extends AbstractMessage {

    private String ipAddress;
    private int port;

    public Node() {
    }

    public Node(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public int getMessageType() {
        // this is a helper class that does not have a proper type
        return 0;
    }

    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        try {
            dataOutput.writeUTF(this.ipAddress);
            dataOutput.writeInt(this.port);
        } catch (IOException e) {
            throw new MessageProcessingException("Can not write to the data out put");
        }
    }

    public void parse(DataInput dataInput) throws MessageProcessingException {
        try {
            this.ipAddress = dataInput.readUTF();
            this.port = dataInput.readInt();
        } catch (IOException e) {
            throw new MessageProcessingException("Can not read from the input stream");
        }
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
