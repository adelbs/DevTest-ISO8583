package com.ca.iso8583.steps;

import javax.swing.JOptionPane;

import com.ca.iso8583.gui.ConnectionChooserPanel;
import com.itko.lisa.editor.CustomEditor;
import com.itko.lisa.editor.TestNodeInfo;

public abstract class GenericConnectionChooserCustomEditor extends CustomEditor {

	private static final long serialVersionUID = 1200729932708166650L;

	protected ConnectionChooserPanel connectionChooserPanel = null;
	private TestNodeInfo nodeInfo;
	
	@Override
	public void display() {
		if (connectionChooserPanel == null) {
			setLayout(null);
			
			nodeInfo = getController().getTestNode();
			connectionChooserPanel = new ConnectionChooserPanel();
			connectionChooserPanel.setBounds(10, 10, 340, 60);
			add(connectionChooserPanel);
		}
		
		connectionChooserPanel.updateCombo(nodeInfo);

		try {
			if (nodeInfo.getAttribute("ISO8583ConnectionName") != null)
				connectionChooserPanel.selectConnectionName(nodeInfo.getAttribute("ISO8583ConnectionName").toString());
		}
		catch (Exception x) {
			x.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error loading XML\n" + x.getMessage());
		}
	}

	@Override
	public String isEditorValid() {
		return null;
	}

	@Override
	public void save() {
		if (connectionChooserPanel.getSelectedConnectionInfo() != null)
			nodeInfo.putAttribute("ISO8583ConnectionName", connectionChooserPanel.getSelectedConnectionInfo().getName());
	}

}
