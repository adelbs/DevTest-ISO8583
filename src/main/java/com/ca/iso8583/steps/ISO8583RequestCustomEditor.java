package com.ca.iso8583.steps;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JOptionPane;

import org.adelbs.iso8583.gui.PnlMain;

import com.ca.iso8583.gui.CreateOrUseConnectionPanel;
import com.ca.iso8583.vo.ConnectionInfoVO;
import com.itko.lisa.editor.CustomEditor;
import com.itko.lisa.editor.TestNodeInfo;

public class ISO8583RequestCustomEditor extends CustomEditor {

	private static final long serialVersionUID = 1L;

	private PnlMain pnlMain;
	private CreateOrUseConnectionPanel createOrUseConnectionPanel;
	private TestNodeInfo nodeInfo;
	
	@Override
	public void display() {
		if (pnlMain == null) {
			nodeInfo = getController().getTestNode();
			
			setLayout(null);
			
			pnlMain = new PnlMain();
			createOrUseConnectionPanel = new CreateOrUseConnectionPanel();
			
			pnlMain.setBounds(0, 0, getWidth(), getHeight() + 50);
			
			addComponentListener(new ComponentListener() {
				@Override
				public void componentShown(ComponentEvent e) {}
				@Override
				public void componentResized(ComponentEvent e) {
					pnlMain.setBounds(0, 0, getWidth(), getHeight() + 50);
				}
				@Override
				public void componentMoved(ComponentEvent e) {}
				@Override
				public void componentHidden(ComponentEvent e) {}
			});
			
			createOrUseConnectionPanel.setBounds(0, 20, 400, 200);
			
			pnlMain.getPnlGuiMessagesClient().getConnectionPanel().removeAll();
			pnlMain.getPnlGuiMessagesClient().getConnectionPanel().add(createOrUseConnectionPanel);
			
			pnlMain.getTabbedPane().removeTabAt(1);
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
			
			createOrUseConnectionPanel.updateSelectedRadio();
		}
		catch (Exception x) {
			x.printStackTrace();
			JOptionPane.showMessageDialog(pnlMain, "Error loading XML\n" + x.getMessage());
		}

	}

	@Override
	public String isEditorValid() {
		String result = null;
		
		if (pnlMain.getTxtFilePath().getText().equals("") || pnlMain.getPnlGuiMessagesClient().getXmlRequest(pnlMain).indexOf("message") == -1)
			result = "You must configure the request message.";
		else
			result = createOrUseConnectionPanel.isEditorValid();
		
		return result;
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
		
		nodeInfo.removeAttribute("ISO8583ConnectionInfo");
		nodeInfo.removeAttribute("ISO8583ConnectionName");
		if (createOrUseConnectionPanel.getCreateConnectionPanel().getConnectionInfo() != null)
			nodeInfo.putAttribute("ISO8583ConnectionInfo", createOrUseConnectionPanel.getCreateConnectionPanel().getConnectionInfo().getSaveString());
		else if (createOrUseConnectionPanel.getConnectionChooserPanel().getSelectedConnectionInfo() != null)
			nodeInfo.putAttribute("ISO8583ConnectionName", createOrUseConnectionPanel.getConnectionChooserPanel().getSelectedConnectionInfo().getName());
	}

}
