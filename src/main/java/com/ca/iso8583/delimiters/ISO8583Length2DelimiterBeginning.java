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
        	
		endOfRequest = bytes.size();
		startOfNextRequest = 0;

    	//LOG ####################################################################################################
		System.out.println("--- Thread("+ Thread.currentThread().getId() +"):Delimiter ---");
		
		if (bytes.size() > 6) {
			try {
				for (int i = 0; i < bytes.size(); i++) {
					System.out.println("Thread("+ Thread.currentThread().getId() +"):Delimiter :: data["+ (i) +"] = "+ ((int) bytes.get( i )) +";");	
				}
				
//				System.out.println("Thread("+ Thread.currentThread().getId() +"):Delimiter :: data["+ (i + 0) +"] = "+ ((int) bytes.get( i + 0 )) +";");
//				System.out.println("Thread("+ Thread.currentThread().getId() +"):Delimiter :: data["+ (i + 1) +"] = "+ ((int) bytes.get( i + 1 )) +";");
//				System.out.println("Thread("+ Thread.currentThread().getId() +"):Delimiter :: data["+ (i + 2) +"] = "+ ((int) bytes.get( i + 2 )) +";");
//				System.out.println("Thread("+ Thread.currentThread().getId() +"):Delimiter :: data["+ (i + 3) +"] = "+ ((int) bytes.get( i + 3 )) +";");
//				System.out.println("Thread("+ Thread.currentThread().getId() +"):Delimiter :: data["+ (i + 4) +"] = "+ ((int) bytes.get( i + 4 )) +";");
//				System.out.println("Thread("+ Thread.currentThread().getId() +"):Delimiter :: data["+ (i + 5) +"] = "+ ((int) bytes.get( i + 5 )) +";");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("--- Thread("+ Thread.currentThread().getId() +"):Delimiter :: data.size() = "+  bytes.size());
		System.out.println("--- Thread("+ Thread.currentThread().getId() +"):Delimiter :: endOfRequest = "+ endOfRequest);
		System.out.println("--- Thread("+ Thread.currentThread().getId() +"):Delimiter :: startOfNextRequest = "+ startOfNextRequest);
		System.out.println("--- Thread("+ Thread.currentThread().getId() +"):Delimiter :: MessageComplete: "+ String.valueOf(result) +" ---");
		
		return result;  
	}

	@Override
	public String validate() {
		return null;
	}

}
