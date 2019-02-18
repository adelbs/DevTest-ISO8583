package com.ca.iso8583.wizardsteps;

import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JOptionPane;

import org.adelbs.iso8583.gui.PnlMain;
import org.adelbs.iso8583.helper.Iso8583Config;

import com.ca.iso8583.delimiters.ISO8583GenericDelimiter;
import com.ca.iso8583.protocol.ISO8583TransportProtocolHandler;
import com.itko.lisa.gui.WizardStep;

public class ISO8583ConfigWizard extends WizardStep {

	private static final long serialVersionUID = 1L;

	private ISO8583TransportProtocolHandler iso8583TransportProtocol;
	private PnlMain pnlMain;
	
	public ISO8583ConfigWizard(ISO8583TransportProtocolHandler iso8583TransportProtocol) {
		this.iso8583TransportProtocol = iso8583TransportProtocol;
	}
	
	@Override
	public void activate() {
		if (pnlMain == null) {
			setLayout(null);
			
			pnlMain = new PnlMain();
			
			pnlMain.setBounds(0, 0, getWidth() + 18, getHeight() + 45);
			
			addComponentListener(new ComponentListener() {
				@Override
				public void componentShown(ComponentEvent e) {}
				@Override
				public void componentResized(ComponentEvent e) {
					pnlMain.setBounds(0, 0, getWidth() + 18, getHeight() + 45);
				}
				@Override
				public void componentMoved(ComponentEvent e) {}
				@Override
				public void componentHidden(ComponentEvent e) {}
			});
			
			pnlMain.getTabbedPane().removeTabAt(0);
			pnlMain.getTabbedPane().removeTabAt(0);
			
			add(pnlMain);
		}
	}

	@Override
	public Component getHeaderComponent() {
		return null;
	}

	@Override
	public boolean save() {
		if (pnlMain.getTxtFilePath().getText().equals("")) {
			JOptionPane.showMessageDialog(pnlMain, "You must configure the message structure.");
			return false;
		}
		else {
			iso8583TransportProtocol.setRequestDelimiter(new ISO8583GenericDelimiter(pnlMain.getIso8583Config()));
			iso8583TransportProtocol.setResponseDelimiter(new ISO8583GenericDelimiter(pnlMain.getIso8583Config()));
			return true;
		}
	}

	public Iso8583Config getIso8583Config() {
		return pnlMain.getIso8583Config();
	}
}
