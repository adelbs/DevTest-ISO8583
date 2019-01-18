package com.ca.iso8583.steps;

import java.io.PrintWriter;

import com.itko.util.XMLUtils;

public class ISO8583ParseToRequestNodeInfo extends GenericNodeInfo implements GenericCreateConnectionNodeInfoInterface {

	@Override
	public void writeSubXML(PrintWriter pw) {
		XMLUtils.streamTagAndChild(pw, "ISO8583ParseToRequestFrom", (String) getAttribute("ISO8583ParseToRequestFrom"));
		XMLUtils.streamTagAndChild(pw, "ISO8583ParseToRequestTo", (String) getAttribute("ISO8583ParseToRequestTo"));
		pw.flush();
	}

	@Override
	public void migrate(Object node) {
		putAttribute("ISO8583ParseToRequestFrom", ((ISO8583RequestNode) node).getStepContent());
		putAttribute("ISO8583ParseToRequestTo", ((ISO8583RequestNode) node).getSavedConnectionInfo());
	}

}
