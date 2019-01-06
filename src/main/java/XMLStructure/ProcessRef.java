package XMLStructure;

public class ProcessRef {
    private String id;
    private String name;
    private String processRef;



    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProcessRef() {
        return processRef;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProcessRef(String processRef) {
        this.processRef = processRef;
    }

    public String toString(){
        return this.processRef;
    }
}
