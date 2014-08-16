package cs455.overlay.node.dijkstra;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/1/14
 * Time: 5:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class Vertex {

    private String ipAddress;
    private int port;
    private List<VertexLink> vertexLinks;

    private int distance = Integer.MAX_VALUE;
    private Vertex previousNode;

    public Vertex(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.vertexLinks = new ArrayList<VertexLink>();
    }

    @Override
    public boolean equals(Object obj) {
        boolean returnValue = false;
        if (obj instanceof Vertex) {
            Vertex newVertex = (Vertex) obj;
            returnValue = (newVertex.getIpAddress().equals(this.ipAddress) && (newVertex.getPort() == this.getPort()));
        }
        return returnValue;
    }

    @Override
    public int hashCode() {
        return this.ipAddress.hashCode() + this.port;
    }

    @Override
    public String toString() {
        String preNode = "";
        if (this.previousNode != null) {
            preNode = this.previousNode.getIpAddress() + ":" + this.previousNode.getPort();
        }
        return "Vertex{" + ipAddress + ":" + port + ", " + distance + ", pre=" + preNode + "}";

    }

    public void addLink(VertexLink vertexLink) {
        this.vertexLinks.add(vertexLink);
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

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Vertex getPreviousNode() {
        return previousNode;
    }

    public void setPreviousNode(Vertex previousNode) {
        this.previousNode = previousNode;
    }

    public List<VertexLink> getVertexLinks() {
        return vertexLinks;
    }

    public void setVertexLinks(List<VertexLink> vertexLinks) {
        this.vertexLinks = vertexLinks;
    }
}
