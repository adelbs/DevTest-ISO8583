package com.ca.iso8583.constants;

import com.ca.iso8583.protocol.ISO8583Delimiter;
import com.ca.iso8583.protocol.ISO8583GenericConfigDelimiter;
import com.ca.iso8583.protocol.ISO8583Length2DelimiterBeginning;
import com.ca.iso8583.protocol.ISO8583Length4DelimiterBeginning;

public enum DelimiterEnum {

	LENGTH2_DELIMITER_BEG("LENGTH2_DELIMITER_BEG", new ISO8583Length2DelimiterBeginning()),
	LENGTH4_DELIMITER_BEG("LENGTH4_DELIMITER_BEG", new ISO8583Length4DelimiterBeginning()),
	GENERIC_CONFIG_DELIMITER("GENERIC_CONFIG_DELIMITER", new ISO8583GenericConfigDelimiter());
	
	private ISO8583Delimiter isoDelimiter;
	private String value;
	
	DelimiterEnum(String value, ISO8583Delimiter isoDelimiter) {
		this.value = value;
		this.isoDelimiter = isoDelimiter;
	}
	
	public static DelimiterEnum getDelimiter(String value) {
		
		if ("LENGTH2_DELIMITER_BEG".equals(value))
			return DelimiterEnum.LENGTH2_DELIMITER_BEG;
		else if ("LENGTH4_DELIMITER_BEG".equals(value))
			return DelimiterEnum.LENGTH4_DELIMITER_BEG;
		else if ("GENERIC_CONFIG_DELIMITER".equals(value))
			return DelimiterEnum.GENERIC_CONFIG_DELIMITER;
		
		return DelimiterEnum.LENGTH2_DELIMITER_BEG;
	}
	
	public String toString() {
		return isoDelimiter.getName();
	}
	
	public ISO8583Delimiter getDelimiter() {
		return isoDelimiter;
	}
	
	public String getValue() {
		return value;
	}
}
