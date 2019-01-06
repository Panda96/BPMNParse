package XMLStructure;

import java.util.HashMap;

public class Process {
    private String id;
    private String name;
    private Boolean isExecutable;
    private HashMap<String,Lane> lanes;
    private HashMap<String,ProcessFlow> flows;
    private HashMap<String,ProcessElem> elems;

    public Process() {

        this.isExecutable = true;
        this.lanes = new HashMap<String, Lane>();
        this.flows = new HashMap<String, ProcessFlow>();
        this.elems = new HashMap<String, ProcessElem>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getExecutable() {
        return isExecutable;
    }

    public HashMap<String, Lane> getLanes() {
        return lanes;
    }

    public HashMap<String, ProcessFlow> getFlows() {
        return flows;
    }

    public HashMap<String, ProcessElem> getElems() {
        return elems;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addLane(String laneRef,Lane lane){
        this.lanes.put(laneRef,lane);
    }

    public void addFlow(String flowRef,ProcessFlow flow){
        this.flows.put(flowRef,flow);
    }

    public void addElem(String elemRef,ProcessElem elem){
        this.elems.put(elemRef,elem);
    }

    public String toString(){
        String s= "";

        s = s+id+"\n";
        for(String laneRef : lanes.keySet()){
            Lane lane = lanes.get(laneRef);
            s+= lane.toString()+"\n";
        }

        for(String elemRef:elems.keySet()){
            ProcessElem elem = elems.get(elemRef);
            s+= elem.toString()+"\n";
        }

        for(String flowRef:flows.keySet()){
            ProcessFlow flow = flows.get(flowRef);
            s+= flow.toString()+"\n";
        }

        return s;
    }
}
