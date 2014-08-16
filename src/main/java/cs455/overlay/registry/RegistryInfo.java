package cs455.overlay.registry;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 1/22/14
 * Time: 6:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class RegistryInfo {

    private int port;

    public RegistryInfo(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
