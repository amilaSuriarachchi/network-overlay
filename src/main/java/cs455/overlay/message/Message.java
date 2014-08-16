package cs455.overlay.message;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.OutputStream;

/**
 * Interface to represent messages.
 */
public interface Message {
    /**
     *
     * @return  - type of the message.
     */
    public int getMessageType();

    /**
     * serialise the message data to a data Output.
     * @param dataOutput
     * @throws MessageProcessingException
     */
    public void serialize(DataOutput dataOutput) throws MessageProcessingException;

    /**
     * read the message from the data Input
     * @param dataInput
     * @throws MessageProcessingException
     */
    public void parse(DataInput dataInput) throws MessageProcessingException;

    /**
     * serialise message to output stream.
     * @param outputStream
     * @throws MessageProcessingException
     */
    public void serialize(OutputStream outputStream) throws MessageProcessingException;

}
