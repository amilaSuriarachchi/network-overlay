package cs455.overlay.message;

import cs455.overlay.util.Constants;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 1/29/14
 * Time: 7:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class DeRegisterResponse extends AbstractMessage {

    private String info;

    public DeRegisterResponse() {
    }

    public DeRegisterResponse(String info) {
        this.info = info;
    }

    public int getMessageType() {
        return Constants.DEREG_RES_MESSAGE_TYPE;
    }

    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        try {
            dataOutput.writeInt(getMessageType());
            dataOutput.writeUTF(this.info);
        } catch (IOException e) {
            throw new MessageProcessingException("Can not write to the data out ", e);
        }
    }

    public void parse(DataInput dataInput) throws MessageProcessingException {
        try {
            this.info = dataInput.readUTF();
        } catch (IOException e) {
            throw new MessageProcessingException("Can not read the message from data input ", e);
        }
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
