package org.scot;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.eclipse.jetty.server.Server;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class FixWuiEx {

	FIXService fixService;
	Server jetty;

	public static void main(String[] args) {
			FixWuiEx fixWuiEx = new FixWuiEx();
			fixWuiEx.start();
	}

	private void start() {
		// TODO Auto-generated method stub
		try {

			fixService = new FIXServiceImpl();
			fixService.init();

			URI baseUri = UriBuilder.fromUri("http://localhost/").port(9998).build();
			ResourceConfig config = new ResourceConfig();
			config.packages("org.scot");
			config.register(new AbstractBinder(){

				@Override
				protected void configure() {
					bind(fixService).to(FIXService.class);
				}
			});
			
			
			jetty = JettyHttpContainerFactory.createServer(baseUri, config);
			jetty.start();

			System.out.println("press <enter> to quit");
			System.in.read();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
