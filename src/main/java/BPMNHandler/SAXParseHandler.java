package BPMNHandler;


import XMLStructure.*;
import XMLStructure.Process;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXParseHandler extends DefaultHandler {

    private Definition definition;
    private Collaboration collaboration;
    private Lane lane;
    private Process process;
    private ProcessElem processElem;
    private ProcessFlow processFlow;
    private ProcessRef processRef;
    private String content;


    @Override
    public void startDocument() throws SAXException {
//        super.startDocument();
        definition = new Definition();
        System.out.println("begin");

    }

    @Override
    public void endDocument() throws SAXException {
//        super.endDocument();
        System.out.println("end");

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
//        super.startElement(uri,localName,qName,attributes);
        if (qName.equals("collaboration")) {
            collaboration = new Collaboration();
        }

        if (qName.equals("participant")) {
            processRef = new ProcessRef();
            String id = attributes.getValue("id");
            String name = attributes.getValue("name");
            String ref = attributes.getValue("processRef");
            processRef.setId(id);
            processRef.setName(name);
            processRef.setProcessRef(ref);

            collaboration.addPool(processRef);
        }

        if (qName.endsWith("Flow")) {
            processFlow = new ProcessFlow();
            String id = attributes.getValue("id");
            String sourceRef = attributes.getValue("sourceRef");
            String targetRef = attributes.getValue("targetRef");

            processFlow.setFlowType(qName);
            processFlow.setId(id);
            processFlow.setSourceRef(sourceRef);
            processFlow.setTargetRef(targetRef);

            String name = attributes.getValue("name");
            if (null != name) {
                processFlow.setName(name);
            } else {
                processFlow.setName("");
            }

            if (qName.equals("messageFlow")) {
                collaboration.addMessageFlow(processFlow);
            } else {
                process.addFlow(id, processFlow);
            }

        }

        if (qName.equals("process")) {
            process = new Process();
            String id = attributes.getValue("id");
            String name = attributes.getValue("name");

            process.setId(id);
            process.setName(name);
        }

        if (qName.endsWith("Task") || qName.endsWith("Gateway") || qName.endsWith("Event")) {
            processElem = new ProcessElem();
            String id = attributes.getValue("id");

            String name = attributes.getValue("name");
            if (null != name) {
                processElem.setName(name);
            } else {
                processElem.setName("");
            }

            processElem.setElemType(qName);
            processElem.setId(id);

            process.addElem(id, processElem);
        }

        if (qName.equals("lane")) {
            lane = new Lane();
            String id = attributes.getValue("id");
            String name = attributes.getValue("name");
            lane.setId(id);
            lane.setName(name);

        }


    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
//        super.endElement(uri,localName,qName);

        if (qName.equals("flowNodeRef")) {
            lane.addRef(content);
        }
        if (qName.equals("lane")) {
            process.addLane(lane.getId(), lane);
        }

        if (qName.equals("process")) {
            definition.addProcess(process.getId(), process);
        }

        if (qName.equals("collaboration")) {
            definition.setCollaboration(collaboration);
        }

    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
//        super.characters(ch,start,length);
        content = new String(ch, start, length);

    }

    public Definition getDefinition() {
        return definition;
    }


}
