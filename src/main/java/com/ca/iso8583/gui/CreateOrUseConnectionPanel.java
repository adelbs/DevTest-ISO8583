package com.ca.iso8583.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class CreateOrUseConnectionPanel extends JPanel {

	private static final long serialVersionUID = -4872381282227442224L;

	private CreateConnectionPanel createConnectionPanel = null;
	private ConnectionChooserPanel connectionChooserPanel = null;
	
	private ButtonGroup radioGroup = new ButtonGroup();
	private JRadioButton radioNewConnection = new JRadioButton();
	private JRadioButton radioExistingConnection = new JRadioButton();
	
	private ActionListener radioAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			createConnectionPanel.enable(radioNewConnection.isSelected());
			connectionChooserPanel.enable(radioExistingConnection.isSelected());
			if (!radioNewConnection.isSelected()) createConnectionPanel.clear();
		}
	};
	
	public CreateOrUseConnectionPanel() {
		setLayout(null);
		
		createConnectionPanel = new CreateConnectionPanel();
		connectionChooserPanel = new ConnectionChooserPanel();
		
		radioNewConnection.setBounds(10, 6, 20, 20);
		createConnectionPanel.setBounds(23, 10, 340, 135);

		radioExistingConnection.setBounds(10, 151, 20, 20);
		connectionChooserPanel.setBounds(23, 155, 340, 60);
		
		radioGroup.add(radioNewConnection);
		radioGroup.add(radioExistingConnection);
		
		radioNewConnection.setSelected(true);
		radioExistingConnection.setSelected(false);
		radioAction.actionPerformed(null);
		
		add(radioNewConnection);
		add(createConnectionPanel);
		add(radioExistingConnection);
		add(connectionChooserPanel);
		
		radioNewConnection.addActionListener(radioAction);
		radioExistingConnection.addActionListener(radioAction);
	}

	public String isEditorValid() {
		String result = null;
		if (radioNewConnection.isSelected())
			result = createConnectionPanel.isEditorValid();
		return result;
	}

	public CreateConnectionPanel getCreateConnectionPanel() {
		return createConnectionPanel;
	}

	public ConnectionChooserPanel getConnectionChooserPanel() {
		return connectionChooserPanel;
	}

	public JRadioButton getRadioNewConnection() {
		return radioNewConnection;
	}

	public JRadioButton getRadioExistingConnection() {
		return radioExistingConnection;
	}
	
	public void updateSelectedRadio() {
		radioNewConnection.setSelected(createConnectionPanel.getConnectionInfo() != null);
		radioExistingConnection.setSelected(createConnectionPanel.getConnectionInfo() == null);
		radioAction.actionPerformed(null);
	}
}
