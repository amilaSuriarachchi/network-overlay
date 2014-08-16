package cs455.overlay.registry;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 1/29/14
 * Time: 9:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class MemberLink {

    private boolean isInitialLink;
    private boolean isRetrived;
    private int weight;
    private MemberNode memberNode;
    private MemberLink backLink;

    public MemberLink(boolean isInitialLink, int weight, MemberNode memberNode) {
        this.isInitialLink = isInitialLink;
        this.weight = weight;
        this.memberNode = memberNode;
    }

    @Override
    public int hashCode() {
        return this.memberNode.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        boolean returnValue = false;
        if (obj instanceof MemberLink){
            MemberLink memberLink = (MemberLink) obj;
            returnValue = this.memberNode.equals(memberLink.getMemberNode());
        }
        return returnValue;
    }

    public boolean isRetrived() {
        return isRetrived;
    }

    public void setRetrived(boolean retrived) {
        isRetrived = retrived;
    }

    public boolean isInitialLink() {
        return isInitialLink;
    }

    public void setInitialLink(boolean initialLink) {
        isInitialLink = initialLink;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public MemberNode getMemberNode() {
        return memberNode;
    }

    public void setMemberNode(MemberNode memberNode) {
        this.memberNode = memberNode;
    }

    public MemberLink getBackLink() {
        return backLink;
    }

    public void setBackLink(MemberLink backLink) {
        this.backLink = backLink;
    }
}
