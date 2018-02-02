package com.ca.iso8583.protocol;

import org.apache.log4j.Logger;

import com.ca.iso8583.helper.PayloadMessageConfig;
import com.ca.iso8583.vo.FieldVO;
import com.ca.iso8583.vo.MessageVO;
import com.ca.iso8583.wizardsteps.ISO8583ConfigWizard;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.vse.stateful.model.Request;
import com.itko.lisa.vse.stateful.model.Response;
import com.itko.lisa.vse.stateful.model.TransientResponse;
import com.itko.lisa.vse.stateful.protocol.DataProtocol;
import com.itko.util.ParameterList;

@SuppressWarnings("deprecation")
public class ISO8583DataProtocolHandler extends DataProtocol {

	private static Logger logger = Logger.getLogger(ISO8583DataProtocolHandler.class);
	
	private ISO8583ConfigWizard iso8583ConfigWizard;
	
	public ISO8583DataProtocolHandler() {
		this(null);
	}
	
	public ISO8583DataProtocolHandler(ISO8583ConfigWizard iso8583ConfigWizard) {
		this.iso8583ConfigWizard = iso8583ConfigWizard;
	}
	
	@Override
	public void updateRequest(TestExec testExec, Request request) {
		byte[] payload;
		
		try {
			ParameterList al = new ParameterList();
			payload = iso8583ConfigWizard.getIso8583Config().getDelimiter().clearPayload(request.getBodyAsByteArray(), iso8583ConfigWizard.getIso8583Config());
			
			PayloadMessageConfig payloadMessageConfig = new PayloadMessageConfig(iso8583ConfigWizard.getIso8583Config());
			payloadMessageConfig.updateFromPayload(payload);
			
			if (testExec != null) 
				testExec.setLastResponse(payloadMessageConfig.getXML());
			
			MessageVO messageVO = payloadMessageConfig.getMessageVOFromXML(payloadMessageConfig.getXML());
			
			request.setBinary(false);
			request.setOperation(messageVO.getType());

			for (FieldVO fieldVO : messageVO.getFieldList())
				al.put(fieldVO.getBitNum().toString(), fieldVO.getValue());
			
			request.setArguments(al);
		} 
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public void updateRequest(Request request) {
		updateRequest(null, request);
	}

	@Override
	public void updateResponse(Response response) {
		response.setBinary(false);
		response.setBody(getResponseXML(response.getBodyAsByteArray()));
	}

	@Override
	public void updateResponse(TestExec testExec, TransientResponse response) {
		response.setBinary(false);
		response.setBody(getResponseXML(response.getBodyAsByteArray()));
	}

	private String getResponseXML(byte[] payloadBytes) {
		byte[] payload;
		PayloadMessageConfig payloadMessageConfig = null;
		
		try {
			payload = iso8583ConfigWizard.getIso8583Config().getDelimiter().clearPayload(payloadBytes, iso8583ConfigWizard.getIso8583Config());
			payloadMessageConfig = new PayloadMessageConfig(iso8583ConfigWizard.getIso8583Config());
			payloadMessageConfig.updateFromPayload(payload);
		} 
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		}
		
		return payloadMessageConfig.getXML();
	}
}
