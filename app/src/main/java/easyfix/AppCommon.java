package easyfix;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppCommon {
    private final static Logger     log = LoggerFactory.getLogger(AppCommon.class);

    protected Properties            properties;
    
    protected void init() throws Exception {
        
        InputStream propinput = ClassLoader.getSystemResourceAsStream("app.properties");
        
        properties = new Properties(System.getProperties());
        
        properties.load(propinput);

        log.info("Loaded properties: " + properties);
    };
}
