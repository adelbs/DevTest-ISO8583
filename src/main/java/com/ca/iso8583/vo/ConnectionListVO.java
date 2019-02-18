package com.ca.iso8583.vo;

import java.util.Collection;
import java.util.Hashtable;

public class ConnectionListVO {

	private Hashtable<String, ConnectionInfoVO> hashConn = new Hashtable<String, ConnectionInfoVO>();
	
	public void add(String name, ConnectionInfoVO connInfo) {
		hashConn.put(name, connInfo);
	}
	
	public void remove(String name) {
		hashConn.remove(name);
	}
	
	public ConnectionInfoVO[] toArray() {
		Collection<ConnectionInfoVO> coll = hashConn.values();
		return (ConnectionInfoVO[]) coll.toArray(new ConnectionInfoVO[0]);
	}
}
