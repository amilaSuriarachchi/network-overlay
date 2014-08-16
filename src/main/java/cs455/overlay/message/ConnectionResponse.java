package cs455.overlay.message;

import cs455.overlay.util.Constants;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/1/14
 * Time: 12:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConnectionResponse extends AbstractMessage {

    private String ipAddress;
    private int port;

    public ConnectionResponse() {
    }

    public ConnectionResponse(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public int getMessageType() {
        return Constants.CON_RES_MESSAGE_TYPE;
    }

    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        try {
            dataOutput.writeInt(getMessageType());
            dataOutput.writeUTF(this.ipAddress);
            dataOutput.writeInt(this.port);
        } catch (IOException e) {
            throw new MessageProcessingException("Can not write to the out put stream");
        }
    }

    public void parse(DataInput dataInput) throws MessageProcessingException {
        try {
            this.ipAddress = dataInput.readUTF();
            this.port = dataInput.readInt();
        } catch (IOException e) {
            throw new MessageProcessingException("Can not read the message from the input stream");
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
