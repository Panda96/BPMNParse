package BPMNPreprocessor;


import bpmn.converter.converter.BpmnXMLConverter;
import bpmn.model.*;
import bpmn.model.Process;

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
    public static String base = "src/main/resources/bpmn/";
    public static String temp = "temp/";
    public static String admission = "admission/";
    public static String elements = "elements/";
    public static String adjust = "adjust/";

    public static void main(String[] args) {

//        String path = base+elements+"Task no Pool.bpmn";
//
//        String[] allTasks = {"task","businessRuleTask","manualTask","receiveTask","scriptTask","sendTask","serviceTask",
//                "userTask"};
//        String callActivity = "callActivity";
//        String[] allGateways = {"exclusiveGateway","eventBasedGateway","inclusiveGateway","parallelGateway","complexGateway"};

//        BpmnModel model = importModel(path);

//        List<Process> processes = model.getProcesses();
//        Process process = processes.get(0);

//        System.out.println(process.getFlowElements().size());
//        List<FlowElement> flowElements = (List<FlowElement>)process.getFlowElements();
//        FlowElement flowElement =flowElements.get(0);
//
//        if(flowElement instanceof Task){
//            for(String type : allTasks){
//                Task task = TaskCreator.crate(type);
//                task.setValues(flowElement);
//
//                flowElements.clear();
//                flowElements.add(task);

//                model = adjustPosition(model,6,6);
//                exportModel(model, base+adjust+elements+type+".bpmn");
//            }
//        }

        count();
    }

    public static void run_normalization() {
        String input_dir = "E:/diagrams/genMyModel/files";
        String output_dir = "E:/diagrams/genMyModel/adjust_files";
        normalize_bpmn(input_dir, output_dir);
    }

    public static void run_move() {

        String input_dir = base + "files";
        String output_dir = base + adjust + "files";
    }

    public static void count() {
        String dirname = "src/main/resources/multi_bpmn/bpmn/";
        File dir = new File(dirname);
        File[] bpmns = {};
        if (dir.isDirectory()) {
            bpmns = dir.listFiles();
        }
        String filename = bpmns[2].getAbsolutePath();
        System.out.println(filename);
        BpmnModel bpmnModel = importModel(filename);

        count_flow_elements(bpmnModel);
        System.out.println("***********************************************************************");
        count_flows(bpmnModel);
    }

    private static void count_flow_elements(BpmnModel bpmnModel){
        Map<String, GraphicInfo> map = bpmnModel.getLocationMap();
        for (Map.Entry<String, GraphicInfo> entry : map.entrySet()) {

            FlowElement flowElement = bpmnModel.getFlowElement(entry.getKey());
            String className = "";
            if(flowElement!=null){
                className = flowElement.getClass().toString();

            }else {
                try {
                    Process process = bpmnModel.getProcess(entry.getKey());
                    className = process.getClass().toString();
                }catch (NullPointerException e){
                    className=".Empty Process";
                }

            }

            System.out.println(entry.getKey()+" \t"+className.substring(className.lastIndexOf(".")+1));
            GraphicInfo value = entry.getValue();
            System.out.println("["+value.getX()+","+value.getY()+","+value.getWidth()+","+value.getHeight()+"]");
            System.out.println("-----------------------------");
        }
    }

    private static void count_flows(BpmnModel bpmnModel){
        Map<String, List<GraphicInfo>> map = bpmnModel.getFlowLocationMap();
        for(Map.Entry<String, List<GraphicInfo>> entry: map.entrySet()){
            FlowElement flowElement = bpmnModel.getFlowElement(entry.getKey());
            String className = "";
            if(flowElement!=null){
                className = flowElement.getClass().toString();
            }else{
                try{
                    className = bpmnModel.getMessageFlow(entry.getKey()).getClass().toString();
                }catch (NullPointerException e){
                    className = ".Unknown Flow";
                }

            }
            System.out.println(entry.getKey()+" \t"+className.substring(className.lastIndexOf(".")+1));
            List<GraphicInfo> graphicInfoList = entry.getValue();
            for (GraphicInfo graphicInfo : graphicInfoList) {
                System.out.print("("+graphicInfo.getX()+","+graphicInfo.getY()+") ");
            }
            System.out.println("\n-----------------------------");
        }
    }


    /**
     * 给bpmn文件重命名
     *
     * @param dir_name
     * @param output_dir
     */
    public static void normalize_bpmn(String dir_name, String output_dir) {
        File dir = new File(dir_name);
        if (dir.isDirectory()) {
            File[] bpmns = dir.listFiles();
            for (File bpmn : bpmns) {
                String name = bpmn.getName();
                String[] parts = name.split("_");
                parts[0] = compensate(parts[0], 3);
                parts[1] = compensate(parts[1], 2);

                String input_file = dir_name + "/" + name;
//                System.out.println(input_file);
                BpmnModel bpmnModel = new BpmnModel();
                try {
                    bpmnModel = importModel(input_file);
                } catch (Exception e) {
                    System.out.println(parts[0] + "_" + parts[1] + " import invalid!");
                    continue;
                }
                name = "";
                for (int i = 0; i < parts.length - 1; i++) {
                    name = name + parts[i] + "_";
                }
                name += parts[parts.length - 1];

                String output_file = output_dir + "/" + name;
                try {
                    exportModel(bpmnModel, output_file);
                } catch (Exception e) {
                    System.out.println(parts[0] + "_" + parts[1] + " export invalid!");
                    continue;
                }


            }
        }
    }

    private static String compensate(String s, int len) {
        while (s.length() < len) {
            s = '0' + s;
        }
        return s;
    }


    /**
     * 调整一个目录下所有bpmn中所有元素的位置
     *
     * @param sourceDir
     * @param targetDir
     */
    public static void moveDir(String sourceDir, String targetDir) {
        System.out.println(sourceDir);
        System.out.println(targetDir);

        File srcDir = new File(sourceDir);
        File tgtDir = new File(targetDir);
        if (srcDir.isDirectory() && tgtDir.isDirectory()) {
            File[] files = srcDir.listFiles();
            for (File f : files) {
                String fileName = f.getName();
//                System.out.println(fileName);
                if (f.isFile() && fileName.endsWith(".bpmn")) {
                    String path = sourceDir + fileName;
                    String adjustPath = targetDir + fileName;
                    move(path, adjustPath);
                    System.out.println(fileName);
                }
            }
        } else {
            System.out.println("Source dir path or target dir path is not a directory!");
        }

    }

    /**
     * 调整单个bpmn所有元素位置，保持相对位置不变
     *
     * @param path
     * @param adjustPath
     */
    public static void move(String path, String adjustPath) {
        BpmnModel bpmnModel = importModel(path);
        bpmnModel = adjustPosition(bpmnModel, 6, 6);
        exportModel(bpmnModel, adjustPath);
    }


    public static BpmnModel adjustPosition(BpmnModel bpmnModel, double targetX, double targetY) {

        String id = "";
        List<Pool> pools = bpmnModel.getPools();
        if (pools.size() > 0) {
            id = pools.get(0).getId();
        } else {
            List<Process> processes = bpmnModel.getProcesses();
            Process process = processes.get(0);

            List<FlowElement> flowElements = (List<FlowElement>) process.getFlowElements();
            FlowElement flowElement = flowElements.get(0);
            id = flowElement.getId();
        }


        GraphicInfo mainProcessGI = bpmnModel.getGraphicInfo(id);
//
        double x = mainProcessGI.getX();

        double y = mainProcessGI.getY();

        double offsetX = targetX - x;
        double offsetY = targetY - y;


        Map<String, GraphicInfo> locationMap = bpmnModel.getLocationMap();
        adjustShapeGraphicInfo(locationMap, offsetX, offsetY);
        Map<String, GraphicInfo> labelLocationMap = bpmnModel.getLabelLocationMap();
        adjustShapeGraphicInfo(labelLocationMap, offsetX, offsetY);
        Map<String, List<GraphicInfo>> flowLocationMap = bpmnModel.getFlowLocationMap();
        adjustEdgeGraphicInfo(flowLocationMap, offsetX, offsetY);


        return bpmnModel;
    }

    private static void adjustEdgeGraphicInfo(Map<String, List<GraphicInfo>> map, double offsetX, double offsetY) {
        for (Map.Entry<String, List<GraphicInfo>> entry : map.entrySet()) {
            List<GraphicInfo> graphicInfoList = entry.getValue();
            for (GraphicInfo graphicInfo : graphicInfoList) {
                graphicInfo.setX(graphicInfo.getX() + offsetX);
                graphicInfo.setY(graphicInfo.getY() + offsetY);
            }

        }

    }

    private static void adjustShapeGraphicInfo(Map<String, GraphicInfo> map, double offsetX, double offsetY) {
//        String testId = "pool1";
//        GraphicInfo testGI = map.get(testId);
//        System.out.println("pool1:("+testGI.getX()+","+testGI.getY()+")");

        for (Map.Entry<String, GraphicInfo> entry : map.entrySet()) {
            GraphicInfo graphicInfo = entry.getValue();
            graphicInfo.setX(graphicInfo.getX() + offsetX);
            graphicInfo.setY(graphicInfo.getY() + offsetY);
        }

//        System.out.println("pool1:("+testGI.getX()+","+testGI.getY()+")");
    }

    public static BpmnModel importModel(String path) {
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

    public static void exportModel(BpmnModel bpmnModel, String path) {
        byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnModel);

        try {
            String dir = path.substring(0, path.lastIndexOf("/"));
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
