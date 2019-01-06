package XMLStructure;


//Event,Task,Gateway
public class ProcessElem {
    //ElemRef
    private String id;
    private String name;
    private String ElemType;


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getElemType() {
        return ElemType;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setElemType(String elemType) {
        this.ElemType = elemType;
    }

    public String toString(){
        return ElemType +"\t:"+id+"\t"+name;
    }
}
