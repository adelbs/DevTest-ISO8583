package com.ca.iso8583.delimiters;

import java.util.List;

import org.adelbs.iso8583.exception.InvalidPayloadException;

import com.itko.lisa.vse.stateful.protocol.tcp.delimiters.TCPDelimiter;

public class ISO8583Length2DelimiterBeginning implements TCPDelimiter {

	private org.adelbs.iso8583.protocol.ISO8583Length2DelimiterBeginning delimiter = new org.adelbs.iso8583.protocol.ISO8583Length2DelimiterBeginning();

	private int startOfNextRequest = -1;
	private int endOfRequest = -1;

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
			result = delimiter.isPayloadComplete(bytes, null);
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
