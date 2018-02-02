package com.ca.iso8583.protocol;

import com.ca.iso8583.exception.ParseException;
import com.ca.iso8583.util.ISOUtils;
import com.ca.iso8583.vo.FieldVO;
import com.ca.iso8583.vo.MessageVO;


public class ISOMessage {

	private int messageSize;
	private byte[] payload;
	private StringBuilder visualPayload = new StringBuilder();

	private Bitmap bitmap;
	
	public ISOMessage(MessageVO messageVO) throws ParseException {
		this(null, messageVO);
	}
	
	public ISOMessage(byte[] payload, MessageVO messageVO) throws ParseException {
		if (payload != null)
			bitmap = new Bitmap(payload, messageVO);
		else
			bitmap = new Bitmap(messageVO);
				
		StringBuilder strMessage = new StringBuilder();
		strMessage.append(messageVO.getType());
		strMessage.append(bitmap.getPayloadBitmap());
		
		this.payload = bitmap.getHeaderEncoding().convert(messageVO.getType());
		this.payload = ISOUtils.mergeArray(this.payload, bitmap.getPayloadBitmap());
		
		for (int i = 0; i <= bitmap.getSize(); i++) {
			if (bitmap.getBit(i) != null) {
			/*	System.out.println("---");
				System.out.println(bitmap.getBit(i).getName());*/
				
				this.payload = ISOUtils.mergeArray(this.payload, bitmap.getBit(i).getPayloadValue());
				strMessage.append(bitmap.getBit(i).getPayloadValue());
			}
		}
		
		visualPayload.append(bitmap.getVisualPayload());
		visualPayload.append("\nEntire message: [").append(strMessage).append("]\n");
		
		this.messageSize = strMessage.length();
		//this.payload = strMessage.toString().getBytes();
	}
	
	public String getVisualPayload() {
		return visualPayload.toString();
	}
	
	public byte[] getPayload() {
		return payload;
	}
	
	public FieldVO getBit(int bit) {
		return bitmap.getBit(bit);
	}
	
	public int getMessageSize() {
		return messageSize;
	}
	
	public String getMessageSize(int numChars) {
		String result = String.valueOf(messageSize);
		while (result.length() < numChars)
			result = "0" + result;
		return result;
	}
	
	public MessageVO getMessageVO() {
		return bitmap.getMessageVO();
	}
}
