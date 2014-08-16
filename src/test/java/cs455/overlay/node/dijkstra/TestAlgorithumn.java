package cs455.overlay.node.dijkstra;

import cs455.overlay.registry.MemberLink;
import cs455.overlay.registry.MemberNode;
import cs455.overlay.registry.OverlayManager;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/1/14
 * Time: 7:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestAlgorithumn extends TestCase {

    public void testAlgorithumn() {

        List<MemberNode> memberNodes = getOverlay();
        List<Vertex> graph = getGraphNodes(memberNodes);
        Algorithum algorithum = new Algorithum(graph, graph.get(0));
        algorithum.calculateDistace();
        algorithum.displayNodes();
        algorithum.displayShortestPaths();


    }

    private List<MemberNode> getOverlay() {
        List<MemberNode> memberNodes = new ArrayList<MemberNode>();
        memberNodes.add(new MemberNode("node1", 4000));
        memberNodes.add(new MemberNode("node2", 4000));
        memberNodes.add(new MemberNode("node3", 4000));
        memberNodes.add(new MemberNode("node4", 4000));
        memberNodes.add(new MemberNode("node5", 4000));
        memberNodes.add(new MemberNode("node6", 4000));
        memberNodes.add(new MemberNode("node7", 4000));
        memberNodes.add(new MemberNode("node8", 4000));
        memberNodes.add(new MemberNode("node9", 4000));
        memberNodes.add(new MemberNode("node10", 4000));

        OverlayManager overlayManager = new OverlayManager(memberNodes, 4);
        overlayManager.setUpOverlay();
        return memberNodes;
    }

    private List<Vertex> getGraphNodes(List<MemberNode> memberNodes) {

        List<Vertex> graph = new ArrayList<Vertex>();

        for (MemberNode memberNode : memberNodes) {
            Vertex vertex = getVertex(graph, memberNode.getIpAddress(), memberNode.getPortNumber());
            for (MemberLink memberLink : memberNode.getAdjecentMembers()){
                Vertex linkVertex = getVertex(graph, memberLink.getMemberNode().getIpAddress(), memberLink.getMemberNode().getPortNumber());
                vertex.addLink(new VertexLink(memberLink.getWeight(), linkVertex));
            }
        }
        return graph;

    }

    private Vertex getVertex(List<Vertex> graph, String ipAddres, int port) {
        Vertex vertex = new Vertex(ipAddres, port);
        if (!graph.contains(vertex)) {
            graph.add(vertex);
        } else {
            vertex = graph.get(graph.indexOf(vertex));
        }
        return vertex;
    }
}
