package cs455.overlay.node.dijkstra;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/1/14
 * Time: 5:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class VertexLink {

    private int weight = Integer.MAX_VALUE;
    private Vertex vertex;

    public VertexLink(int weight, Vertex vertex) {
        this.weight = weight;
        this.vertex = vertex;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Vertex getVertex() {
        return vertex;
    }

    public void setVertex(Vertex vertex) {
        this.vertex = vertex;
    }

    @Override
    public String toString() {
        return "NodeLink{" +
                "weight=" + weight +
                ", vertex=" + vertex.getIpAddress() +
                '}';
    }
}
