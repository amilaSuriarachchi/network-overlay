package cs455.overlay.registry;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 1/30/14
 * Time: 2:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestOverlayManager extends TestCase {

    public void testSetup() {

        for (int i = 0; i < 1000; i++) {

            System.out.println("Executing " + i + " Iterator ");
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

        }


    }
}
