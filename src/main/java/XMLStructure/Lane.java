package XMLStructure;

import java.util.ArrayList;

public class Lane {
    private String id;
    private String name;
    private ArrayList<String> NodeRef;

    public Lane() {
        this.NodeRef = new ArrayList<String>();
    }

    public int addRef(String ref){
        this.NodeRef.add(ref);
        return this.NodeRef.size();
    }

    public ArrayList<String> getNodeRef() {
        return NodeRef;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString(){
        return id+":"+name+"\n"+NodeRef.toString();
    }
}
