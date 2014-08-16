package cs455.overlay.registry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 1/24/14
 * Time: 10:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class MemberNode {

    private String ipAddress;
    private int portNumber;
    private List<MemberLink> adjecentMembers;

    public MemberNode(String ipAddress, int portNumber) {
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.adjecentMembers = new ArrayList<MemberLink>();
    }

    public boolean isLinkedTo(MemberNode memberNode){
        return this.adjecentMembers.contains(new MemberLink(false, 2, memberNode));
    }

    public void displayLinks(){
         for (MemberLink memberLink : this.adjecentMembers){
             System.out.println("(" + this.ipAddress + "," + memberLink.getMemberNode().getIpAddress() + ")");
         }
    }

    @Override
    public int hashCode() {
        return this.ipAddress.hashCode() + this.portNumber;
    }

    @Override
    public boolean equals(Object obj) {
        boolean returnValue = false;
        if (obj instanceof MemberNode){
            MemberNode newNode = (MemberNode)obj;
            returnValue = this.ipAddress.equals(newNode.getIpAddress())
                        && (this.getPortNumber() == newNode.getPortNumber());
        }
        return returnValue;
    }

    public int getNumberOfNodes(){
        return  this.adjecentMembers.size();
    }

    public void addLink(MemberLink memberLink){
        this.adjecentMembers.add(memberLink);
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

    public List<MemberLink> getAdjecentMembers() {
        return adjecentMembers;
    }

    public void setAdjecentMembers(List<MemberLink> adjecentMembers) {
        this.adjecentMembers = adjecentMembers;
    }
}
