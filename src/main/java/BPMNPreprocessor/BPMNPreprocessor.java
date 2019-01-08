package BPMNPreprocessor;



import bpmn.converter.converter.BpmnXMLConverter;
import bpmn.model.*;
import bpmn.model.Process;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



/**
 * Created by PandaLin on 2018/12/14.
 */
public class BPMNPreprocessor {

    public static void main(String[] args) {

        String base = "src/main/resources/";
        String temp ="temp";
        String admission = "admission";
        String elements = "elements";

        String adjust_temp = "adjust/temp";
        String adjust_admission = "adjust/admission";

        String path = base+elements+"Task.bpmn";

        String[] allTasks = {"task","businessRuleTask","manualTask","receiveTask","scriptTask","sendTask","serviceTask",
                "userTask"};
        String callActivity = "callActivity";
        String[] allGateways = {"exclusiveGateway","eventBasedGateway","inclusiveGateway","parallelGateway","complexGateway"};

        BpmnModel model = importModel(path);

        List<Process> processes = model.getProcesses();
        Process process = processes.get(0);

//        System.out.println(process.getFlowElements().size());
        Collection<FlowElement> flowElements = process.getFlowElements();





//        System.out.println(model.getProcesses().size());


//        moveDir(base+temp,base+adjust_temp);


    }

    public static void moveDir(String sourceDir, String targetDir){
        System.out.println(sourceDir);
        System.out.println(targetDir);

        File srcDir = new File(sourceDir);
        File tgtDir = new File(targetDir);
        if(srcDir.isDirectory()&& tgtDir.isDirectory()){
            File[] files = srcDir.listFiles();
            for(File f:files){
                String fileName = f.getName();
//                System.out.println(fileName);
                if(f.isFile()&&fileName.endsWith(".bpmn")){
                    String path = sourceDir+"/"+fileName;
                    String adjustPath = targetDir+"/"+fileName;
                    move(path,adjustPath);
                    System.out.println(fileName);
                }
            }
        }else{
            System.out.println("Source dir path or target dir path is not a directory!");
        }

    }

    public static void move(String path, String adjustPath){
        BpmnModel bpmnModel = importModel(path);
        bpmnModel = adjustPosition(bpmnModel, 6, 6);
        exportModel(bpmnModel, adjustPath);
    }


    public static BpmnModel adjustPosition(BpmnModel bpmnModel, double targetX, double targetY){

//        Process mainProcess = bpmnModel.getMainProcess();
//        System.out.println(mainProcess.getId());
//        System.out.println(mainProcess.getName());
//        System.out.println(mainProcess.get);
        Pool firstPool = bpmnModel.getPools().get(0);
//        System.out.println(firstPool.getProcessRef());
//        System.out.println(firstPool.getName());
//        System.out.println(firstPool.getId());

        GraphicInfo mainProcessGI = bpmnModel.getGraphicInfo(firstPool.getId());
//
        double x = mainProcessGI.getX();

        double y = mainProcessGI.getY();

        double offsetX = targetX-x;
        double offsetY = targetY-y;

//        System.out.println(offsetX+","+offsetY);

//        bpmnModel.get

        Map<String, GraphicInfo> locationMap = bpmnModel.getLocationMap();
        adjustShapeGraphicInfo(locationMap,offsetX,offsetY);
        Map<String, GraphicInfo> labelLocationMap = bpmnModel.getLabelLocationMap();
//        for(Map.Entry<String, GraphicInfo> entry:labelLocationMap.entrySet()){
//            System.out.println(entry.getKey());
//        }
        adjustShapeGraphicInfo(labelLocationMap,offsetX,offsetY);
        Map<String, List<GraphicInfo>> flowLocationMap = bpmnModel.getFlowLocationMap();
        adjustEdgeGraphicInfo(flowLocationMap,offsetX,offsetY);


        return bpmnModel;
    }

    private static void adjustEdgeGraphicInfo(Map<String,List<GraphicInfo>> map, double offsetX, double offsetY){
        for(Map.Entry<String, List<GraphicInfo>> entry:map.entrySet()){
            List<GraphicInfo> graphicInfoList = entry.getValue();
            for(GraphicInfo graphicInfo:graphicInfoList){
                graphicInfo.setX(graphicInfo.getX()+offsetX);
                graphicInfo.setY(graphicInfo.getY()+offsetY);
            }

        }

    }

    private static void adjustShapeGraphicInfo(Map<String,GraphicInfo> map, double offsetX, double offsetY){
//        String testId = "pool1";
//        GraphicInfo testGI = map.get(testId);
//        System.out.println("pool1:("+testGI.getX()+","+testGI.getY()+")");

        for(Map.Entry<String, GraphicInfo> entry:map.entrySet()){
            GraphicInfo graphicInfo = entry.getValue();
            graphicInfo.setX(graphicInfo.getX()+offsetX);
            graphicInfo.setY(graphicInfo.getY()+offsetY);
        }

//        System.out.println("pool1:("+testGI.getX()+","+testGI.getY()+")");
    }

    public static BpmnModel importModel(String path){
        BpmnModel bpmnModel = new BpmnModel();
        BpmnXMLConverter converter = new BpmnXMLConverter();
        try {
            InputStream inputStream = new FileInputStream(new File(path));

            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(inputStream);
            bpmnModel = converter.convertToBpmnModel(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

        return bpmnModel;
    }

    public static void exportModel(BpmnModel bpmnModel, String path){
        byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnModel);

        try {
            String dir = path.substring(0,path.lastIndexOf("/"));
            File tgtDir = new File(dir);
            tgtDir.mkdirs();
            FileOutputStream fos = new FileOutputStream(new File(path));
            fos.write(bpmnBytes);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
