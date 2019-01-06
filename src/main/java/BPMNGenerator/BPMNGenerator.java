package BPMNGenerator;


import bpmn.converter.converter.BpmnXMLConverter;
import bpmn.model.*;
import bpmn.model.Process;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BPMNGenerator {



    //创建task
    public UserTask createUserTask(String id, String name, String assignee) {
        UserTask userTask = new UserTask();
        userTask.setName(name);
        userTask.setId(id);
        userTask.setAssignee(assignee);
        return userTask;
    }


    //创建箭头
    public SequenceFlow createSequenceFlow(String from, String to) {
        SequenceFlow flow = new SequenceFlow();
        flow.setSourceRef(from);
        flow.setTargetRef(to);
        return flow;
    }


    public StartEvent createStartEvent(String id) {
        StartEvent startEvent = new StartEvent();
        startEvent.setId(id);
        return startEvent;
    }




    public EndEvent createEndEvent(String id) {
        EndEvent endEvent = new EndEvent();
        endEvent.setId(id);
        return endEvent;
    }

    public static void main(String[] args) {

        BPMNGenerator generator = new BPMNGenerator();

        //创建bpmn模型
        BpmnModel model = new BpmnModel();
        Pool pool_1 = new Pool();
        pool_1.setName("TestModel1");
        pool_1.setId("Pool1");
        pool_1.setProcessRef("process_1");

        Pool pool_2 = new Pool();
        pool_2.setName("TestModel2");
        pool_2.setId("Pool2");
        pool_2.setProcessRef("process_2");

        Process process_1 = new Process();
        model.addProcess(process_1);
        process_1.setId("process_1");

        Lane lane1 = new Lane();
        lane1.setId("lane1");
        lane1.setName("Test_pool1");

        Lane lane2 = new Lane();
        lane2.setId("lane2");
        lane2.setName("Test_pool2");




        List<Lane> lanes_1 = new ArrayList<>();
        List<Lane> lanes_2 = new ArrayList<>();

        lanes_1.add(lane1);
        lanes_2.add(lane2);


        //创建bpmn元素
        process_1.setLanes(lanes_1);
        process_1.addFlowElement(generator.createStartEvent("start1"));
        process_1.addFlowElement(generator.createUserTask("task1", "First task", "fred"));
        process_1.addFlowElement(generator.createUserTask("task2", "Second task", "john"));
        process_1.addFlowElement(generator.createUserTask("task3","Third task","nobody"));
        process_1.addFlowElement(generator.createEndEvent("end1"));


        process_1.addFlowElement(generator.createSequenceFlow("start1", "task1"));
        process_1.addFlowElement(generator.createSequenceFlow("task1", "task2"));
        process_1.addFlowElement(generator.createSequenceFlow("task1", "task3"));
        process_1.addFlowElement(generator.createSequenceFlow("task2", "end1"));
        process_1.addFlowElement(generator.createSequenceFlow("task3", "end1"));


        Process process_2 = new Process();
        model.addProcess(process_2);
        process_2.setId("process_2");

//        Lane lane1 = new Lane();
//        lane1.setId("lane1");
//        lane1.setName("Test_pool1");
//
//        Lane lane2 = new Lane();
//        lane2.setId("lane2");
//        lane2.setName("Test_pool2");

        lane2.setParentProcess(process_2);

        //创建bpmn元素
        process_2.setLanes(lanes_2);
        process_2.addFlowElement(generator.createStartEvent("start2"));
        process_2.addFlowElement(generator.createUserTask("task4", "Fourth task", "fred"));
        process_2.addFlowElement(generator.createUserTask("task5", "Fifth task", "john"));
        process_2.addFlowElement(generator.createUserTask("task6","Sixth task","nobody"));
        process_2.addFlowElement(generator.createEndEvent("end2"));


        process_2.addFlowElement(generator.createSequenceFlow("start2", "task4"));
        process_2.addFlowElement(generator.createSequenceFlow("task4", "task5"));
        process_2.addFlowElement(generator.createSequenceFlow("task4", "task6"));
        process_2.addFlowElement(generator.createSequenceFlow("task5", "end2"));
        process_2.addFlowElement(generator.createSequenceFlow("task6", "end2"));

        List<Pool> pools = new ArrayList<>();

        pools.add(pool_1);
        pools.add(pool_2);

        model.setPools(pools);




//        model.addProcess(process);

        // 2.生成BPMN自动布局
//        new BpmnAutoLayout(model).execute();

        byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(model);

        try {
            FileOutputStream fos = new FileOutputStream(new File("src/main/resources/test.bpmn"));
            fos.write(bpmnBytes);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
