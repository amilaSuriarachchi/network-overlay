package cs455.overlay.connection;

/**
 * wrapper class to put ip and port for remote clients.
 */
public class RemoteSocket {

    private String ipAddress;
    private int port;

    public RemoteSocket(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override
    public int hashCode() {
        return this.ipAddress.hashCode() + this.port;
    }

    @Override
    public boolean equals(Object obj) {
        boolean returnValue = false;
        if (obj instanceof RemoteSocket){
            RemoteSocket remoteSocket = (RemoteSocket) obj;
            returnValue = (remoteSocket.getIpAddress().equals(this.ipAddress) && (this.port == remoteSocket.getPort()));
        }
        return returnValue;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
