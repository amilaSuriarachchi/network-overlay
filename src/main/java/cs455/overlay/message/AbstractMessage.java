package cs455.overlay.message;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * this class provides the serialise method implementation to all message classes.
 */
public abstract class AbstractMessage implements Message {

    /**
     * create an data output wrapping the given output stream
     * @param outputStream
     * @throws MessageProcessingException
     */
    public void serialize(OutputStream outputStream) throws MessageProcessingException {
        try {
            DataOutput dataOutput = new DataOutputStream(outputStream);
            serialize(dataOutput);
            outputStream.flush();
        } catch (IOException e) {
            throw new MessageProcessingException("Can not write data to stream ", e);
        }
    }
}
