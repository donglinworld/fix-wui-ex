package easyfix;

import java.io.InputStream;
import java.util.Properties;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class AppCommon {
    private final static Logger     log = LoggerFactory.getLogger(AppCommon.class);
    
    protected ComboPooledDataSource datasource;
    protected Jdbi                  jdbi;
    protected Properties            properties;
    
    protected void init() throws Exception {
        
        InputStream propinput = ClassLoader.getSystemResourceAsStream("app.properties");
        
        properties = new Properties(System.getProperties());
        
        properties.load(propinput);
        
        String host = properties.getProperty("db.host");
        String dbname = properties.getProperty("db.dbname");
        String user = properties.getProperty("db.user");
        String pwd = properties.getProperty("db.pwd");
        
        datasource = new ComboPooledDataSource();
        
        datasource.setDriverClass("org.postgresql.Driver");
        // loads the jdbc driver
        String dbUrl = "jdbc:postgresql://" + host + "/" + dbname;
        datasource.setJdbcUrl(dbUrl);
        datasource.setUser(user);
        datasource.setPassword(pwd);
        
        log.info("connect db by: " + dbUrl + " " + user);
        
        jdbi = Jdbi.create(datasource)
                .installPlugin(new SqlObjectPlugin())
                .installPlugin(new PostgresPlugin());
        Handle handle = jdbi.open();
        handle.execute("SELECT 1;");
        handle.close();
    };
}
