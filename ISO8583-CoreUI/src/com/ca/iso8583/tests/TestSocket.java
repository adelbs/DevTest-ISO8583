package com.ca.iso8583.tests;

import java.io.IOException;

import com.ca.iso8583.clientserver.ISOConnection;

public class TestSocket {

	public static void main(String[] args) throws IOException {
	//	new ISOClient("localhost", 8080, null, null);
		new ISOConnection(true, "localhost", 9090, 1000);
		
		System.out.println(System.currentTimeMillis());
	}
	
}
