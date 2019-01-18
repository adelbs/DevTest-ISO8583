package com.ca.iso8583.steps;

import java.io.PrintWriter;

import org.w3c.dom.Element;

import com.itko.lisa.test.TestCase;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.test.TestNode;
import com.itko.lisa.test.TestRunException;
import com.itko.util.XMLUtils;

public class ISO8583ParseToPayloadNode extends TestNode {

	private String stepContent = "";
	private String payloadFrom = "";
	private String payloadTo = "";

	@Override
	public String getTypeName() throws Exception {
		return "ISO8583 Parse to Payload";
	}

	@Override
	protected void execute(TestExec testExec) throws TestRunException {
		
	}

	@Override
	public void initialize(TestCase testCase, Element element) {
		for (int i = 0; i < element.getChildNodes().getLength(); i++) {
			if (element.getChildNodes().item(i).getNodeName().equals("ISO8583ParseToPayloadFrom"))
				this.payloadFrom = element.getChildNodes().item(i).getTextContent();
			else if (element.getChildNodes().item(i).getNodeName().equals("ISO8583ParseToPayloadTo"))
				this.payloadTo = element.getChildNodes().item(i).getTextContent();
			else if (element.getChildNodes().item(i).getNodeName().equals("ISO8583RequestStepContent"))
				this.stepContent = element.getChildNodes().item(i).getTextContent();
		}
	}

	@Override
	public void writeSubXML(PrintWriter pw) {
		XMLUtils.streamTagAndChild(pw, "ISO8583ParseToPayloadFrom", payloadFrom);
		XMLUtils.streamTagAndChild(pw, "ISO8583ParseToPayloadTo", payloadTo);
		XMLUtils.streamTagAndChild(pw, "ISO8583RequestStepContent", stepContent);
		pw.flush();
	}

}
