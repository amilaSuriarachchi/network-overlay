package cs455.overlay.message;

import cs455.overlay.util.Constants;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 1/22/14
 * Time: 8:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class RegisterRequest extends AbstractMessage {

    private String ipAddress;
    private int portNumber;

    public RegisterRequest() {
    }

    public RegisterRequest(String ipAddress, int portNumber) {
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
    }

    public int getMessageType() {
        return Constants.REG_REQ_MESSAGE_TYPE;
    }

    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        try {
            dataOutput.writeInt(getMessageType());
            dataOutput.writeUTF(this.ipAddress);
            dataOutput.writeInt(this.portNumber);
        } catch (IOException e) {
            throw new MessageProcessingException("Can not serialise the message ", e);
        }
    }

    public void parse(DataInput dataInput) throws MessageProcessingException {
        try {
            this.ipAddress = dataInput.readUTF();
            this.portNumber = dataInput.readInt();
        } catch (IOException e) {
            throw new MessageProcessingException("Can not read values", e);
        }
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }
}
