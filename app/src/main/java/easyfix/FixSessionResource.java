package easyfix;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/")
@Singleton
public class FixSessionResource {
    
    @Inject
    FIXService fixService;
    
    @GET
    @Path("/sessions")
    @Produces(MediaType.TEXT_PLAIN)
    public String getSessions() {
        
        List<String> sessionList = fixService.getSessionList();
        
        return sessionList.toString();
    }
    
}
