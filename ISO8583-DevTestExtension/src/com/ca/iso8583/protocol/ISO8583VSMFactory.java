package com.ca.iso8583.protocol;

import com.ca.iso8583.steps.ISO8583ListenerNode;
import com.ca.iso8583.steps.ISO8583OpenConnectionNode;
import com.ca.iso8583.steps.ISO8583ResponderNode;
import com.itko.lisa.repo.ConfigEditSession;
import com.itko.lisa.test.TestCase;
import com.itko.lisa.test.TestDefException;
import com.itko.lisa.test.TestNode;
import com.itko.lisa.vse.ExecutionMode;
import com.itko.lisa.vse.ModuleVSE;
import com.itko.lisa.vse.stateful.protocol.DefaultVSMFactory;
import com.itko.lisa.vse.stateful.protocol.TransportProtocol;

public class ISO8583VSMFactory extends DefaultVSMFactory {

	private final TransportProtocol transportProtocol;
	
	public ISO8583VSMFactory(TransportProtocol transportProtocol, boolean outputAsObject) {
		super(transportProtocol, outputAsObject);
		this.transportProtocol = transportProtocol;
	}

	@Override
	public void createVSM(TestCase testCase, String serviceImageFileName) {
		ConfigEditSession session = new ConfigEditSession(testCase);Throwable localThrowable3 = null;
		try {
			session.ensureProperty("lisa.vse.execution.mode", ExecutionMode.EFFICIENT.name());
		}
		catch (Throwable localThrowable1) {
			localThrowable3 = localThrowable1;throw localThrowable1;
		}
		finally {
			if (session != null) {
				if (localThrowable3 != null) {
					try {
						session.close();
					}
					catch (Throwable localThrowable2) {
						localThrowable3.addSuppressed(localThrowable2);
					}
				} 
				else {
					session.close();
				}
			}
		}
		
		this.transportProtocol.setOutputAsObject(isOutputAsObject());
	    
		
		//open connection
		ISO8583OpenConnectionNode openConnection = new ISO8583OpenConnectionNode();
		openConnection.setName("Open Connection");
		addStepToTestCase(testCase, openConnection);
		
		//listener
		ISO8583ListenerNode listener = new ISO8583ListenerNode();
		listener.setName("Listener");
		addStepToTestCase(testCase, listener);
		
		//conversarional
		TestNode chooseStep = createLookupStep(testCase, serviceImageFileName);
		
		
		//responder
		ISO8583ResponderNode responder = new ISO8583ResponderNode();
		responder.setName("Responder");
		addStepToTestCase(testCase, responder);
		
	    this.transportProtocol.finalizeModel(testCase);
	}
	
	private void addStepToTestCase(TestCase testCase, TestNode step) {
		if ((testCase == null) || (step == null)) {
			return;
		}
		if (!testCase.containsNode(step)) {
			try {
				testCase.addNode(-1, step);
			}
			catch (TestDefException e) {
				throw new RuntimeException(ModuleVSE.resources.get("autogen.vse.stateful.recorder.RecordingSession.str.7", new Object[] {step.getClass().getCanonicalName(), e }));
			}
		}
	}
}
