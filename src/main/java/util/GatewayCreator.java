package util;

import bpmn.converter.constants.BpmnXMLConstants;
import bpmn.model.*;

/**
 * Created by PandaLin on 2019/1/8.
 */
public class GatewayCreator implements BpmnXMLConstants {
    public static Gateway crate(String type){
        switch(type){
            case ELEMENT_GATEWAY_COMPLEX: return new ComplexGateway();
            case ELEMENT_GATEWAY_EVENT: return new EventGateway();
            case ELEMENT_GATEWAY_EXCLUSIVE: return new ExclusiveGateway();
            case ELEMENT_GATEWAY_INCLUSIVE: return new InclusiveGateway();
            case ELEMENT_GATEWAY_PARALLEL: return new ParallelGateway();
        }
        return null;
    }
}
