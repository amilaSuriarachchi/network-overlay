package cs455.overlay.util;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 1/22/14
 * Time: 7:54 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Constants {

    int REG_REQ_MESSAGE_TYPE = 1;
    int REG_RES_MESSAGE_TYPE = 2;
    int DEREG_REQ_MESSAGE_TYPE = 3;
    int DEREG_RES_MESSAGE_TYPE = 4;
    int MESSAGING_NODES_LIST_MESSAGE_TYPE = 5;
    int LINK_WEIGHTS_MESSAGE_TYPE = 6;
    int CON_REQ_MESSAGE_TYPE = 7;
    int CON_RES_MESSAGE_TYPE = 8;
    int TASK_INITIATE_MESSAGE_TYPE = 9;
    int DATA_MESSAGE_MESSAGE_TYPE = 10;
    int TASK_COMPLETE_MESSAGE_TYPE = 11;
    int PULL_TRAFFIC_SUMMARY_MESSAGE_TYPE = 12;
    int TRAFFIC_SUMMARY_MESSAGE_TYPE = 13;

    byte STATUS_SUCESS = 1;
    byte STATUS_FAILUER = 2;

    // message node commands
    String PRINT_SHORTEST_PATH_CMD = "print-shortest-path";
    String EXIT_OVERLAY = "exit-overlay";

    //registry commands
    String LIST_MESSAGING_NODES = "list-messaging-nodes";
    String LIST_WEIGHTS = "list-weights";
    String SETUP_OVERLAY = "setup-overlay";
    String SEND_OVERLAY_LINK_WEIGHTS = "send-overlay-link-weights";
    String START = "start";




}
