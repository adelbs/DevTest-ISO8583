package com.ca.iso8583.steps;

import javax.swing.JOptionPane;

import com.ca.iso8583.gui.CreateConnectionPanel;
import com.ca.iso8583.vo.ConnectionInfoVO;
import com.itko.lisa.editor.CustomEditor;
import com.itko.lisa.editor.TestNodeInfo;

public class ISO8583OpenConnectionCustomEditor extends CustomEditor {

	private static final long serialVersionUID = 5029268348472880609L;

	private CreateConnectionPanel createConnectionPanel = null;
	private TestNodeInfo nodeInfo;
	
	@Override
	public void display() {
		if (createConnectionPanel == null) {
			setLayout(null);
			
			nodeInfo = getController().getTestNode();
			createConnectionPanel = new CreateConnectionPanel();
			createConnectionPanel.setBounds(10, 10, 340, 135);
			add(createConnectionPanel);
		}
		
		try {
			if (nodeInfo.getAttribute("ISO8583ConnectionInfo") != null) {
				createConnectionPanel.loadConnectionInfo(new ConnectionInfoVO(nodeInfo.getAttribute("ISO8583ConnectionInfo").toString()));
			}
		}
		catch (Exception x) {
			x.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error loading XML\n" + x.getMessage());
		}
	}

	@Override
	public String isEditorValid() {
		return createConnectionPanel.isEditorValid();
	}

	@Override
	public void save() {
		if (createConnectionPanel.getConnectionInfo() != null)
			nodeInfo.putAttribute("ISO8583ConnectionInfo", createConnectionPanel.getConnectionInfo().getSaveString());
	}

}
