package com.ca.iso8583.steps;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.ca.iso8583.gui.CreateOrUseConnectionPanel;
import com.ca.iso8583.gui.PnlMain;
import com.ca.iso8583.vo.ConnectionInfoVO;
import com.itko.lisa.editor.CustomEditor;
import com.itko.lisa.editor.TestNodeInfo;

public class ISO8583ListenerCustomEditor extends CustomEditor {

	private static final long serialVersionUID = 4954935557851887461L;

	private PnlMain pnlMain;
	private CreateOrUseConnectionPanel createOrUseConnectionPanel;
	private JLabel lblKeepAlive = new JLabel("Send keep-alive message for each (seconds):");
	private JTextField txtKeepAlive = new JTextField("0");
	private TestNodeInfo nodeInfo;
	
	@Override
	public void display() {
		if (pnlMain == null) {
			nodeInfo = getController().getTestNode();
			
			setLayout(null);
			
			pnlMain = new PnlMain();
			createOrUseConnectionPanel = new CreateOrUseConnectionPanel();
			
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
			
			createOrUseConnectionPanel.setBounds(0, 20, 400, 200);
			pnlMain.getPnlGuiMessagesClient().getCkRequestSync().setBounds(10, 240, 179, 22);
			lblKeepAlive.setBounds(15, 270, 230, 22);
			txtKeepAlive.setBounds(240, 270, 40, 22);
			
			pnlMain.getPnlGuiMessagesClient().getConnectionPanel().removeAll();
			pnlMain.getPnlGuiMessagesClient().getConnectionPanel().add(createOrUseConnectionPanel);
			pnlMain.getPnlGuiMessagesClient().getConnectionPanel().add(pnlMain.getPnlGuiMessagesClient().getCkRequestSync());
			pnlMain.getPnlGuiMessagesClient().getConnectionPanel().add(lblKeepAlive);
			pnlMain.getPnlGuiMessagesClient().getConnectionPanel().add(txtKeepAlive);
			
			pnlMain.getPnlGuiMessagesClient().getTabbedPane().setTitleAt(1, "Keep-alive Request");
			pnlMain.getPnlGuiMessagesClient().getTabbedPane().setTitleAt(2, "Keep-alive Response");
			pnlMain.getTabbedPane().removeTabAt(1);
			pnlMain.getTabbedPane().setTitleAt(0, "Connection");
			
			add(pnlMain);
			
			try {
				if (nodeInfo.getAttribute("ISO8583RequestStepContent") != null) {
					pnlMain.getPnlGuiMessagesClient().setXmlRequest(pnlMain, nodeInfo.getAttribute("ISO8583RequestStepContent").toString());
				}
			}
			catch (Exception x) {
				x.printStackTrace();
				JOptionPane.showMessageDialog(pnlMain, "Error loading XML\n" + x.getMessage());
			}
		}
		
		createOrUseConnectionPanel.getConnectionChooserPanel().updateCombo(nodeInfo);
		
		try {
			if (nodeInfo.getAttribute("ISO8583ConnectionInfo") != null && !"".equals(nodeInfo.getAttribute("ISO8583ConnectionInfo").toString()))
				createOrUseConnectionPanel.getCreateConnectionPanel().loadConnectionInfo(new ConnectionInfoVO(nodeInfo.getAttribute("ISO8583ConnectionInfo").toString()));
			else if (nodeInfo.getAttribute("ISO8583ConnectionName") != null)
				createOrUseConnectionPanel.getConnectionChooserPanel().selectConnectionName(nodeInfo.getAttribute("ISO8583ConnectionName").toString());
			
			if (nodeInfo.getAttribute("ISO8583Keepalive") != null)
				txtKeepAlive.setText(nodeInfo.getAttribute("ISO8583Keepalive").toString());
			
			createOrUseConnectionPanel.updateSelectedRadio();
		}
		catch (Exception x) {
			x.printStackTrace();
			JOptionPane.showMessageDialog(pnlMain, "Error loading XML\n" + x.getMessage());
		}
	}

	@Override
	public String isEditorValid() {
		return createOrUseConnectionPanel.isEditorValid();
	}

	@Override
	public void save() {
		if (pnlMain.getPnlGuiMessagesClient().getPnlRequest().getXmlText().getText().indexOf("<%") == -1 && 
				pnlMain.getPnlGuiMessagesClient().getPnlRequest().getXmlText().getText().indexOf("%>") == -1) {
			
			nodeInfo.putAttribute("ISO8583RequestStepContent", pnlMain.getPnlGuiMessagesClient().getXmlRequest(pnlMain));
		}
		else {
			nodeInfo.putAttribute("ISO8583RequestStepContent", pnlMain.getPnlGuiMessagesClient().getPnlRequest().getXmlText().getText());
		}
		
		nodeInfo.putAttribute("ISO8583Keepalive", txtKeepAlive.getText());
		
		nodeInfo.removeAttribute("ISO8583ConnectionInfo");
		nodeInfo.removeAttribute("ISO8583ConnectionName");
		if (createOrUseConnectionPanel.getCreateConnectionPanel().getConnectionInfo() != null)
			nodeInfo.putAttribute("ISO8583ConnectionInfo", createOrUseConnectionPanel.getCreateConnectionPanel().getConnectionInfo().getSaveString());
		else if (createOrUseConnectionPanel.getConnectionChooserPanel().getSelectedConnectionInfo() != null)
			nodeInfo.putAttribute("ISO8583ConnectionName", createOrUseConnectionPanel.getConnectionChooserPanel().getSelectedConnectionInfo().getName());
	}

}
