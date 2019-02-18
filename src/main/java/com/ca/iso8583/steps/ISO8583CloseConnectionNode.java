package com.ca.iso8583.steps;

import java.io.PrintWriter;

import org.adelbs.iso8583.clientserver.ISOConnection;
import org.w3c.dom.Element;

import com.itko.lisa.test.TestCase;
import com.itko.lisa.test.TestDefException;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.test.TestNode;
import com.itko.lisa.test.TestRunException;
import com.itko.util.XMLUtils;

public class ISO8583CloseConnectionNode extends TestNode {

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

	@Override
	public String getTypeName() throws Exception {
		return "ISO8583 Close Connection";
	}

	@Override
	protected void execute(TestExec testExec) throws TestRunException {
		try {
			ISOConnection isoConnection = (ISOConnection) testExec.getStateValue(getConnectionName());
			isoConnection.endConnection();
		}
		catch (Exception x) {
			throw new TestRunException(x.getMessage(), x);
		}
	}

}
