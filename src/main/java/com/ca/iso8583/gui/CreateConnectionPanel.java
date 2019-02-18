package com.ca.iso8583.gui;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.ca.iso8583.vo.ConnectionInfoVO;

public class CreateConnectionPanel extends JPanel {

	private static final long serialVersionUID = -6687800967487763226L;

	private JLabel lblName = new JLabel("Connection name: ");
	private JLabel lblHost = new JLabel("Host: ");
	private JLabel lblPort = new JLabel("Port: ");
	private JLabel lblTimeout = new JLabel("Timeout: ");
	
	private JTextField txtName = new JTextField();
	private JTextField txtHost = new JTextField();
	private JTextField txtPort = new JTextField();
	private JTextField txtTimeout = new JTextField();
	private JCheckBox ckServer = new JCheckBox("Server");
	
	public CreateConnectionPanel() {
		setLayout(null);
		setBorder(new TitledBorder(null, "Create new Connection", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		lblName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblName.setBounds(10, 20, 100, 20);
		txtName.setBounds(110, 20, 200, 20);
		
		lblHost.setHorizontalAlignment(SwingConstants.RIGHT);
		lblHost.setBounds(10, 45, 100, 20);
		txtHost.setBounds(110, 45, 200, 20);
		
		lblPort.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPort.setBounds(10, 70, 100, 20);
		txtPort.setBounds(110, 70, 70, 20);

		lblTimeout.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTimeout.setBounds(10, 95, 100, 20);
		txtTimeout.setBounds(110, 95, 70, 20);
		
		ckServer.setBounds(190, 95, 80, 20);
		
		add(lblName);
		add(txtName);
		add(lblHost);
		add(txtHost);
		add(lblPort);
		add(txtPort);
		add(ckServer);
		add(lblTimeout);
		add(txtTimeout);
	}

	public String isEditorValid() {
		String validationError = null;
		if (txtName.getText().equals("") || txtHost.getText().equals("") || txtPort.getText().equals(""))
			validationError = "Please, inform the Name, Host and Port of the connection";
		return validationError;
	}
	
	public ConnectionInfoVO getConnectionInfo() {
		if (isEditorValid() == null)
			return new ConnectionInfoVO(txtName.getText(), txtHost.getText(), txtPort.getText(), txtTimeout.getText(), ckServer.isSelected());
		else 
			return null;
	}
	
	public void loadConnectionInfo(ConnectionInfoVO connVO) {
		txtName.setText(connVO.getName());
		txtHost.setText(connVO.getHost());
		txtPort.setText(String.valueOf(connVO.getPort()));
		txtTimeout.setText(String.valueOf(connVO.getTimeout()));
		ckServer.setSelected(connVO.isServer());
	}
	
	public void enable(boolean value) {
		lblName.setEnabled(value);
		lblHost.setEnabled(value);
		lblPort.setEnabled(value);
		txtName.setEnabled(value);
		txtHost.setEnabled(value);
		txtPort.setEnabled(value);
		lblTimeout.setEnabled(value);
		txtTimeout.setEnabled(value);
		ckServer.setEnabled(value);
	}
	
	public void clear() {
		txtName.setText("");
		txtHost.setText("");
		txtPort.setText("");
		txtTimeout.setText("");
		ckServer.setSelected(false);
	}
}
