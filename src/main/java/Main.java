import com.ravishandev.epicreads.config.AppConfig;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.glassfish.jersey.servlet.ServletContainer;

import java.io.File;

public class Main {

    private static final int PORT = 8080;
    private static final String CONTEXT_PATH = "/epicreads";

    public static void main(String[] args) {
        try {
            Tomcat tomcat = new Tomcat();
            tomcat.setPort(PORT);
            tomcat.getConnector();

            Context context = tomcat.addWebapp(CONTEXT_PATH, new File("src/main/webapp").getAbsolutePath());
            Tomcat.addServlet(context, "JerseyServlet", new ServletContainer(new AppConfig()));
            context.addServletMappingDecoded("/api/*", "JerseyServlet");

            tomcat.start();
            System.out.println("App URL:  http://localhost:8080"+ CONTEXT_PATH);
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            throw new RuntimeException("Server Starting Failed" + e.getMessage());
        }
    }
}
