package easyfix;

import java.net.URI;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.core.UriBuilder;

public class EasyFIX extends AppCommon {
    private final static Logger log = LoggerFactory.getLogger(EasyFIX.class);

    FIXService fixService;
    protected Server jetty;

    public static void main(String[] args) {
        log.info("Application start.");

        try {

            EasyFIX app = new EasyFIX();
            app.init();
            app.start();

        } catch (Exception e) {
            log.error("Error", e);
        }

        log.info("Application end.");

        System.exit(0);
    }

    @Override
    protected void init() throws Exception {
        super.init();
        fixService = new FIXServiceImpl();

        ResourceConfig config = new ResourceConfig();
        config.packages("easyfix");
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
                .setBaseResource(Resource.newResource(ClassLoader.getSystemClassLoader().getResource("static")));
        staticResourceHandler.setWelcomeFiles(new String[] { "index.html" });
        ContextHandler staticContextHandler = new ContextHandler("/");
        staticContextHandler.setHandler(staticResourceHandler);

        HandlerList handlerList = new HandlerList();
        handlerList.addHandler(staticContextHandler);
        handlerList.addHandler(jerseyServletContextHandler);

        URI baseUri = UriBuilder.fromUri("http://localhost/").port(Integer.parseInt(properties.getProperty("wui.port")))
                .build();
        jetty = JettyHttpContainerFactory.createServer(baseUri, false);

        jetty.setHandler(handlerList);
    }

    protected void start() throws Exception {

        fixService.init();

        jetty.start();
        jetty.join();
    }
}
