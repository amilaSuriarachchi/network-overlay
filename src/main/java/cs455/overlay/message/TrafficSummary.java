package cs455.overlay.message;

import cs455.overlay.util.Constants;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/5/14
 * Time: 6:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrafficSummary extends AbstractMessage {

    private String ipAddress;
    private int portNumber;
    private int sendTracker;
    private long sendSummation;
    private int receiveTracker;
    private long receiveSummation;
    private int relayTracker;

    public TrafficSummary() {
    }

    public TrafficSummary(String ipAddress, int portNumber) {
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
    }

    public int getMessageType() {
        return Constants.TRAFFIC_SUMMARY_MESSAGE_TYPE;
    }

    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        try {
            dataOutput.writeInt(getMessageType());
            dataOutput.writeUTF(this.ipAddress);
            dataOutput.writeInt(this.portNumber);
            dataOutput.writeInt(this.sendTracker);
            dataOutput.writeLong(this.sendSummation);
            dataOutput.writeInt(this.receiveTracker);
            dataOutput.writeLong(this.receiveSummation);
            dataOutput.writeInt(this.relayTracker);
        } catch (IOException e) {
            throw new MessageProcessingException("Can not process the message ", e);
        }
    }

    public void parse(DataInput dataInput) throws MessageProcessingException {
        try {
            this.ipAddress = dataInput.readUTF();
            this.portNumber = dataInput.readInt();
            this.sendTracker = dataInput.readInt();
            this.sendSummation = dataInput.readLong();
            this.receiveTracker = dataInput.readInt();
            this.receiveSummation = dataInput.readLong();
            this.relayTracker = dataInput.readInt();
        } catch (IOException e) {
            throw new MessageProcessingException("Can not read the message ", e);
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

    public int getSendTracker() {
        return sendTracker;
    }

    public void setSendTracker(int sendTracker) {
        this.sendTracker = sendTracker;
    }

    public long getSendSummation() {
        return sendSummation;
    }

    public void setSendSummation(long sendSummation) {
        this.sendSummation = sendSummation;
    }

    public int getReceiveTracker() {
        return receiveTracker;
    }

    public void setReceiveTracker(int receiveTracker) {
        this.receiveTracker = receiveTracker;
    }

    public long getReceiveSummation() {
        return receiveSummation;
    }

    public void setReceiveSummation(long receiveSummation) {
        this.receiveSummation = receiveSummation;
    }

    public int getRelayTracker() {
        return relayTracker;
    }

    public void setRelayTracker(int relayTracker) {
        this.relayTracker = relayTracker;
    }
}
