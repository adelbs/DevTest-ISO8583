package com.ca.iso8583.steps;

import java.io.PrintWriter;
import java.util.List;

import org.adelbs.iso8583.exception.ParseException;
import org.adelbs.iso8583.helper.Iso8583Parser;
import org.w3c.dom.Element;

import com.itko.lisa.test.TestCase;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.test.TestNode;
import com.itko.lisa.test.TestRunException;
import com.itko.lisa.vse.stateful.model.TransientResponse;
import com.itko.util.XMLUtils;

public class ISO8583ParseResponseNode extends TestNode {

	private String payloadFrom = "";
	private String payloadTo = "";

	public String getPayloadFrom() {
		return payloadFrom;
	}

	public String getPayloadTo() {
		return payloadTo;
	}

	@Override
	public String getTypeName() throws Exception {
		return "ISO8583 Parse Response";
	}

	@Override
	protected void execute(TestExec testExec) throws TestRunException {
		try {
			Iso8583Parser isoParser = (Iso8583Parser) testExec.getStateObject("isoParser");
			
			Object responseObj = testExec.getStateObject("lisa.vse.response");
			TransientResponse response = (TransientResponse) ((List) responseObj).get(0);
			String parsedXML = testExec.parseInState(response.getBodyAsString());
			
			byte[] data = isoParser.parseXmlToBytes(parsedXML);
			
			testExec.setLastResponse(data);
			response.setBodyBytes(data);
		} 
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void initialize(TestCase testCase, Element element) {
		for (int i = 0; i < element.getChildNodes().getLength(); i++) {
			if (element.getChildNodes().item(i).getNodeName().equals("ISO8583ParseResponseFrom"))
				this.payloadFrom = element.getChildNodes().item(i).getTextContent();
			else if (element.getChildNodes().item(i).getNodeName().equals("ISO8583ParseResponseTo"))
				this.payloadTo = element.getChildNodes().item(i).getTextContent();
		}
	}

	@Override
	public void writeSubXML(PrintWriter pw) {
		XMLUtils.streamTagAndChild(pw, "ISO8583ParseResponseFrom", payloadFrom);
		XMLUtils.streamTagAndChild(pw, "ISO8583ParseResponseTo", payloadTo);
		pw.flush();
	}

}
