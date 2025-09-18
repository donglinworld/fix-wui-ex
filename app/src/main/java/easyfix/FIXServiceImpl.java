package easyfix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import simplefix.Application;
import simplefix.Engine;
import simplefix.EngineFactory;
import simplefix.Message;
import simplefix.Session;

public class FIXServiceImpl implements FIXService {
    private final static Logger log = LoggerFactory.getLogger(FIXServiceImpl.class);
    
    private static EngineFactory _engineFact;
    private Engine               _engine;
    
    // Store messages for each session (sessionId -> list of messages)
    private final List<FixMessage> messageHistory = Collections.synchronizedList(new ArrayList<>());
    
    @Override
    public void init() {
        
        try {
            
            Class<?> classobj = Class.forName("simplefix.quickfix.EngineFactory");
            Object engineobj = classobj.getDeclaredConstructor().newInstance();
            
            if ( engineobj instanceof EngineFactory ) {
                
                _engineFact = (EngineFactory) engineobj;
                _engine = _engineFact.createEngine();
                _engine.initEngine("app/src/main/resources/banzai.cfg");
                
                Application application = new _Application();
                
                _engine.startInProcess(application);
                
                log.info("engine started");
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        
    }
    
    @Override
    public ArrayList<SessionStatus> getSessionList() throws IllegalArgumentException {
        ArrayList<SessionStatus> sessions = new ArrayList<SessionStatus>();
        
        for ( Session session : _engine.getAllSessions() ) {
            String status = "UNKNOWN";
            if (session instanceof simplefix.quickfix.Session) {
                simplefix.quickfix.Session qfSession = (simplefix.quickfix.Session) session;
                status = qfSession.getQuickFixSession().isLoggedOn() ? "UP" : "DOWN";
            } 
            sessions.add(new SessionStatus(session.getSenderCompID() + "<-->" + session.getTargetCompID(), status));
        }

        Collections.sort(sessions, (s1, s2) -> s1.getSessionId().compareTo(s2.getSessionId()));
        return sessions;
    }
    
    @Override
    public void sendMessage(String sessionId, String messageStr) throws Exception {
        log.debug("Sending message to session: {}", sessionId);
        log.debug("Message: {}", messageStr);

        Session session = findSession(sessionId);
        if (session == null) {
            throw new Exception("Session not found: " + sessionId);
        }
        
        // Parse message string into Message object
        simplefix.Message message = MsgUtil.convertToFIX(messageStr);

        // Send message through session
        session.sendAppMessage(message);
    }
    
    private Session findSession(String sessionId) {
        String[] compIds = sessionId.split("<-->");
        if (compIds.length != 2) {
            return null;
        }
        return _engine.lookupSession(compIds[0], compIds[1]);
    }
    
    private class _Application implements Application {
        
        public _Application() {
        }
        
        @Override
        public void onRecvAppMessage(final Message message, final Session session) {
            String sessionId = getSessionId(session);
            FixMessage fixMessage = new FixMessage(
                sessionId,
                "RECEIVED",
                message.toString(),
                getCurrentTimestamp()
            );
            messageHistory.add(fixMessage);
            log.debug("Received message for session {}: {}", sessionId, message);
        }
        
        @Override
        public void onLogon(final Session sessionId) {
            
        }
        
        @Override
        public void onLogout(final Session arg0) {
            
        }
        
        @Override
        public void onSendAppMessage(Message message, Session session) {
            String sessionId = getSessionId(session);
            FixMessage fixMessage = new FixMessage(
                sessionId,
                "SENT",
                message.toString(),
                getCurrentTimestamp()
            );
            messageHistory.add(fixMessage);
            log.debug("Sent message for session {}: {}", sessionId, message);
        }
        
        private String getSessionId(Session session) {
            return session.getSenderCompID() + "<-->" + session.getTargetCompID();
        }
        
        private String getCurrentTimestamp() {
            return java.time.LocalDateTime.now().toString();
        }
    };

    @Override
    public List<FixMessage> getSessionMessages(String sessionId) {
        log.debug("Getting messages for session: {}", sessionId);
        
        // Return messages for the specific session, sorted by timestamp in descending order
        return messageHistory.stream()
            .filter(msg -> msg.getSessionId().equals(sessionId))
            .sorted((m1, m2) -> m2.getTimestamp().compareTo(m1.getTimestamp()))
            .collect(java.util.stream.Collectors.toList());
    };
    
}
