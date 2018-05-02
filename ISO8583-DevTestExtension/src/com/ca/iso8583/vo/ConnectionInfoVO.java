package com.ca.iso8583.vo;

public class ConnectionInfoVO {

	private String name;
	
	private String host;
	
	private String port;
	
	private String timeout;
	
	private boolean server;

	public ConnectionInfoVO(String name, String host, String port, String timeout, boolean server) {
		this.name = name;
		this.host = host;
		this.port = port;
		this.timeout = timeout;
		this.server = server;
	}
	
	public ConnectionInfoVO(String savedString) {
		String[] savedValues = savedString.split("\\|");
		this.name = savedValues[0];
		this.host = savedValues[1];
		this.port = savedValues[2];
		this.timeout = savedValues[3];
		this.server = savedValues[4].equals("true");
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public String getSaveString() {
		return name + "|" + host + "|" + port + "|" + timeout + (server ? "|true" : "|false");
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public boolean isServer() {
		return server;
	}

	public void setServer(boolean server) {
		this.server = server;
	}

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}
	
}
