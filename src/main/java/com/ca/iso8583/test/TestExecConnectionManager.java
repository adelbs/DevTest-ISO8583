package com.ca.iso8583.test;

import org.adelbs.iso8583.clientserver.ISOConnection;

import com.ca.iso8583.vo.ConnectionInfoVO;
import com.itko.lisa.test.LisaException;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.test.TestRunException;

/**
 * Class with sole purpose to handle the creation of ISOConnections and the 
 * storage of this at {@link TestExec} instances
 *
 */
public class TestExecConnectionManager {
	
	private TestExec testExec;
	private String connectionName;
	
	
	public TestExecConnectionManager(final TestExec testExec, final String connectionName){
		this.testExec = testExec;
		this.connectionName = connectionName;
	}
	
	/**
	 * Connects to a Host:Port creating a new ISOConnection and storing it at TestExec
	 * @param connInfo 
	 * @throws LisaException
	 */
	public void connectTo(final ConnectionInfoVO connInfo) throws LisaException{
		try {
			ISOConnection isoConnection = new ISOConnection(connInfo.isServer(), 
					testExec.parseInState(connInfo.getHost()), 
					Integer.parseInt(testExec.parseInState(connInfo.getPort())), 
					Integer.parseInt(testExec.parseInState(connInfo.getTimeout())));
			testExec.setStateObject(this.connectionName, isoConnection);
		}
		catch (Exception x) {
			throw new TestRunException(x.getMessage(), x);
		}
	}
	
	/**
	 * Return actual TestExec connection
	 * @return an instance of {@ISOConnection} 
	 */
	public ISOConnection getCurrentConnection(){
		return (ISOConnection) testExec.getStateValue(this.connectionName);
	}
	
	/**
	 * Clean up the connection so no leaf overs are left behind, like open connection,
	 * zombie threads, etcs, Anything that might prevent the connect to be restarted right away
	 */
	public void cleanUp(){
		this.disconnect();
	}
	
	/**
	 * Disconect the actual conenction stored at TestExec	
	 */
	public void disconnect(){
		final Object testExecConnection = testExec.getStateValue(this.connectionName);
		if(testExecConnection != null && testExecConnection instanceof ISOConnection){
			ISOConnection isoConnection = (ISOConnection) testExecConnection;
			isoConnection.endConnection();
			isoConnection = null;
			testExec.setStateValue(this.connectionName, null);
		}
	}
	
	@Override
	public String toString() {
		return "TestExec Connection Manager ["+this.connectionName+"]";
	}
}
