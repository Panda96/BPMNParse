package XMLStructure;



//SequenceFlow,MessageFlow
public class ProcessFlow {
    //FlowRef
    private String id;
    private String name;
    private String sourceRef;
    private String targetRef;

    private String flowType;




    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSourceRef() {
        return sourceRef;
    }

    public String getTargetRef() {
        return targetRef;
    }

    public String getFlowType() {
        return flowType;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSourceRef(String sourceRef) {
        this.sourceRef = sourceRef;
    }

    public void setTargetRef(String targetRef) {
        this.targetRef = targetRef;
    }

    public void setFlowType(String flowType) {
        this.flowType = flowType;
    }

    public String toString(){
        return flowType+"\t:("+sourceRef+"\t,\t"+targetRef+") name:"+name;
    }


}
