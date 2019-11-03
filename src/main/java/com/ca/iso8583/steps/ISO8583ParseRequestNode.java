package com.ca.iso8583.steps;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.adelbs.iso8583.exception.OutOfBoundsException;
import org.adelbs.iso8583.exception.ParseException;
import org.adelbs.iso8583.helper.Iso8583Config2;
import org.adelbs.iso8583.helper.Iso8583Parser;
import org.adelbs.iso8583.util.ISOUtils;
import org.adelbs.iso8583.vo.FieldVO;
import org.adelbs.iso8583.vo.MessageVO;
import org.w3c.dom.Element;

import com.itko.lisa.test.TestCase;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.test.TestNode;
import com.itko.lisa.test.TestRunException;
import com.itko.lisa.vse.SharedModelMap;
import com.itko.lisa.vse.stateful.model.Request;
import com.itko.util.ParameterList;
import com.itko.util.XMLUtils;

@SuppressWarnings("deprecation")
public class ISO8583ParseRequestNode extends TestNode {

	private String isoConfigFilePath = "";

	public String getisoConfigFilePath() {
		return isoConfigFilePath;
	}

	@Override
	public String getTypeName() throws Exception {
		return "ISO8583 Parse Request";
	}

	@Override
	protected void execute(TestExec testExec) throws TestRunException {
		try {
			
			//Config ###########################################################
			if (!SharedModelMap.containsKey("isoConfig")) {
				synchronized(this) {
					if (!SharedModelMap.containsKey("isoConfig")) {
						System.out.println("################ CREATE PARSER CONFIG ################");
						SharedModelMap.putObject("isoConfig", new Iso8583Config2(isoConfigFilePath));
					}
				}
			}
			
			Iso8583Config2 isoConfig = (Iso8583Config2) SharedModelMap.getObject("isoConfig");
			byte[] bytes = getNextBytesPayload(testExec, isoConfig);
			testExec.setStateObject("lisa.vse.request", buildRequestFromBytes(testExec, isoConfig, bytes));

			System.out.println("--- Thread("+ Thread.currentThread().getId() +"):ParseRequest ---");
//			for (int i = 0; i < bytes.length; i++)
//				System.out.println("Thread("+ Thread.currentThread().getId() +"):ParseRequest :: data["+ i +"] = "+ ((int) bytes[i]) +";");
			System.out.println("--- Thread("+ Thread.currentThread().getId() +"):ParseRequest :: end ---");
		} 
		catch (ParseException e) {
			e.printStackTrace();
		} 
		catch (OutOfBoundsException e) {
			e.printStackTrace();
		}
	}
	
	private byte[] getNextBytesPayload(TestExec testExec, Iso8583Config2 isoConfig) throws OutOfBoundsException {
		byte[] bytes;
		
		if (testExec.getStateValue("iso8583.nextPayloads") != null)
			bytes = (byte[]) testExec.getStateValue("iso8583.nextPayloads");
		else 
			bytes = (byte[]) testExec.getStateValue("lisa.vse.tcp.current.transaction.raw");
		
		List<Byte> list = new ArrayList<Byte>();
		for (int i = 0; i < bytes.length; i++) list.add(bytes[i]);
		int messageSize = isoConfig.getDelimiter().getMessageSize(list);
		
		if (list.size() > (messageSize + 2))
			testExec.setStateValue("iso8583.nextPayloads", ISOUtils.subArray(bytes, messageSize + 2, list.size()));
		else
			testExec.setStateValue("iso8583.nextPayloads", null);
		
		bytes = ISOUtils.subArray(bytes, 2, bytes.length);
		
		return bytes;
	}
	
	private Request buildRequestFromBytes(TestExec testExec, Iso8583Config2 isoConfig, byte[] bytes) throws ParseException, OutOfBoundsException {
		Request request = new Request();
		Iso8583Parser isoParser = new Iso8583Parser(isoConfig);
		
		String parsedXML = isoParser.parseBytesToXml(bytes);

		MessageVO messageVO = isoParser.parseXmlToMessageVO(parsedXML);
		ParameterList al = new ParameterList();
		
		request.setOperation(messageVO.getType());

		for (FieldVO fieldVO : messageVO.getFieldList()) {
			al.put(fieldVO.getBitNum().toString(), fieldVO.getValue());
		}
		
		request.setArguments(al);
		request.setBody(parsedXML);
		
		testExec.setLastResponse(parsedXML);
		
		return request;
	}

	@Override
	public void initialize(TestCase testCase, Element element) {
		for (int i = 0; i < element.getChildNodes().getLength(); i++) {
			if (element.getChildNodes().item(i).getNodeName().equals("ISO8583ConfigFilePath"))
				this.isoConfigFilePath = element.getChildNodes().item(i).getTextContent();
		}
	}

	@Override
	public void writeSubXML(PrintWriter pw) {
		XMLUtils.streamTagAndChild(pw, "ISO8583ConfigFilePath", isoConfigFilePath);
		pw.flush();
	}

}
