package com.ca.iso8583.steps;

import java.io.PrintWriter;

import org.w3c.dom.Element;

import com.itko.lisa.test.TestCase;
import com.itko.lisa.test.TestDefException;
import com.itko.lisa.test.TestNode;
import com.itko.util.XMLUtils;

public abstract class GenericConnectionChooserNode extends TestNode {

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
		//XMLUtils.streamTagAndChild(pw, "ISO8583ConnectionInfo", (String) getAttribute("ISO8583ConnectionInfo"));
		XMLUtils.streamTagAndChild(pw, "ISO8583ConnectionInfo", connectionName);
		pw.flush();
	}

}
