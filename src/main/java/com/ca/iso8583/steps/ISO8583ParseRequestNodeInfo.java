package com.ca.iso8583.steps;

import java.io.PrintWriter;

import com.itko.util.XMLUtils;

public class ISO8583ParseRequestNodeInfo extends GenericNodeInfo {

	@Override
	public void writeSubXML(PrintWriter pw) {
		XMLUtils.streamTagAndChild(pw, "ISO8583ConfigFilePath", (String) getAttribute("ISO8583ConfigFilePath"));
		pw.flush();
	}

	@Override
	public void migrate(Object node) {
		putAttribute("ISO8583ConfigFilePath", ((ISO8583ParseRequestNode) node).getisoConfigFilePath());
	}

}
