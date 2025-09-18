package easyfix;

import java.util.List;

public interface FIXService {
    
    void init();
    
    List<SessionStatus> getSessionList();
    
    void sendMessage(String sessionId, String messageStr) throws Exception;
    
    List<FixMessage> getSessionMessages(String sessionId);
}
