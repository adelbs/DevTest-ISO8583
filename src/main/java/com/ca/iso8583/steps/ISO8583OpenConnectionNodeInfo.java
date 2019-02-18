package com.ca.iso8583.steps;

import java.io.PrintWriter;

import com.itko.util.XMLUtils;

public class ISO8583OpenConnectionNodeInfo extends GenericNodeInfo implements GenericCreateConnectionNodeInfoInterface {

	@Override
	public void writeSubXML(PrintWriter pw) {
		XMLUtils.streamTagAndChild(pw, "ISO8583ConnectionInfo", (String) getAttribute("ISO8583ConnectionInfo"));
		pw.flush();
	}

	@Override
	public void migrate(Object node) {
		putAttribute("ISO8583ConnectionInfo", ((ISO8583OpenConnectionNode) node).getSavedConnectionInfo());
	}

}
