package org.scot;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/sessions")
public class FixSessionResource {

	@Inject
	FIXService fixService;
	
		@GET
		@Produces(MediaType.TEXT_PLAIN)
		public String getSessions(@Context ServletContext servletContext) {
			
			List<String> sessionList = fixService.getSessionList();
			
			return sessionList.toString();
		}

}
