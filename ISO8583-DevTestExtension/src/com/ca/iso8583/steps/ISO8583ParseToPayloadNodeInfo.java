package com.ca.iso8583.steps;

import java.io.PrintWriter;

import com.itko.util.XMLUtils;

public class ISO8583ParseToPayloadNodeInfo extends GenericNodeInfo implements GenericCreateConnectionNodeInfoInterface {

	@Override
	public void writeSubXML(PrintWriter pw) {
		XMLUtils.streamTagAndChild(pw, "ISO8583ParseToPayloadFrom", (String) getAttribute("ISO8583ParseToPayloadFrom"));
		XMLUtils.streamTagAndChild(pw, "ISO8583ParseToPayloadTo", (String) getAttribute("ISO8583ParseToPayloadTo"));
		pw.flush();
	}

	@Override
	public void migrate(Object node) {
		putAttribute("ISO8583ParseToPayloadFrom", ((ISO8583RequestNode) node).getStepContent());
		putAttribute("ISO8583ParseToPayloadTo", ((ISO8583RequestNode) node).getSavedConnectionInfo());
	}

}
