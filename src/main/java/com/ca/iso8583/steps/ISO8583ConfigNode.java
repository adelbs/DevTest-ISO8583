package com.ca.iso8583.steps;

import java.io.PrintWriter;

import org.adelbs.iso8583.helper.Iso8583Parser;
import org.w3c.dom.Element;

import com.itko.lisa.test.TestCase;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.test.TestNode;
import com.itko.lisa.test.TestRunException;
import com.itko.util.XMLUtils;

public class ISO8583ConfigNode extends TestNode {

	private String isoConfigFilePath = "";

	public String getisoConfigFilePath() {
		return isoConfigFilePath;
	}

	@Override
	public String getTypeName() throws Exception {
		return "ISO8583 Config";
	}

	@Override
	protected void execute(TestExec testExec) throws TestRunException {
		testExec.setStateObject("isoParser", new Iso8583Parser(isoConfigFilePath));
	}

	@Override
	public void initialize(TestCase testCase, Element element) {
		for (int i = 0; i < element.getChildNodes().getLength(); i++) {
			if (element.getChildNodes().item(i).getNodeName().equals("ISO8583ConfigFilePath"))
				this.isoConfigFilePath = element.getChildNodes().item(i).getTextContent();
		}
	}

	@Override
	public void writeSubXML(PrintWriter pw) {
		XMLUtils.streamTagAndChild(pw, "ISO8583ConfigFilePath", isoConfigFilePath);
		pw.flush();
	}

}
