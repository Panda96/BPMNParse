package BPMNPreprocessor;


import bpmn.converter.converter.BpmnXMLConverter;
import bpmn.converter.exceptions.XMLException;
import bpmn.model.BpmnModel;
import bpmn.model.FlowElement;
import bpmn.model.GraphicInfo;
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

//        run_move();
//        count();
        run_normalization();
    }

    public static void run_normalization() {
        String input_dir = "E:/diagrams/bpmn-io/bpmn2image/files_04_22";
        String output_dir = "E:/diagrams/bpmn-io/bpmn2image/files_valid_04_22";
        normalize_bpmn(input_dir, output_dir);

    }

    public static void run_move() {

//        String input_dir = "E:/diagrams/bpmn-io/bpmn2image/files_2019_04_21/";
        String output_dir = "E:/diagrams/bpmn-io/bpmn2image/files_2019_04_21/";
        String input_dir = "E:/diagrams/bpmn-io/bpmn2image/files/";
//        String output_dir = "E:/diagrams/bpmn-io/bpmn2image/files/";
        moveDir(input_dir, output_dir);
    }

    public static void filter() {
        String bpmn_dir = "E:/diagrams/bpmn-io/bpmn2image/files_2019_04_21/";
        String img_dir = "E:/diagrams/bpmn-io/bpmn2image/images_2019_04_21/";
        String filter_dir = "E:/diagrams/bpmn-io/bpmn2image/mapped_files_2019_04_21/";
        File bpmn_Dir = new File(bpmn_dir);
        File[] bpmns = bpmn_Dir.listFiles();

        File img_Dir = new File(img_dir);
        File[] imgs = img_Dir.listFiles();
        int i = 0;
        int j = 0;
        while (i < bpmns.length) {
            File f = bpmns[i];
            String name = f.getName();
            String img_name = name.replace(".bpmn", ".png");

            if (img_name.equals(imgs[j].getName())) {
                j++;
                f.renameTo(new File(filter_dir + name));

            }
            i++;
            if (i % 100 == 0) {
                System.out.println(i);
            }
        }


    }

    public static void count() {
//        String dirname = "E:/diagrams/bpmn-io/bpmn2image/mapped_files_2019_04_21/";
        String dirname = "E:/diagrams/bpmn-io/bpmn2image/files/";
//        String dirname = "src/main/resources/bpmn/admission";
        File dir = new File(dirname);
        File[] bpmns = {};
        if (dir.isDirectory()) {
            bpmns = dir.listFiles();
        }

        try {
            FileWriter eleWriter = new FileWriter("src/main/java/elements.txt");
            FileWriter flowWriter = new FileWriter("src/main/java/flows.txt");
            for (int i = 0; i < bpmns.length; i++) {
                File f = bpmns[i];
                String filename = f.getAbsolutePath();
                try {
                    BpmnModel bpmnModel = importModel(filename);
                    count_flow_elements(bpmnModel, f.getName(), eleWriter);
                    count_flows(bpmnModel, f.getName(), flowWriter);
                } catch (XMLException xe) {
                    System.out.println(filename);
                    continue;
                }


                if (i % 100 == 0) {
                    System.out.println(i);
                }
            }

            eleWriter.close();
            flowWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void count_flow_elements(BpmnModel bpmnModel, String fileName, FileWriter fw) {
        Map<String, GraphicInfo> map = bpmnModel.getLocationMap();

        for (Map.Entry<String, GraphicInfo> entry : map.entrySet()) {

            FlowElement flowElement = bpmnModel.getFlowElement(entry.getKey());
            String className = "";
            if (flowElement != null) {
                className = flowElement.getClass().toString();

            } else {
                try {
                    Process process = bpmnModel.getProcess(entry.getKey());
                    className = process.getClass().toString();
                } catch (NullPointerException e) {
                    className = ".EmptyProcess";
                }

            }

            String id = fileName.substring(0, fileName.lastIndexOf("_"));
//            String id = fileName;

            String flowElementType = className.substring(className.lastIndexOf(".") + 1);
            GraphicInfo value = entry.getValue();
            String elementPosition = "[" + value.getX() + "," + value.getY() + "," + value.getWidth() + "," + value.getHeight() + "]";

            try {
                fw.write(id + ";" + flowElementType + ";" + elementPosition + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void count_flows(BpmnModel bpmnModel, String fileName, FileWriter fw) {
        Map<String, List<GraphicInfo>> map = bpmnModel.getFlowLocationMap();
        for (Map.Entry<String, List<GraphicInfo>> entry : map.entrySet()) {
            FlowElement flowElement = bpmnModel.getFlowElement(entry.getKey());
            String className = "";
            if (flowElement != null) {
                className = flowElement.getClass().toString();
            } else {
                try {
                    className = bpmnModel.getMessageFlow(entry.getKey()).getClass().toString();
                } catch (NullPointerException e) {
                    className = ".UnknownFlow";
                }

            }
            String id = fileName.substring(0, fileName.lastIndexOf("_"));
//            String id = fileName;

            String flowType = className.substring(className.lastIndexOf(".") + 1);
            List<GraphicInfo> graphicInfoList = entry.getValue();
            String points = "[ ";
            for (GraphicInfo graphicInfo : graphicInfoList) {
                String point = "(" + graphicInfo.getX() + "," + graphicInfo.getY() + ") ";
                points = points + point;
            }
            points = points + "]";

            try {
                fw.write(id + ";" + flowType + ";" + points + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
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

                String input_file = dir_name + "/" + name;
                BpmnModel bpmnModel = new BpmnModel();
                try {
                    bpmnModel = importModel(input_file);
                } catch (Exception e) {
                    System.out.println(parts[0] + "_" + parts[1] + " import invalid!");
                    continue;
                }
//                name = "";
//                for (int i = 0; i < parts.length - 1; i++) {
//                    name = name + parts[i] + "_";
//                }
//                name += parts[parts.length - 1];

                String output_file = output_dir + "/" + name;
//                System.out.println(output_file);
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

        try {
            BpmnModel bpmnModel = importModel(path);
            bpmnModel = adjustPosition(bpmnModel, 6, 6);
            exportModel(bpmnModel, adjustPath);
        } catch (Exception e) {

            File file = new File("src/main/java/miss.txt");
            try {
                FileWriter fw = new FileWriter(file, true);
                fw.write(path.substring(path.lastIndexOf("/") + 1) + "\n");
                fw.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }


    }


    public static BpmnModel adjustPosition(BpmnModel bpmnModel, double targetX, double targetY) {

        String id = "";
        Map<String, GraphicInfo> locationMap = bpmnModel.getLocationMap();

        double min_x = 10000;
        double min_y = 10000;
        for (Map.Entry<String, GraphicInfo> entry : locationMap.entrySet()) {
            GraphicInfo graphicInfo = entry.getValue();
            if (graphicInfo.getX() < min_x) {
                min_x = graphicInfo.getX();
            }


            if (graphicInfo.getY() < min_y) {
                min_y = graphicInfo.getY();
            }

        }
//        System.out.println(min_x + "," + min_y);

        double offsetX = targetX - min_x;
        double offsetY = targetY - min_y;
//
//        System.out.println(offsetX + "," + offsetY);

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
