package com.ca.iso8583.protocol;

import java.util.List;

import com.ca.iso8583.helper.Iso8583Config;
import com.ca.iso8583.util.ISOUtils;

public class ISO8583Length4DelimiterBeginning implements ISO8583Delimiter {

	@Override
	public String getName() {
		return "4 bytes (Length) Delimiter Beginning";
	}

	@Override
	public String getDesc() {
		return "Adds 4 bytes at the beginning of the message. These bytes represent the message size.";
	}

	@Override
	public byte[] preparePayload(ISOMessage isoMessage, Iso8583Config isoConfig) {
		byte[] data = ISOUtils.mergeArray(isoMessage.getMessageSize(4).getBytes(), isoMessage.getPayload());
		return data;
	}

	@Override
	public boolean isPayloadComplete(List<Byte> bytes, Iso8583Config isoConfig) {
		
		boolean result = false;
		
		if (bytes.size() > 4) {
			//byte[] data = ISOUtils.listToArray(bytes);
			int messageSize = getMessageSize(bytes); //Integer.parseInt(new String(ISOUtils.subArray(data, 0, 4)));

			result = bytes.size() == (messageSize + 4);
		}
		
		return result;
	}

	@Override
	public byte[] clearPayload(byte[] data, Iso8583Config isoConfig) {
		return ISOUtils.subArray(data, 4, data.length);
	}

	@Override
	public int getMessageSize(List<Byte> bytes) {
		if (bytes.size() > 4) {
			byte[] data = ISOUtils.listToArray(bytes);
			int messageSize = Integer.parseInt(new String(ISOUtils.subArray(data, 0, 4)));
			
			return messageSize;
		}
		return -1;
	}

}
