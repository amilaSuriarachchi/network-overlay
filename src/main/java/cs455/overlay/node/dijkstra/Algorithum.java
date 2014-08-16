package cs455.overlay.node.dijkstra;

import java.util.ArrayList;
import java.util.List;

/**
 * this class implements the dijkstras's shortes path algorithum to calculate the shorts paths.
 */
public class Algorithum {

    // list to keep the initial nodes
    private List<Vertex> initialGraph;
    // source vertex
    private Vertex source;
    // vertex list after applying the algorithm
    private List<Vertex> finalGraph;

    public Algorithum(List<Vertex> initialGraph, Vertex source) {
        this.initialGraph = initialGraph;
        this.source = source;
        this.finalGraph = new ArrayList<Vertex>();
    }

    public void calculateDistace() {

        //set the source vertex distance to zero.
        source.setDistance(0);

        while (!this.initialGraph.isEmpty()) {

            Vertex minVertext = getShortestDistanceNode();
            for (VertexLink vertexLink : minVertext.getVertexLinks()) {
                int altDistance = minVertext.getDistance() + vertexLink.getWeight();
                if ((altDistance < vertexLink.getVertex().getDistance()) && (!this.finalGraph.contains(vertexLink.getVertex()))) {
                    vertexLink.getVertex().setDistance(altDistance);
                    vertexLink.getVertex().setPreviousNode(minVertext);
                }
            }
            this.finalGraph.add(minVertext);
        }

    }

    private Vertex getShortestDistanceNode() {
        // we assume this is not empty since it is checked in while loop
        int minVertexIndex = 0;
        if (this.initialGraph.size() > 1) {
            for (int i = 1; i < this.initialGraph.size(); i++) {
                if (this.initialGraph.get(i).getDistance() < this.initialGraph.get(minVertexIndex).getDistance()) {
                    minVertexIndex = i;
                }
            }
        }
        return this.initialGraph.remove(minVertexIndex);
    }

    public void displayNodes() {
        for (Vertex vertex : this.finalGraph) {
            System.out.println(vertex.toString());
            for (VertexLink vertexLink : vertex.getVertexLinks()) {
                System.out.println(vertexLink);
            }
        }
    }

    public void displayShortestPaths() {
        for (Vertex vertex : this.finalGraph) {
            List<Vertex> shortestPath = shortestPath(vertex);
            System.out.println(shortestPath.toString());

        }
    }

    public List<Vertex> shortestPath(Vertex vertex) {
        List<Vertex> tmp = new ArrayList<Vertex>();
        tmp.add(vertex);
        while (!vertex.equals(this.source)) {
            vertex = vertex.getPreviousNode();
            tmp.add(vertex);
        }

        List<Vertex> shortestPath = new ArrayList<Vertex>();
        for (int i = tmp.size() - 1; i > -1; i--) {
            shortestPath.add(tmp.get(i));
        }
        return shortestPath;
    }

    public Vertex getRandomNode() {
        return this.finalGraph.get(getRandom(1, this.finalGraph.size()));
    }

    private static int getRandom(int min, int max) {
        return min + (int) Math.floor((max - min) * Math.random());
    }

}
