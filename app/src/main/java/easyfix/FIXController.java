package easyfix;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
@Singleton
public class FIXController {

    private final static Logger log = LoggerFactory.getLogger(FIXController.class);
    
    @Inject
    FIXService fixService;
    
    @GET
    @Path("/sessions")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONDataWrapper<SessionStatus> getSessions() {
        log.debug("Fetching session list");
        List<SessionStatus> sessionList = fixService.getSessionList();
        return new JSONDataWrapper<SessionStatus> (sessionList);
    }
    
    @POST
    @Path("/messages/send")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendMessage(MessageRequest request) {
        try {
            log.debug("Sending message to session: {}", request.getSessionId());
            fixService.sendMessage(request.getSessionId(), request.getMessage());
            return Response.ok().build();
        } catch (Exception e) {
            log.error("Error sending message", e);
            return Response.status(Response.Status.BAD_REQUEST)
                         .entity(new ErrorResponse(e.getMessage()))
                         .build();
        }
    }
}

class MessageRequest {
    private String sessionId;
    private String message;
    
    // getters and setters
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}

class ErrorResponse {
    private String error;
    
    public ErrorResponse(String error) {
        this.error = error;
    }
    
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}
