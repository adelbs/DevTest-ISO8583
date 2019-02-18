package com.ca.iso8583.protocol;

import org.adelbs.iso8583.helper.Iso8583Config;

import com.ca.iso8583.wizardsteps.ISO8583ConfigWizard;
import com.itko.lisa.gui.WizardStep;
import com.itko.lisa.vse.stateful.model.TransactionProcessor;
import com.itko.lisa.vse.stateful.protocol.VSMFactory;
import com.itko.lisa.vse.stateful.protocol.tcp.TCPConnectionPanel;
import com.itko.lisa.vse.stateful.protocol.tcp.TCPProtocolHandler;
import com.itko.lisa.vse.stateful.protocol.tcp.recordingproxy.ProxyConfig;
import com.itko.lisa.vse.stateful.recorder.RecordingWizard;
import com.itko.lisa.vse.stateful.recorder.WizardPhase;

public class ISO8583TransportProtocolHandler extends TCPProtocolHandler implements TransactionProcessor, ProxyConfig {

	private ISO8583ConfigWizard iso8583ConfigWizard = new ISO8583ConfigWizard(this);
	
	public ISO8583TransportProtocolHandler() {
		super();
		addDataProtocol(new ISO8583DataProtocolHandler(iso8583ConfigWizard), true);
		addDataProtocol(new ISO8583DataProtocolHandler(iso8583ConfigWizard), false);
	}

	@Override
	public VSMFactory getVSMFactory(boolean outputAsObject) {
		return new ISO8583VSMFactory(this, outputAsObject);
	}
	
	@Override
	public WizardStep[] getWizardSteps(RecordingWizard wizard, WizardPhase phase) {
        if(phase == WizardPhase.CONFIGURATION)
            return (new WizardStep[] {
            	iso8583ConfigWizard,
                new TCPConnectionPanel(wizard, this)
                //new TCPConnectionPanel2(wizard, this, phase)
            });
        if(phase == WizardPhase.SESSION_ID)
            /*return (new WizardStep[] {
            	new TCPConnectionPanel2(wizard, this, phase),
            });*/
        	return null;
        if(phase == WizardPhase.FINALIZE)
            return null;
        else
            return null;
	}

	public Iso8583Config getIso8583Config() {
		return iso8583ConfigWizard.getIso8583Config();
	}
}
