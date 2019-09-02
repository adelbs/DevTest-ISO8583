package com.ca.iso8583.steps;

import java.io.PrintWriter;

import org.adelbs.iso8583.util.Out;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;

import com.ca.iso8583.test.TestExecConnectionManager;
import com.ca.iso8583.vo.ConnectionInfoVO;
import com.itko.lisa.test.TestCase;
import com.itko.lisa.test.TestDefException;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.test.TestNode;
import com.itko.lisa.test.TestRunException;
import com.itko.util.XMLUtils;

public class ISO8583OpenConnectionNode extends TestNode implements GenericCreateConnectionNodeInterface {
	
	protected static Log logger = LogFactory.getLog("com.ca.iso8583.steps.ISO8583OpenConnectionNode");

	private String connectionInfo = "";
	
	public ISO8583OpenConnectionNode() {
		super();
	}
	
	public ISO8583OpenConnectionNode(String connectionInfo) {
		this.connectionInfo = connectionInfo;
	}
	
	@Override
	public String getTypeName() throws Exception {
		return "ISO8583 Open Connection";
	}

	@Override
	protected void execute(TestExec testExec) throws TestRunException {
//		TestExecConnectionManager connManager = null;
		ConnectionInfoVO connInfo = new ConnectionInfoVO(connectionInfo);
		
		try {
			
//			connManager = new TestExecConnectionManager(testExec, connInfo.getName());
//			connManager.connectTo(connInfo);
			
			TestExecConnectionManager.connectTo(connInfo, testExec);
			
			Out.log("ISO8583OpenConnectionNode", "Connecting...");
		}
		catch (Exception x) {
			Out.log("ISO8583OpenConnectionNode", "Error "+ x.getMessage());
			
//			if(connManager != null){
//				connManager.cleanUp();
//			}
			
			TestExecConnectionManager.disconnect(connInfo.getName());
			
			logger.error("Faile to Start a new ISO connection" + this, x);
			throw new TestRunException(x.getMessage(), x);
		}
	}

	@Override
	public void initialize(TestCase testCase, Element element) throws TestDefException {
		for (int i = 0; i < element.getChildNodes().getLength(); i++) {
			if (element.getChildNodes().item(i).getNodeName().equals("ISO8583ConnectionInfo")) {
				this.connectionInfo = element.getChildNodes().item(i).getTextContent();
				break;
			}
		}
	}
	
	@Override
	public void destroy(TestExec ts) {
		super.destroy(ts);
		try{
			final ConnectionInfoVO connInfo = new ConnectionInfoVO(connectionInfo); 
//			TestExecConnectionManager connManager  = new TestExecConnectionManager(ts, connInfo.getName());
//			connManager.cleanUp();
			
//			TestExecConnectionManager.disconnect(connInfo.getName());
			System.out.println("DESTROY :: TODO");
		}
		catch(Exception e){
			Out.log("ISO8583OpenConnectionNode", "Error on destroy "+ e.getMessage());
			
			logger.error("Failed to close ISO connection! " + this, e);
		}
	}

	@Override
	public String getSavedConnectionInfo() {
		return connectionInfo;
	}

	@Override
	public void writeSubXML(PrintWriter pw) {
		XMLUtils.streamTagAndChild(pw, "ISO8583ConnectionInfo", connectionInfo);
		pw.flush();
	}
}
