package BPMNPreprocessor;



import bpmn.converter.converter.BpmnXMLConverter;
import bpmn.model.BpmnModel;
import bpmn.model.GraphicInfo;
import bpmn.model.Pool;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created by PandaLin on 2018/12/14.
 */
public class BPMNPreprocessor {

    public static void main(String[] args) {
        String temp_dir ="src/main/resources/temp/";
        String admission_dir = "src/main/resources/admission/";
        String hotel = "Hotel.bpmn";
        String Bicycle = "Bicycle.bpmn";
        String FUBerlin = "FUBerlin.bpmn";
        String Tasks = "Tasks.bpmn";
        String FU_Berlin = "FU_Berlin.bpmn";

        String path = temp_dir+Bicycle;

        BpmnModel bpmnModel = importModel(path);


//        Map<String, GraphicInfo> map = bpmnModel.getLocationMap();
//
//        for(Map.Entry<String, GraphicInfo> entry:map.entrySet()){
//            System.out.println(entry.getKey());
//        }


        bpmnModel = adjustPosition(bpmnModel, 6, 6);
//
        String adjust_dir = "src/main/resources/adjust/";
//
        String adjustPath = adjust_dir+Bicycle;
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
            FileOutputStream fos = new FileOutputStream(new File(path));
            fos.write(bpmnBytes);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
