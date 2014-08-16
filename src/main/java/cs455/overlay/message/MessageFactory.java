package cs455.overlay.message;

import cs455.overlay.util.Constants;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Message factory to create the messages. Message lister class calls this class to create
 * the messages.
 */
public class MessageFactory {

    public static Message getMessage(InputStream inputStream) throws MessageProcessingException{
        DataInput dataInput = new DataInputStream(inputStream);
        Message message = null;
        try {
            int messageType = dataInput.readInt();
            switch (messageType) {
                case Constants.REG_REQ_MESSAGE_TYPE: {
                    message = new RegisterRequest();
                    break;
                }
                case Constants.REG_RES_MESSAGE_TYPE: {
                    message = new RegisterResponse();
                    break;
                }
                case Constants.DEREG_REQ_MESSAGE_TYPE: {
                    message = new DeRegisterRequest();
                    break;
                }
                case Constants.DEREG_RES_MESSAGE_TYPE : {
                    message = new DeRegisterResponse();
                    break;
                }
                case Constants.MESSAGING_NODES_LIST_MESSAGE_TYPE : {
                    message = new MessagingNodesList();
                    break;
                }
                case Constants.CON_REQ_MESSAGE_TYPE : {
                    message = new ConnectionRequest();
                    break;
                }
                case Constants.CON_RES_MESSAGE_TYPE : {
                    message = new ConnectionResponse();
                    break;
                }
                case Constants.LINK_WEIGHTS_MESSAGE_TYPE : {
                    message = new LinkWeights();
                    break;
                }
                case Constants.TASK_INITIATE_MESSAGE_TYPE: {
                    message = new TaskInitiate();
                    break;
                }
                case Constants.DATA_MESSAGE_MESSAGE_TYPE : {
                    message = new DataMessage();
                    break;
                }
                case Constants.TASK_COMPLETE_MESSAGE_TYPE : {
                    message = new TaskComplete();
                    break;
                }
                case Constants.PULL_TRAFFIC_SUMMARY_MESSAGE_TYPE : {
                    message = new PullTrafficSummary();
                    break;
                }
                case Constants.TRAFFIC_SUMMARY_MESSAGE_TYPE : {
                    message = new TrafficSummary();
                    break;
                }

            }
            message.parse(dataInput);
            return message;
        } catch (IOException e) {
            throw new MessageProcessingException("Can not read the message ", e);
        }
    }
}
