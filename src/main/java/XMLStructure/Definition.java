package XMLStructure;

import java.util.HashMap;

public class Definition {
    private Collaboration collaboration;
    private HashMap<String,Process> processes;
//    private Model model;
    private HashMap<String,Integer> map;



    public Definition(){
        this.processes = new HashMap();
    }

    public Collaboration getCollaboration() {
        return collaboration;
    }

    public void setCollaboration(Collaboration collaboration) {
        this.collaboration = collaboration;
    }

    public HashMap<String, Process> getProcesses() {
        return processes;
    }

    public void addProcess(String processRef,Process process){
        this.processes.put(processRef,process);
    }

    public String toString(){
        String s = "";
        for(String proref:processes.keySet()){
            Process process = processes.get(proref);
            s+= process.toString()+"\n";
        }
        return collaboration.toString()+"\n"+s;
    }


//    public Model transform2Model(){
//        model = new Model();
//        map = new HashMap<>();
//
//        int poolId = 0;
//        int roleId = 0;
//        int elemId = 0;
//        int flowId = 0;
//
//        for(ProcessRef processRef : collaboration.getPools()){
//
//            poolId++;
//            Pool pool = new Pool();
//            pool.setId(poolId);
//            pool.setName(processRef.getName());
//            pool.setRef(processRef.getId());
//
//            try {
//                model.addPool(poolId,pool);
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//
//            Process process = processes.get(processRef.getProcessRef());
//
//            for(Lane lane:process.getLanes().values()){
//                roleId++;
//                Role role = new Role();
//                role.setId(roleId);
//                role.setName(lane.getName());
//                role.setRef(lane.getId());
//                role.setPoolId(poolId);
//
//                try {
//                    pool.addRole(roleId,role);
//                } catch (Exception e) {
//                    System.out.println(e.getMessage());
//                }
//
//                for(String eleref:lane.getNodeRef()){
//                    elemId++;
////                    System.out.println(eleref);
//                    ProcessElem elem = process.getElems().get(eleref);
//
////                    System.out.println(elem.getName());
//
//                    String type = elem.getElemType();
//
//
//
//                    try {
//                        Element element = new Element();
//
//                        if(type.endsWith("Task")) {
//                            element.setType(ElementType.TASK);
//
//                            pool.addTask(elemId,TaskType.TYPE_MAP.get(type));
//
//                        }else if(type.endsWith("Event")){
//                            element.setType(ElementType.EVENT);
//                            pool.addEvent(elemId,EventType.TYPE_MAP.get(type));
//
//                        }else{
//                            element.setType(ElementType.GATEWAY);
//                            pool.addGateway(elemId,GatewayType.TYPE_MAP.get(type));
//                        }
//
//                        element.setId(elemId);
//                        element.setLabel(elem.getName());
//                        element.setRef(elem.getId());
//                        element.setRoleId(roleId);
//
//                        pool.addElement(elemId,element);
//                        map.put(elem.getId(),elemId);
//                    } catch (Exception e) {
//                        System.out.println(e.getMessage());
//                    }
//
//                }
//
//            }
//
//            for(ProcessFlow f: process.getFlows().values()){
//                flowId++;
//                Flow flow = transformFlow(f,flowId,poolId);
//                try {
//                    pool.addFlow(flowId,flow);
//                } catch (Exception e) {
//                    System.out.println(e.getMessage());
//                    e.printStackTrace();
//
//                }
//            }
//
//        }
//
//
//
//        for(ProcessFlow f: collaboration.getMessageFlows()){
//            flowId++;
//            Flow flow = transformFlow(f,flowId,-1);
//            try {
//                model.addMessageFlow(flowId,flow);
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//                e.printStackTrace();
//            }
//
//        }
//
//        Pool.elemNum = elemId;
//        Pool.flowNum = flowId;
//
//        return model;
//    }
//
//    private Flow transformFlow(ProcessFlow f,int flowId,int poolId){
//        Pool pool = model.getPool(poolId);
//        int sourceId = map.get(f.getSourceRef());
//        int targetId = map.get(f.getTargetRef());
//        if(flowId!=-1){
//            if(pool!=null){
//                Element s = pool.getElement(sourceId);
//                Element t = pool.getElement(targetId);
//                s.addSourceArc(flowId);
//                t.addTargetArc(flowId);
//
//            }
//
//        }
//
//        return f.transform2Flow(flowId,sourceId,targetId,poolId);
//
//
//
//    }







}
