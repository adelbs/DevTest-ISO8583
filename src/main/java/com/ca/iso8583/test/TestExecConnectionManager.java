package com.ca.iso8583.test;

import java.io.IOException;

import org.adelbs.iso8583.clientserver.ISOConnection;
import org.adelbs.iso8583.exception.ConnectionException;

import com.ca.iso8583.vo.ConnectionInfoVO;
import com.itko.lisa.test.LisaException;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.test.TestRunException;
import com.itko.lisa.vse.SharedModelMap;

/**
 * Class with sole purpose to handle the creation of ISOConnections and the 
 * storage of this at {@link TestExec} instances
 *
 */
public class TestExecConnectionManager {
	
	/**
	 * Connects to a Host:Port creating a new ISOConnection and storing it at TestExec
	 * @param connInfo 
	 * @throws LisaException
	 */
	public static synchronized void connectTo(final ConnectionInfoVO connInfo, TestExec testExec) throws LisaException{
		try {
			if (!SharedModelMap.containsKey(connInfo.getName())) {
				ISOConnection isoConnection = new ISOConnection(connInfo.isServer(), 
						testExec.parseInState(connInfo.getHost()), 
						Integer.parseInt(testExec.parseInState(connInfo.getPort())), 
						Integer.parseInt(testExec.parseInState(connInfo.getTimeout())));
				
				SharedModelMap.putObject(connInfo.getName(), isoConnection);
			}
		}
		catch (Exception x) {
			throw new TestRunException(x.getMessage(), x);
		}			
	}
	
	public static synchronized void connect(String connectionName, String thread) throws IOException, ConnectionException {
		if (!getCurrentConnection(connectionName).isConnected())
			getCurrentConnection(connectionName).connect(thread);
		else
			getCurrentConnection(connectionName).registerSender(thread);
	}
	
	/**
	 * Return actual TestExec connection
	 * @return an instance of {@ISOConnection} 
	 */
	public static ISOConnection getCurrentConnection(String connectionName){
		return (ISOConnection) SharedModelMap.getObject(connectionName);
	}
	
	/**
	 * Clean up the connection so no leaf overs are left behind, like open connection,
	 * zombie threads, etcs, Anything that might prevent the connect to be restarted right away
	 */
//	public void cleanUp(){
//		this.disconnect();
//	}
	
	/**
	 * Disconect the actual conenction stored at TestExec	
	 */
	public static synchronized void disconnect(String connectionName){
		final Object testExecConnection = SharedModelMap.getObject(connectionName);
		if(testExecConnection != null && testExecConnection instanceof ISOConnection){
			ISOConnection isoConnection = (ISOConnection) testExecConnection;
			isoConnection.endConnection(String.valueOf(Thread.currentThread().getId()));
			isoConnection = null;
			
			SharedModelMap.remove(connectionName);
		}
	}
	
//	@Override
//	public String toString() {
//		return "TestExec Connection Manager ["+this.connectionName+"]";
//	}
}
