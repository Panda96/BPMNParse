package bpmn.converter.converter.child;

import javax.xml.stream.XMLStreamReader;

import bpmn.model.Activity;
import bpmn.model.BaseElement;
import bpmn.model.BpmnModel;

public class ActivitiFailedjobRetryParser extends BaseChildElementParser {

  @Override
  public String getElementName() {
    return FAILED_JOB_RETRY_TIME_CYCLE;
  }

  @Override
  public void parseChildElement(XMLStreamReader xtr, BaseElement parentElement, BpmnModel model) throws Exception {
    if (!(parentElement instanceof Activity))
      return;
    String cycle = xtr.getElementText();
    if (cycle == null || cycle.isEmpty()) {
      return;
    }
    ((Activity) parentElement).setFailedJobRetryTimeCycleValue(cycle);
  }

}
