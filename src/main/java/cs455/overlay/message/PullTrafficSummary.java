package cs455.overlay.message;

import cs455.overlay.util.Constants;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/5/14
 * Time: 5:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class PullTrafficSummary extends AbstractMessage {

    public int getMessageType() {
        return Constants.PULL_TRAFFIC_SUMMARY_MESSAGE_TYPE;
    }

    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        try {
            dataOutput.writeInt(getMessageType());
        } catch (IOException e) {
            throw new MessageProcessingException("Can not write to data out");
        }
    }

    public void parse(DataInput dataInput) throws MessageProcessingException {

    }
}
