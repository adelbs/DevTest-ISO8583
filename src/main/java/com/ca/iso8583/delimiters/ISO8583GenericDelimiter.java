package com.ca.iso8583.delimiters;

import java.util.List;

import org.adelbs.iso8583.exception.InvalidPayloadException;
import org.adelbs.iso8583.helper.Iso8583Config;

import com.itko.lisa.vse.stateful.protocol.tcp.delimiters.TCPDelimiter;

public class ISO8583GenericDelimiter implements TCPDelimiter {

	private Iso8583Config isoConfig;
	
	private int startOfNextRequest = -1;
	private int endOfRequest = -1;
	
	public ISO8583GenericDelimiter(Iso8583Config isoConfig) {
		this.isoConfig = isoConfig;
	}
	
	@Override
	public void configure(String config) {
		System.out.println(config);
	}

	@Override
	public int getEndOfRequest() {
		return endOfRequest;
	}

	@Override
	public int getStartOfNextRequest() {
		return startOfNextRequest;
	}

	@Override
	public boolean locateRequest(List<Byte> bytes) {
		boolean result = false;
		try {
			result = isoConfig.getDelimiter().isPayloadComplete(bytes, isoConfig);
		} 
		catch (InvalidPayloadException e) {
			result = true;
		}
		
		if (!result) {
            endOfRequest = -1;
            startOfNextRequest = -1;
        }
        else {
            endOfRequest = bytes.size();
            //endOfRequest = 1;
            startOfNextRequest = 0;
        }
		
		return result; 
	}

	@Override
	public String validate() {
		return null;
	}

}
