package com.ca.iso8583.steps;

import java.io.PrintWriter;

import org.adelbs.iso8583.exception.OutOfBoundsException;
import org.adelbs.iso8583.exception.ParseException;
import org.adelbs.iso8583.helper.Iso8583Parser;
import org.adelbs.iso8583.util.ISOUtils;
import org.adelbs.iso8583.vo.FieldVO;
import org.adelbs.iso8583.vo.MessageVO;
import org.w3c.dom.Element;

import com.itko.lisa.test.TestCase;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.test.TestNode;
import com.itko.lisa.test.TestRunException;
import com.itko.lisa.vse.stateful.model.Request;
import com.itko.util.ParameterList;
import com.itko.util.XMLUtils;

public class ISO8583ParseRequestNode extends TestNode {

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
		return "ISO8583 Parse Request";
	}

	@Override
	protected void execute(TestExec testExec) throws TestRunException {
		try {
			byte[] bytes = (byte[]) testExec.getStateValue("lisa.vse.tcp.current.transaction.raw");
			bytes = ISOUtils.subArray(bytes, 2, bytes.length);
			
			Iso8583Parser isoParser = (Iso8583Parser) testExec.getStateObject("isoParser");
			String parsedXML = isoParser.parseBytesToXml(bytes);

			testExec.setLastResponse(parsedXML);
			
			Request request = new Request();
			
			MessageVO messageVO = isoParser.parseXmlToMessageVO(parsedXML);
			ParameterList al = new ParameterList();
			
			request.setOperation(messageVO.getType());

			for (FieldVO fieldVO : messageVO.getFieldList()) {
				al.put(fieldVO.getBitNum().toString(), fieldVO.getValue());
			}
			
			request.setArguments(al);
			request.setBody(parsedXML);
			
			testExec.setStateObject("lisa.vse.request", request);
		} 
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (OutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(TestCase testCase, Element element) {
		for (int i = 0; i < element.getChildNodes().getLength(); i++) {
			if (element.getChildNodes().item(i).getNodeName().equals("ISO8583ParseRequestFrom"))
				this.payloadFrom = element.getChildNodes().item(i).getTextContent();
			else if (element.getChildNodes().item(i).getNodeName().equals("ISO8583ParseRequestTo"))
				this.payloadTo = element.getChildNodes().item(i).getTextContent();
		}
	}

	@Override
	public void writeSubXML(PrintWriter pw) {
		XMLUtils.streamTagAndChild(pw, "ISO8583ParseRequestFrom", payloadFrom);
		XMLUtils.streamTagAndChild(pw, "ISO8583ParseRequestTo", payloadTo);
		pw.flush();
	}

}
