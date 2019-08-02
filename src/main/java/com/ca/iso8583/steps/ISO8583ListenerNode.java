package com.ca.iso8583.steps;

import java.io.IOException;
import java.io.PrintWriter;

import org.adelbs.iso8583.clientserver.CallbackAction;
import org.adelbs.iso8583.clientserver.ISOConnection;
import org.adelbs.iso8583.clientserver.SocketPayload;
import org.adelbs.iso8583.exception.ParseException;
import org.adelbs.iso8583.helper.PayloadMessageConfig;
import org.adelbs.iso8583.protocol.ISOMessage;
import org.adelbs.iso8583.util.Out;
import org.adelbs.iso8583.vo.FieldVO;
import org.adelbs.iso8583.vo.MessageVO;
import org.w3c.dom.Element;

import com.ca.iso8583.test.TestExecConnectionManager;
import com.ca.iso8583.vo.ConnectionInfoVO;
import com.itko.lisa.test.TestCase;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.test.TestRunException;
import com.itko.lisa.vse.stateful.BaseListenStep;
import com.itko.lisa.vse.stateful.model.Request;
import com.itko.util.ParameterList;
import com.itko.util.XMLUtils;


@SuppressWarnings("deprecation")
public class ISO8583ListenerNode extends BaseListenStep implements GenericCreateConnectionNodeInterface {

	public static final String ISO8583_LISTENER_RECEIVED = "lisa.iso8583.listener.receivedtime";
	public static final String ISO8583_LISTENER_CONNECTIONDROPPED = "lisa.iso8583.listener.connectiondropped";
	
	private String keepalive = "0";
	private String stepContent = "";
	private String connectionInfo = "";
	private String connectionName = "";

	public ISO8583ListenerNode() {
		super();
	}
	
	public ISO8583ListenerNode(String stepContent, String connectionName) {
		this.stepContent = stepContent;
		this.connectionName = connectionName;
	}
	
	@Override
	public String getTypeName() throws Exception {
		return "ISO8583 Listener";
	}

	@Override
	protected void execute(final TestExec testExec) throws TestRunException {
//		TestExecConnectionManager connManager = null;
		try {
//			connManager = new TestExecConnectionManager(testExec, this.getConnectionName());
			if (!connectionInfo.equals("")) {
				ConnectionInfoVO connInfo = new ConnectionInfoVO(connectionInfo);
//				connManager = new TestExecConnectionManager(testExec, connInfo.getName());
//				connManager.connectTo(connInfo);
				
				TestExecConnectionManager.connectTo(connInfo, testExec);
			}		
			
			final ISOConnection isoConnection = TestExecConnectionManager.getCurrentConnection(this.getConnectionName());
			
			String parsedXML = testExec.parseInState(stepContent);
			
			final TestExec te = testExec;
			final PayloadMessageConfig payloadMessageConfig = new PayloadMessageConfig(parsedXML);
			ISOMessage isoMessage = null;
			
			if (payloadMessageConfig.getMessageVO() != null){
				isoMessage = new ISOMessage(payloadMessageConfig.getMessageVO());
			}
			
			setVSResourceName("ISO8583 tcp://"+ isoConnection.getHost() +":"+ isoConnection.getPort(), testExec);
			
			final byte[] data = (isoMessage != null ? payloadMessageConfig.getIsoConfig().getDelimiter().preparePayload(isoMessage, payloadMessageConfig.getIsoConfig()) : null);
			final ISOConnection isoKeepaliveConnection = isoConnection;
			
			isoConnection.setIsoConfig(payloadMessageConfig.getIsoConfig());
			isoConnection.putCallback(String.valueOf(Thread.currentThread().getId()), new CallbackAction() {

				@Override
				public void dataReceived(SocketPayload payload) throws ParseException {
					testExec.setStateValue(ISO8583_LISTENER_RECEIVED, System.currentTimeMillis());
					payloadMessageConfig.updateFromPayload(payload.getData());
					te.setLastResponse(payloadMessageConfig.getXML());
					
					Request request = new Request();
					
					MessageVO messageVO = payloadMessageConfig.buildMessageStructureFromXML(payloadMessageConfig.getXML());
					ParameterList al = new ParameterList();
					
					request.setOperation(messageVO.getType());

					for (FieldVO fieldVO : messageVO.getFieldList()) {
						al.put(fieldVO.getBitNum().toString(), fieldVO.getValue());
//						testExec.setStateObject("bit_"+ fieldVO.getBitNum().toString(), fieldVO.getValue());
					}
					
					request.setArguments(al);
					request.setBody(payloadMessageConfig.getXML());
					
					testExec.setStateObject("socketToRespond", payload.getSocket());
					testExec.setStateObject("lisa.vse.request", request);
					
					Out.log("ISO8583ListenerNode", "Data received!");
				}

				@Override
				public void keepalive() {
					try {
						if (data != null)
							isoKeepaliveConnection.send(new SocketPayload(data, isoKeepaliveConnection.getClientSocket()));
					} 
					catch (IOException | ParseException | InterruptedException e) {
//						final TestExecConnectionManager connManager = new TestExecConnectionManager(testExec,getConnectionName());
						
						TestExecConnectionManager.disconnect(getConnectionName());
						
//						connManager.cleanUp();
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
			
			TestExecConnectionManager.connect(getConnectionName(), String.valueOf(Thread.currentThread().getId()));
			isoConnection.processNextPayload(String.valueOf(Thread.currentThread().getId()), true, Integer.parseInt(keepalive));
			testExec.setStateValue(ISO8583_LISTENER_CONNECTIONDROPPED, String.valueOf(isoConnection.isConnected()));
		}
		catch (Exception x) {
			Out.log("ISO8583ListenerNode", "Error "+ x.getMessage());
			
			TestExecConnectionManager.disconnect(getConnectionName());
			throw new TestRunException(x.getMessage(), x);
		}
	}

	@Override
	public void initialize(TestCase testCase, Element node) {
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
