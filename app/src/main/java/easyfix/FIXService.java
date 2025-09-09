package easyfix;

import java.util.List;

public interface FIXService {
    
    void init();
    
    List<SessionStatus> getSessionList();
    
}
