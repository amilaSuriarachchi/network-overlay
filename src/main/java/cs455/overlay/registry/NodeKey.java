package cs455.overlay.registry;


public class NodeKey {

    private String ip;
    private int port;

    public NodeKey(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public int hashCode() {
        return this.ip.hashCode() + this.port;
    }

    @Override
    public boolean equals(Object obj) {
        boolean returnValue = false;
        if (obj instanceof NodeKey){
            NodeKey newKey = (NodeKey) obj;
            if (this.ip.equals(newKey.getIp()) && (this.port == newKey.getPort())){
                returnValue = true;
            }
        }
        return  returnValue;
    }

    @Override
    public String toString() {
        return this.ip + ":" + this.port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
