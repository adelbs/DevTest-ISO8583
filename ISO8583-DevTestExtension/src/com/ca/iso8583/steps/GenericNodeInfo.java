package com.ca.iso8583.steps;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.itko.lisa.editor.TestNodeInfo;

public abstract class GenericNodeInfo extends TestNodeInfo {

	@Override
	public String getHelpString() {
		return "Help String ISO8583";
	}

	@Override
	public void initNewOne() {
		System.out.println("Init New One");
	}

	@Override
	public String getEditorName() {
		return "Editor Name ISO8583";
	}

	@Override
	public Icon getSmallIcon() {
		return new ImageIcon(this.getClass().getResource("/com/ca/iso8583/resource/isoIconSmall.png"));
	}
	
	@Override
	public Icon getLargeIcon() {
		return new ImageIcon(this.getClass().getResource("/com/ca/iso8583/resource/isoIconBig.png"));
	}

}
