package util;


import bpmn.converter.constants.BpmnXMLConstants;
import bpmn.model.*;

/**
 * Created by PandaLin on 2019/1/8.
 */
public class TaskCreator implements BpmnXMLConstants {
    public static Task crate(String type){
        switch (type){
            case ELEMENT_TASK: return new Task();
            case ELEMENT_TASK_BUSINESSRULE: return new BusinessRuleTask();
            case ELEMENT_TASK_MANUAL: return new ManualTask();
            case ELEMENT_TASK_RECEIVE: return new ReceiveTask();
            case ELEMENT_TASK_SCRIPT: return new ScriptTask();
            case ELEMENT_TASK_SEND: return new SendTask();
            case ELEMENT_TASK_SERVICE: return new ServiceTask();
            case ELEMENT_TASK_USER: return new UserTask();
        }
        return null;
    }
}
