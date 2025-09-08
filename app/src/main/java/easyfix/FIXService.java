package easyfix;

import java.util.List;

public interface FIXService {
    
    void init();
    
    List<String> getSessionList();
    
}
