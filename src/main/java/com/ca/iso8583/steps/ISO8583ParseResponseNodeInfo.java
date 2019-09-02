package com.ca.iso8583.steps;

import java.io.PrintWriter;

import com.itko.util.XMLUtils;

public class ISO8583ParseResponseNodeInfo extends GenericNodeInfo {

	@Override
	public void writeSubXML(PrintWriter pw) {
		XMLUtils.streamTagAndChild(pw, "ISO8583ParseResponseFrom", (String) getAttribute("ISO8583ParseResponseFrom"));
		XMLUtils.streamTagAndChild(pw, "ISO8583ParseResponseTo", (String) getAttribute("ISO8583ParseResponseTo"));
		pw.flush();
	}

	@Override
	public void migrate(Object node) {
		putAttribute("ISO8583ParseResponseFrom", ((ISO8583ParseResponseNode) node).getPayloadFrom());
		putAttribute("ISO8583ParseResponseTo", ((ISO8583ParseResponseNode) node).getPayloadTo());
	}

}
