package bpmn.model.alfresco;


import bpmn.model.ServiceTask;

public class AlfrescoMailTask extends ServiceTask {

  public AlfrescoMailTask clone() {
    AlfrescoMailTask clone = new AlfrescoMailTask();
    clone.setValues(this);
    return clone;
  }

  public void setValues(AlfrescoMailTask otherElement) {
    super.setValues(otherElement);
  }
}
