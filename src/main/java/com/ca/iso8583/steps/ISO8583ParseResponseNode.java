package com.ca.iso8583.steps;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.adelbs.iso8583.exception.ParseException;
import org.adelbs.iso8583.helper.Iso8583Config2;
import org.adelbs.iso8583.helper.Iso8583Parser;
import org.adelbs.iso8583.util.ISOUtils;
import org.w3c.dom.Element;

import com.itko.lisa.test.TestCase;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.test.TestNode;
import com.itko.lisa.test.TestRunException;
import com.itko.lisa.vse.SharedModelMap;
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

	@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
	@Override
	protected void execute(TestExec testExec) throws TestRunException {
		try {
			
			Iso8583Config2 isoConfig = (Iso8583Config2) SharedModelMap.getObject("isoConfig");
			Iso8583Parser isoParser = new Iso8583Parser(isoConfig);
			
			List responseObj = (List) testExec.getStateObject("lisa.vse.response");
			TransientResponse response = (TransientResponse) responseObj.get(0);
			String parsedXML = testExec.parseInState(response.getBodyAsString());
			System.out.println(testExec.getStateObject("lisa.vse.response").getClass());
			byte[] data = isoParser.parseXmlToBytes(parsedXML);
		
			testExec.setLastResponse(data);
			response.setBodyBytes(data);

			List responsePayloads = new ArrayList();
			if (testExec.getStateValue("iso8583.responsePayloads") != null)
				responsePayloads = (ArrayList) testExec.getStateValue("iso8583.responsePayloads");
			
			responsePayloads.add(response);
			
			if (testExec.getStateValue("iso8583.nextPayloads") == null) {
				testExec.setStateValue("iso8583.responsePayloads", null);
				
				byte[] newData = {};
				TransientResponse savedResponse;
				for (int i = 0; i < responsePayloads.size(); i++) {
					savedResponse = (TransientResponse) responsePayloads.get(i);
					newData = ISOUtils.mergeArray(newData, savedResponse.getBodyBytes());
				}
				
				response.setBodyBytes(newData);
			}
			else {
				testExec.setStateValue("iso8583.responsePayloads", responsePayloads);
			}
			
			System.out.println("--- Thread("+ Thread.currentThread().getId() +"):ParseResponse ---");
//			for (int i = 0; i < data.length; i++)
//				System.out.println("Thread("+ Thread.currentThread().getId() +"):ParseResponse :: data["+ i +"] = "+ ((int) data[i]) +";");
			System.out.println("--- Thread("+ Thread.currentThread().getId() +"):ParseResponse :: end ---");

		} 
		catch (ParseException e) {
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
