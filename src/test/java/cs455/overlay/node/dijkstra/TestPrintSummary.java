package cs455.overlay.node.dijkstra;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/6/14
 * Time: 6:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestPrintSummary {

    public static void main(String[] args) {
        System.out.println("|---------------------------------------------------------------------------------------------------------------\t|");
        System.out.println("|Node   \t|Number of     \t|Number of         \t|Summation of        \t|Summation of        \t|Number of        \t|");
        System.out.println("|       \t|messages sent \t|messages received \t|sent messages       \t|received messages   \t|messages relayed \t|");
        System.out.println("|---------------------------------------------------------------------------------------------------------------\t|");
        System.out.println("| \t|25000         \t|25440             \t|-340,040,800,604.00 \t|-144,703,367,090.00 \t|40445            \t|");
    }
}
