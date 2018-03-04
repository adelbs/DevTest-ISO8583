package com.ca.iso8583.steps;

import java.io.PrintWriter;

import org.adelbs.iso8583.clientserver.ISOConnection;
import org.w3c.dom.Element;

import com.ca.iso8583.vo.ConnectionInfoVO;
import com.itko.lisa.test.TestCase;
import com.itko.lisa.test.TestDefException;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.test.TestNode;
import com.itko.lisa.test.TestRunException;
import com.itko.util.XMLUtils;

public class ISO8583OpenConnectionNode extends TestNode implements GenericCreateConnectionNodeInterface {

	private String connectionInfo = "";
	
	@Override
	public String getTypeName() throws Exception {
		return "ISO8583 Open Connection";
	}

	@Override
	protected void execute(TestExec testExec) throws TestRunException {
		try {
			ConnectionInfoVO connInfo = new ConnectionInfoVO(connectionInfo);
			ISOConnection isoConnection = new ISOConnection(connInfo.isServer(), connInfo.getHost(), connInfo.getPort(), connInfo.getTimeout());
			testExec.setStateObject(connInfo.getName(), isoConnection);
		}
		catch (Exception x) {
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
	public String getSavedConnectionInfo() {
		return connectionInfo;
	}

	@Override
	public void writeSubXML(PrintWriter pw) {
		//XMLUtils.streamTagAndChild(pw, "ISO8583ConnectionInfo", (String) getAttribute("ISO8583ConnectionInfo"));
		XMLUtils.streamTagAndChild(pw, "ISO8583ConnectionInfo", connectionInfo);
		pw.flush();
	}

}
