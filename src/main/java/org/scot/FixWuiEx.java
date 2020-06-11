package org.scot;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class FixWuiEx {
    
    FIXService fixService;
    Server     jetty;
    
    public static void main(String[] args) {
        FixWuiEx fixWuiEx = new FixWuiEx();
        fixWuiEx.start();
    }
    
    private void start() {
        // TODO Auto-generated method stub
        try {
            
            fixService = new FIXServiceImpl();
            fixService.init();
            
            ResourceConfig config = new ResourceConfig();
            config.packages("org.scot");
            config.register(new AbstractBinder() {
                
                @Override
                protected void configure() {
                    bind(fixService).to(FIXService.class);
                }
            });
            ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
            contextHandler.setContextPath("/");
            contextHandler.addServlet(new ServletHolder(new ServletContainer(config)), "/api/*");
            
            URI baseUri = UriBuilder.fromUri("http://localhost/").port(9998).build();
            jetty = JettyHttpContainerFactory.createServer(baseUri, false);
            
            jetty.setHandler(contextHandler);
            
            jetty.start();
            
            jetty.join();
            
        } catch ( Exception e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
}
