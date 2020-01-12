package ir.saeidbabaei.bank.moneytransfer;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.glassfish.jersey.servlet.ServletContainer;

import ir.saeidbabaei.bank.moneytransfer.App;
import ir.saeidbabaei.bank.moneytransfer.common.ApplicationConstants;
import ir.saeidbabaei.bank.moneytransfer.controller.AccountController;

public class App 
{
	
    public static Server server;
    
    public static void main( String[] args ) throws Exception
    {

        initServer();
        try {
            server.start();
            server.join();
        } finally {
            server.destroy();
        }
        
    }
    
    /**
     * Initiate a Jetty server
     */
    public static void initServer(){
        if(server == null){
            synchronized (App.class) {
                if(server == null) {
                    QueuedThreadPool threadPool = new QueuedThreadPool(100, 10, 120);
                    server = new Server(threadPool);
                    ServerConnector connector = new ServerConnector(server);
                    connector.setPort(ApplicationConstants.WEB_APPLICATION_PORT);
                    server.setConnectors(new Connector[] { connector });
                    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
                    context.setContextPath("/");
                    server.setHandler(context);
                    ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/*");
                    jerseyServlet.setInitParameter("jersey.config.server.provider.classnames",
                    		AccountController.class.getCanonicalName());
                }
            }
        }
    }
    
}
