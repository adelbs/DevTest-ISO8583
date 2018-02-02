package com.ca.iso8583.gui;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;

public class PnlXmlConfig extends JPanel {
	
	private static final long serialVersionUID = 1L;

	private JPanel xmlPanel = new JPanel();
	
	public PnlXmlConfig(PnlMain pnlMain) {
		
		setLayout(null);
		
		xmlPanel.setLayout(new BorderLayout());
		xmlPanel.add(pnlMain.getIso8583Config().getXmlText());

		addComponentListener(new ComponentListener() {
			@Override
			public void componentShown(ComponentEvent e) {}
			@Override
			public void componentResized(ComponentEvent e) {
				xmlPanel.setBounds(0, 0, getWidth() - 15, getHeight());
			}
			@Override
			public void componentMoved(ComponentEvent e) {}
			@Override
			public void componentHidden(ComponentEvent e) {}
		}); 
		
		add(xmlPanel);
	}
}
