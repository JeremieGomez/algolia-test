package org.jgo.testalgo;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.jgo.testalgo.timestamp.TimestampParseException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App
{

    private static final String ROOT_PATH = "/1/queries/";

    public static QueriesTree QUERIES_TREE_INSTANCE;
    private static Logger logger = Logger.getLogger(App.class.getName());

    public static void main( String[] args ) {

        if (args.length < 1) {
            logger.log(Level.SEVERE, "First argument must be the path to your tsv file");
            System.exit(1);
        }

        String tsvPath = args[0];
        // Will exit application if the tree cannot be built
        buildQueriesTree(tsvPath);

        runServer(8080);
    }

    public static void buildQueriesTree(String tsvPath) {

        QueriesTree queriesTree = null;

        logger.log(Level.INFO, "Building queries tree...");
        try {
            queriesTree = new QueriesTree(tsvPath);
        }
        catch (IOException | TimestampParseException e) {
            logger.log(Level.SEVERE, "Error building tree: " + e.toString());
            System.exit(1);
        }
        logger.log(Level.INFO, "Tree finished building.");

        QUERIES_TREE_INSTANCE = queriesTree;
    }

    public static void runServer(Integer port) {

        // Start the jetty server that uses jersey (from http://zetcode.com/articles/jerseyembeddedjetty/)

        Server server = new Server(port);

        ServletContextHandler ctx =
                new ServletContextHandler(ServletContextHandler.NO_SESSIONS);

        ctx.setContextPath("/");
        server.setHandler(ctx);

        ServletHolder serHol = ctx.addServlet(ServletContainer.class, ROOT_PATH+"*");
        serHol.setInitOrder(1);
        serHol.setInitParameter("jersey.config.server.provider.packages",
                "org.jgo.testalgo.api_resources");

        try {
            server.start();
            server.join();
        } catch (Exception ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            server.destroy();
        }
    }

    public static QueriesTree getQueriesTree() {
        return QUERIES_TREE_INSTANCE;
    }

}