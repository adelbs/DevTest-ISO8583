package com.ca.iso8583.steps;

import java.io.PrintWriter;
import java.util.List;

import org.adelbs.iso8583.clientserver.ISOConnection;
import org.adelbs.iso8583.helper.PayloadMessageConfig;
import org.adelbs.iso8583.protocol.ISOMessage;
import org.w3c.dom.Element;

import com.itko.lisa.test.TestCase;
import com.itko.lisa.test.TestDefException;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.test.TestRunException;
import com.itko.lisa.vse.stateful.BaseRespondStep;
import com.itko.lisa.vse.stateful.model.TransientResponse;
import com.itko.util.XMLUtils;


public class ISO8583ResponderNode extends BaseRespondStep {

	protected String connectionName = "";
	
	@Override
	public void initialize(TestCase arg0, Element element) throws TestDefException {
		for (int i = 0; i < element.getChildNodes().getLength(); i++) {
			if (element.getChildNodes().item(i).getNodeName().equals("ISO8583ConnectionName")) {
				this.connectionName = element.getChildNodes().item(i).getTextContent();
				break;
			}
		}
	}

	public String getConnectionName() {
		return this.connectionName;
	}
	
	@Override
	public void writeSubXML(PrintWriter pw) {
		XMLUtils.streamTagAndChild(pw, "ISO8583ConnectionName", connectionName);
		pw.flush();
	}

	public ISO8583ResponderNode() {
		super();
	}
	
	public ISO8583ResponderNode(String connectionName) {
		this.connectionName = connectionName;
	}

	@Override
	public String getTypeName() throws Exception {
		return "ISO8583 Responder";
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void respond(TestExec testExec) throws Exception {
		try {
			ISOConnection isoConnection = (ISOConnection) testExec.getStateValue(getConnectionName());
			
			Object responseObj = testExec.getStateObject("lisa.vse.response");
			TransientResponse response = (TransientResponse) ((List) responseObj).get(0);
			
			String parsedXML = testExec.parseInState(response.getBodyAsString());
			
		//	final TestExec te = testExec;
			final PayloadMessageConfig payloadMessageConfig = new PayloadMessageConfig(parsedXML);
			ISOMessage isoMessage = new ISOMessage(payloadMessageConfig.getMessageVO());
			
			byte[] data = payloadMessageConfig.getIsoConfig().getDelimiter().preparePayload(isoMessage, payloadMessageConfig.getIsoConfig());
			
		/*	isoConnection.setIsoConfig(payloadMessageConfig.getIsoConfig());
			isoConnection.setCallback(new CallbackAction() {

				@Override
				public void dataReceived(byte[] data) throws ParseException {
					payloadMessageConfig.updateFromPayload(data);
					te.setLastResponse(payloadMessageConfig.getXML());
				}

				@Override
				public void log(String log) { }

				@Override
				public void end() {
					if (te.getLastResponse() == null || te.getLastResponse().equals(""))
						te.setLastResponse("");
				}
				
			});*/
			
			if (!isoConnection.isConnected()) isoConnection.connect();
			isoConnection.sendBytes(data);
			isoConnection.resetSocket(true);
		}
		catch (Exception x) {
			throw new TestRunException(x.getMessage(), x);
		}
	}

	@Override
	protected String getResponsePropertyKey() {
		return "lisa.iso8583.response.list";
	}

}
