package cs455.overlay.message;

import cs455.overlay.util.Constants;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 1/24/14
 * Time: 3:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class RegisterResponse extends AbstractMessage {

    private byte statusCode;
    private String additionalInfo;

    public RegisterResponse() {
    }

    public RegisterResponse(byte statusCode, String additionalInfo) {
        this.statusCode = statusCode;
        this.additionalInfo = additionalInfo;
    }

    public int getMessageType() {
        return Constants.REG_RES_MESSAGE_TYPE;
    }

    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        try {
            dataOutput.writeInt(getMessageType());
            dataOutput.writeByte(this.statusCode);
            dataOutput.writeUTF(this.additionalInfo);
        } catch (IOException e) {
            throw new MessageProcessingException("Can not write data", e);
        }
    }

    public void parse(DataInput dataInput) throws MessageProcessingException {
        try {
            this.statusCode = dataInput.readByte();
            this.additionalInfo = dataInput.readUTF();
        } catch (IOException e) {
            throw new MessageProcessingException("Can not read data from stream", e);
        }
    }

    public byte getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(byte statusCode) {
        this.statusCode = statusCode;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
