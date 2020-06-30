package org.scot;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.flywaydb.core.Flyway;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class FixWuiEx {
    
    ComboPooledDataSource datasource;
    Jdbi                  jdbi;
    
    FIXService            fixService;
    Server                jetty;
    
    public static void main(String[] args) {
        FixWuiEx fixWuiEx = new FixWuiEx();
        fixWuiEx.init();
        // fixWuiEx.start();
    }
    
    private void init() {
        try {
            datasource = new ComboPooledDataSource();
            
            datasource.setDriverClass("org.postgresql.Driver");
            // loads the jdbc driver
            datasource.setJdbcUrl("jdbc:postgresql://18.183.35.88/devdb");
            datasource.setUser("dev");
            datasource.setPassword("dev");
            
            jdbi = Jdbi.create(datasource)
                    .installPlugin(new SqlObjectPlugin())
                    .installPlugin(new PostgresPlugin());
            Handle handle = jdbi.open();
            handle.execute("SELECT 1;");
            handle.close();
            
            Flyway flyway = Flyway.configure()
                    .dataSource(datasource)
                    .load();
            flyway.migrate();
            
        } catch ( Exception e ) {
            e.printStackTrace();
        }
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
            ServletContextHandler jerseyServletContextHandler = new ServletContextHandler(
                    ServletContextHandler.SESSIONS);
            jerseyServletContextHandler.setContextPath("/");
            jerseyServletContextHandler.addServlet(new ServletHolder(new ServletContainer(config)), "/api/*");
            
            ResourceHandler staticResourceHandler = new ResourceHandler();
            staticResourceHandler.setDirectoriesListed(false);
            staticResourceHandler
                    .setBaseResource(Resource.newResource(FixWuiEx.class.getClassLoader().getResource("static")));
            staticResourceHandler.setWelcomeFiles(new String[] { "Welcome.html" });
            ContextHandler staticContextHandler = new ContextHandler("/static");
            staticContextHandler.setHandler(staticResourceHandler);
            
            HandlerList handlerList = new HandlerList();
            handlerList.addHandler(staticContextHandler);
            handlerList.addHandler(jerseyServletContextHandler);
            
            URI baseUri = UriBuilder.fromUri("http://localhost/").port(9998).build();
            jetty = JettyHttpContainerFactory.createServer(baseUri, false);
            
            jetty.setHandler(handlerList);
            
            jetty.start();
            
            jetty.join();
            
        } catch ( Exception e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
}
