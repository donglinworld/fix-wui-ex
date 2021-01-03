package org.scot;

import java.io.IOException;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;

/**
 * @author Pavel Bucek (pavel.bucek at oracle.com)
 */
@Path("/sse")
public class ServerSentEventsResource {
    
    private static volatile EventOutput eventOutput = new EventOutput();
    
    @GET
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    public EventOutput getMessageQueue() {
        return eventOutput;
    }
    
    @POST
    public void addMessage(final String message) throws IOException {
        final EventOutput localOutput = eventOutput;
        if ( localOutput != null ) {
            eventOutput.write(new OutboundEvent.Builder().name("custom-message").data(String.class, message).build());
        }
    }
    
    @DELETE
    public void close() throws IOException {
        final EventOutput localOutput = eventOutput;
        if ( localOutput != null ) {
            eventOutput.close();
        }
        ServerSentEventsResource.setEventOutput(new EventOutput());
    }
    
    @GET
    @Path("domains/{id}")
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    public EventOutput startDomain(@PathParam("id") final String id) {
        
        final EventOutput seq = new EventOutput();
        
        new Thread() {
            @Override
            public void run() {
                try {
                    seq.write(new OutboundEvent.Builder().name("domain-progress")
                            .data(String.class, "starting domain " + id + " ...").build());
                    System.out.println("starting domain");
                    
                    int percent = 0;
                    
                    for ( percent = 0; percent <= 100; percent++ ) {
                        Thread.sleep(1000);
                        seq.write(
                                new OutboundEvent.Builder().name("domain-progress").data(String.class, percent + "%")
                                        .build());
                        System.out.println(percent + "%");
                    }
                    
                    seq.close();
                    
                } catch ( final InterruptedException | IOException e ) {
                    System.out.println("end from clent side.");
                    e.printStackTrace();
                } finally {
                    try {
                        seq.close();
                    } catch ( IOException e ) {
                        // ignore;
                    }
                }
            }
        }.start();
        
        return seq;
    }
    
    private static void setEventOutput(final EventOutput eventOutput) {
        ServerSentEventsResource.eventOutput = eventOutput;
    }
}