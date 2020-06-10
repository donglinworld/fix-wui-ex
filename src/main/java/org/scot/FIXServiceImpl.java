package org.scot;

import java.util.ArrayList;
import java.util.Collections;

import simplefix.Application;
import simplefix.Engine;
import simplefix.EngineFactory;
import simplefix.Message;
import simplefix.Session;

public class FIXServiceImpl implements FIXService {

	private static EngineFactory _engineFact;
	private Engine _engine;


	@Override
	public void init() {

		try {

			Class<?> classobj = Class.forName("simplefix.quickfix.EngineFactory");
			Object engineobj = classobj.newInstance();

			if (engineobj instanceof EngineFactory) {

				_engineFact = (EngineFactory) engineobj;
				_engine = _engineFact.createEngine();
				_engine.initEngine("src/main/resources/banzai.cfg");

				Application application = new _Application();

				_engine.startInProcess(application);

				System.out.println("engine started");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public ArrayList<String> getSessionList() throws IllegalArgumentException {
		ArrayList<String> sessions = new ArrayList<String>();

		for (Session session : _engine.getAllSessions()) {
			sessions.add(session.getSenderCompID() + "<-->" + session.getTargetCompID());
		}

		Collections.sort(sessions);
		return sessions;
	}

	private static class _Application implements Application {

		public _Application() {
		}

		@Override
		public void onAppMessage(final Message arg0, final Session arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLogon(final Session sessionId) {

		}

		@Override
		public void onLogout(final Session arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void toAppMessage(Message arg0, Session arg1) {
			// TODO Auto-generated method stub
			
		}
	};

}
