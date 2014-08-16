package cs455.overlay.node;

/**
 * class to keep the message node info
 */
public class NodeInfo {

    private int portNumber;
    private String ipAddress;
    private String registryHost;
    private int registryPort;


    public NodeInfo(String registryHost, int registryPort) {
        this.registryHost = registryHost;
        this.registryPort = registryPort;
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

    public String getRegistryHost() {
        return registryHost;
    }

    public void setRegistryHost(String registryHost) {
        this.registryHost = registryHost;
    }

    public int getRegistryPort() {
        return registryPort;
    }

    public void setRegistryPort(int registryPort) {
        this.registryPort = registryPort;
    }
}
