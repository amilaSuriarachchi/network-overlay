package cs455.overlay.message;

import cs455.overlay.util.Constants;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/5/14
 * Time: 11:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class TaskInitiate extends AbstractMessage {

    public int getMessageType() {
        return Constants.TASK_INITIATE_MESSAGE_TYPE;
    }

    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        try {
            dataOutput.writeInt(getMessageType());
        } catch (IOException e) {
            throw new MessageProcessingException("Can not read from the data in");
        }
    }

    public void parse(DataInput dataInput) throws MessageProcessingException {

    }
}
