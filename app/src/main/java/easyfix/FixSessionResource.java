package easyfix;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
@Singleton
public class FixSessionResource {

    private final static Logger log = LoggerFactory.getLogger(FixSessionResource.class);
    
    @Inject
    FIXService fixService;
    
    @GET
    @Path("/sessions")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONDataWrapper<SessionStatus> getSessions() {
        List<SessionStatus> sessionList = fixService.getSessionList();
        
        return new JSONDataWrapper<SessionStatus> (sessionList);
    }
    
}
