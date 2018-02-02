package com.ca.iso8583.steps;

import java.io.PrintWriter;

import com.itko.util.XMLUtils;

public abstract class GenericConnectionChooserNodeInfo extends GenericNodeInfo {

	@Override
	public void writeSubXML(PrintWriter pw) {
		XMLUtils.streamTagAndChild(pw, "ISO8583ConnectionName", (String) getAttribute("ISO8583ConnectionName"));
		pw.flush();
	}

	@Override
	public void migrate(Object node) {
		putAttribute("ISO8583ConnectionName", ((GenericConnectionChooserNode) node).getConnectionName());
	}

}
