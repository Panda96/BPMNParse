package XMLStructure;

import java.util.ArrayList;

public class Collaboration {
    private ArrayList<ProcessRef> pools;
    private ArrayList<ProcessFlow> messageFlows;

    public Collaboration(){
        this.pools = new ArrayList<ProcessRef>();
        this.messageFlows = new ArrayList<ProcessFlow>();
    }


    public ArrayList<ProcessRef> getPools() {
        return pools;
    }

    public ArrayList<ProcessFlow> getMessageFlows() {
        return messageFlows;
    }

    public void addPool(ProcessRef processRef){
        this.pools.add(processRef);
    }

    public void addMessageFlow(ProcessFlow messageFlow){
        this.messageFlows.add(messageFlow);
    }

    public String toString(){
        return pools.toString()+"\n"+messageFlows.toString();
    }
}
