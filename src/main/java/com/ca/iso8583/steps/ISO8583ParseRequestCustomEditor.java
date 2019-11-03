package com.ca.iso8583.steps;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JOptionPane;

import org.adelbs.iso8583.gui.PnlMain;

import com.itko.lisa.editor.CustomEditor;
import com.itko.lisa.editor.TestNodeInfo;

public class ISO8583ParseRequestCustomEditor extends CustomEditor {

    private static final long serialVersionUID = 1L;

	private PnlMain pnlMain;
	private TestNodeInfo nodeInfo;

    @Override
    public void display() {
		if (pnlMain == null) {
			nodeInfo = getController().getTestNode();
			
			setLayout(null);
			
			pnlMain = new PnlMain();
			
			pnlMain.setBounds(0, 0, getWidth(), getHeight());
			
			addComponentListener(new ComponentListener() {
				@Override
				public void componentShown(ComponentEvent e) {}
				@Override
				public void componentResized(ComponentEvent e) {
					pnlMain.setBounds(0, 0, getWidth(), getHeight());
				}
				@Override
				public void componentMoved(ComponentEvent e) {}
				@Override
				public void componentHidden(ComponentEvent e) {}
			});
			
			pnlMain.getTabbedPane().removeTabAt(0);
			pnlMain.getTabbedPane().removeTabAt(0);
			
			add(pnlMain);
			
			try {
				if (nodeInfo.getAttribute("ISO8583ConfigFilePath") != null) {
					pnlMain.getTxtFilePath().setText(nodeInfo.getAttribute("ISO8583ConfigFilePath").toString());
					pnlMain.openXML();
				}
			}
			catch (Exception x) {
				x.printStackTrace();
				JOptionPane.showMessageDialog(pnlMain, "Error loading XML\n" + x.getMessage());
			}
		}
    }

    @Override
    public String isEditorValid() {
        return null;
    }

    @Override
    public void save() {
		if (pnlMain.getIso8583Config().getXmlFilePath() != null)
			nodeInfo.putAttribute("ISO8583ConfigFilePath", pnlMain.getIso8583Config().getXmlFilePath());
		else 
			nodeInfo.putAttribute("ISO8583ConfigFilePath", "");
    }

}
