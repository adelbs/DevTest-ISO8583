package com.ca.iso8583.gui;

import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.ca.iso8583.steps.GenericCreateConnectionNodeInfoInterface;
import com.ca.iso8583.vo.ConnectionInfoVO;
import com.itko.lisa.editor.TestNodeInfo;

public class ConnectionChooserPanel extends JPanel {

	private static final long serialVersionUID = 6300503276863040133L;

	private JLabel lblConnectionName = new JLabel("Connection name: ");
	private JComboBox<ConnectionInfoVO> cmbConnectionName = new JComboBox<ConnectionInfoVO>();

	public ConnectionChooserPanel() {
		setLayout(null);
		setBorder(new TitledBorder(null, "Choose existing Connection", TitledBorder.LEADING, TitledBorder.TOP, null,
				null));

		lblConnectionName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblConnectionName.setBounds(10, 20, 100, 20);
		cmbConnectionName.setBounds(110, 20, 200, 20);

		add(lblConnectionName);
		add(cmbConnectionName);
	}

	public void updateCombo(TestNodeInfo nodeInfo) {
		ArrayList<ConnectionInfoVO> connList = new ArrayList<ConnectionInfoVO>();

		for (TestNodeInfo stepNodeInfo : nodeInfo.getTestCaseInfo().getNodes()) {
			if (GenericCreateConnectionNodeInfoInterface.class.isAssignableFrom(stepNodeInfo.getClass())) {
				if (stepNodeInfo.getAttribute("ISO8583ConnectionInfo") != null
						&& !"".equals(stepNodeInfo.getAttribute("ISO8583ConnectionInfo").toString()))
					connList.add(new ConnectionInfoVO(stepNodeInfo.getAttribute("ISO8583ConnectionInfo").toString()));
			}
		}

		cmbConnectionName.setModel(new DefaultComboBoxModel<ConnectionInfoVO>(
				(ConnectionInfoVO[]) connList.toArray(new ConnectionInfoVO[] {})));
	}

	public void enable(boolean value) {
		lblConnectionName.setEnabled(value);
		cmbConnectionName.setEnabled(value);
	}

	public ConnectionInfoVO getSelectedConnectionInfo() {
		return (ConnectionInfoVO) cmbConnectionName.getSelectedItem();
	}

	public void selectConnectionName(String name) {
		for (int i = 0; i < cmbConnectionName.getItemCount(); i++) {
			if (cmbConnectionName.getItemAt(i).getName().equals(name)) {
				cmbConnectionName.setSelectedIndex(i);
				break;
			}
		}
	}

}
