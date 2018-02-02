package com.ca.iso8583.steps;

import java.io.IOException;
import java.io.PrintWriter;

import org.w3c.dom.Element;

import com.ca.iso8583.clientserver.CallbackAction;
import com.ca.iso8583.clientserver.ISOConnection;
import com.ca.iso8583.exception.ParseException;
import com.ca.iso8583.helper.PayloadMessageConfig;
import com.ca.iso8583.protocol.ISOMessage;
import com.ca.iso8583.vo.ConnectionInfoVO;
import com.ca.iso8583.vo.FieldVO;
import com.ca.iso8583.vo.MessageVO;
import com.itko.lisa.test.TestCase;
import com.itko.lisa.test.TestDefException;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.test.TestNode;
import com.itko.lisa.test.TestRunException;
import com.itko.lisa.vse.stateful.model.Request;
import com.itko.util.ParameterList;
import com.itko.util.XMLUtils;


@SuppressWarnings("deprecation")
public class ISO8583ListenerNode extends TestNode implements GenericCreateConnectionNodeInterface {

	private String keepalive = "";
	private String stepContent = "";
	private String connectionInfo = "";
	private String connectionName = "";

	@Override
	public String getTypeName() throws Exception {
		return "ISO8583 Listener";
	}

	@Override
	protected void execute(final TestExec testExec) throws TestRunException {
		try {
			ISOConnection isoConnection = null;
			String parsedXML = testExec.parseInState(stepContent);
			
			final TestExec te = testExec;
			final PayloadMessageConfig payloadMessageConfig = new PayloadMessageConfig(parsedXML);
			ISOMessage isoMessage = new ISOMessage(payloadMessageConfig.getMessageVO());
			
			if (!connectionInfo.equals("") && isoConnection == null) {
				ConnectionInfoVO connInfo = new ConnectionInfoVO(connectionInfo);
				isoConnection = new ISOConnection(connInfo.isServer(), connInfo.getHost(), connInfo.getPort(), connInfo.getTimeout());
				testExec.setStateObject(connInfo.getName(), isoConnection);
			}
			
			if (isoConnection == null) isoConnection = (ISOConnection) testExec.getStateValue(getConnectionName());
			
			final byte[] data = payloadMessageConfig.getIsoConfig().getDelimiter().preparePayload(isoMessage, payloadMessageConfig.getIsoConfig());
			final ISOConnection isoKeepaliveConnection = isoConnection;
			
			isoConnection.setIsoConfig(payloadMessageConfig.getIsoConfig());
			isoConnection.setCallback(new CallbackAction() {

				@Override
				public void dataReceived(byte[] data) throws ParseException {
					payloadMessageConfig.updateFromPayload(data);
					te.setLastResponse(payloadMessageConfig.getXML());
					
					Request request = new Request();
					MessageVO messageVO = payloadMessageConfig.getMessageVOFromXML(payloadMessageConfig.getXML());
					ParameterList al = new ParameterList();
					
					request.setOperation(messageVO.getType());

					for (FieldVO fieldVO : messageVO.getFieldList())
						al.put(fieldVO.getBitNum().toString(), fieldVO.getValue());
					
					request.setArguments(al);
					request.setBody(payloadMessageConfig.getXML());
					testExec.setStateObject("lisa.vse.request", request);
				}

				@Override
				public void keepalive() {
					try {
						isoKeepaliveConnection.sendBytes(data, payloadMessageConfig.getISOTestVO().isRequestSync(), payloadMessageConfig.getISOTestVO().isResponseSync());
					} 
					catch (IOException | ParseException | InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				@Override
				public void log(String log) { }

				@Override
				public void end() {
					if (te.getLastResponse() == null || te.getLastResponse().equals(""))
						te.setLastResponse("");
				}
				
			});
			
			if (!isoConnection.isConnected()) isoConnection.connect();
			isoConnection.waitNextRequest(payloadMessageConfig.getISOTestVO().isRequestSync(), Integer.parseInt(keepalive));
		}
		catch (Exception x) {
			throw new TestRunException(x.getMessage(), x);
		}
	}

	@Override
	public void initialize(TestCase testCase, Element node) throws TestDefException {
		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			if (node.getChildNodes().item(i).getNodeName().equals("ISO8583Keepalive"))
				this.keepalive = node.getChildNodes().item(i).getTextContent();
			else if (node.getChildNodes().item(i).getNodeName().equals("ISO8583RequestStepContent"))
				this.stepContent = node.getChildNodes().item(i).getTextContent();
			else if (node.getChildNodes().item(i).getNodeName().equals("ISO8583ConnectionInfo"))
				this.connectionInfo = node.getChildNodes().item(i).getTextContent();
			else if (node.getChildNodes().item(i).getNodeName().equals("ISO8583ConnectionName"))
				this.connectionName = node.getChildNodes().item(i).getTextContent();
		}
	}

	public String getStepContent() {
		return stepContent;
	}

	public String getConnectionName() {
		return this.connectionName;
	}

	@Override
	public String getSavedConnectionInfo() {
		return connectionInfo;
	}

	public String getKeepalive() {
		return keepalive;
	}

	@Override
	public boolean canDeployToVSE() {
		return true;
	}
	
	@Override
	public void writeSubXML(PrintWriter pw) {
		XMLUtils.streamTagAndChild(pw, "ISO8583Keepalive", keepalive);
		XMLUtils.streamTagAndChild(pw, "ISO8583RequestStepContent", stepContent);
		XMLUtils.streamTagAndChild(pw, "ISO8583ConnectionInfo", connectionInfo);
		XMLUtils.streamTagAndChild(pw, "ISO8583ConnectionName", connectionName);
		pw.flush();
	}

}
