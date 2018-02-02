package com.ca.iso8583.steps;

import java.io.PrintWriter;

import com.itko.util.XMLUtils;

public class ISO8583RequestNodeInfo extends GenericNodeInfo implements GenericCreateConnectionNodeInfoInterface {

	@Override
	public void writeSubXML(PrintWriter pw) {
		XMLUtils.streamTagAndChild(pw, "ISO8583RequestStepContent", (String) getAttribute("ISO8583RequestStepContent"));
		XMLUtils.streamTagAndChild(pw, "ISO8583ConnectionInfo", (String) getAttribute("ISO8583ConnectionInfo"));
		XMLUtils.streamTagAndChild(pw, "ISO8583ConnectionName", (String) getAttribute("ISO8583ConnectionName"));
		pw.flush();
	}

	@Override
	public void migrate(Object node) {
		putAttribute("ISO8583RequestStepContent", ((ISO8583RequestNode) node).getStepContent());
		putAttribute("ISO8583ConnectionInfo", ((ISO8583RequestNode) node).getSavedConnectionInfo());
		putAttribute("ISO8583ConnectionName", ((ISO8583RequestNode) node).getConnectionName());
	}
	
}
