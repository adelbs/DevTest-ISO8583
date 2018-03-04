package com.ca.iso8583.steps;

import org.adelbs.iso8583.clientserver.ISOConnection;

import com.itko.lisa.test.TestExec;
import com.itko.lisa.test.TestRunException;

public class ISO8583CloseConnectionNode extends GenericConnectionChooserNode {

	@Override
	public String getTypeName() throws Exception {
		return "ISO8583 Close Connection";
	}

	@Override
	protected void execute(TestExec testExec) throws TestRunException {
		try {
			ISOConnection isoConnection = (ISOConnection) testExec.getStateValue(getConnectionName());
			isoConnection.endConnection();
		}
		catch (Exception x) {
			throw new TestRunException(x.getMessage(), x);
		}
	}

}
