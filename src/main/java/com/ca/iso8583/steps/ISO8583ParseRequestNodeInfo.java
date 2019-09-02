package com.ca.iso8583.steps;

import java.io.PrintWriter;

import com.itko.util.XMLUtils;

public class ISO8583ParseRequestNodeInfo extends GenericNodeInfo {

	@Override
	public void writeSubXML(PrintWriter pw) {
		XMLUtils.streamTagAndChild(pw, "ISO8583ParseRequestFrom", (String) getAttribute("ISO8583ParseRequestFrom"));
		XMLUtils.streamTagAndChild(pw, "ISO8583ParseRequestTo", (String) getAttribute("ISO8583ParseRequestTo"));
		pw.flush();
	}

	@Override
	public void migrate(Object node) {
		putAttribute("ISO8583ParseRequestFrom", ((ISO8583ParseRequestNode) node).getPayloadFrom());
		putAttribute("ISO8583ParseRequestTo", ((ISO8583ParseRequestNode) node).getPayloadTo());
	}

}
