import org.hibernate.SessionFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import repo.*;
import utils.ORMSessionFactory;

import java.io.IOException;
import java.util.Properties;

public class StartServer {

    private static void ormMain() {
        ORMSessionFactory orm = new ORMSessionFactory();
        try {
            SessionFactory sessionFactory = orm.getSessionFactory();
            int defaultPort = 55555;

            Properties props = new Properties();
            try {
                props.load(StartServer.class.getResourceAsStream("/server.properties"));
                System.out.println("Server properties set.");
                props.list(System.out);
            }
            catch (IOException e) {
                System.err.println("Cannot find server.properties\n" + e);
                return;
            }
            IUserRepository userRepo = new UserRepositoryORM(sessionFactory);
            IChildRepository childRepo = new ChildRepositoryDB(props);
            IEntryRepository entryRepo = new EntryRepositoryDB(props);
            IServiceServer service = new ServiceServer(userRepo, childRepo, entryRepo);

            int serverPort = defaultPort;
            try {
                serverPort = Integer.parseInt(props.getProperty("server.port"));
            }
            catch (NumberFormatException e) {
                System.err.println("Wrong port number\n" + e.getMessage());
                System.err.println("Using default port " + defaultPort);
            }
            System.out.println("Starting server on port " + serverPort);

            AbstractServer server = new ConcurrentServer(serverPort, service);
//        AbstractServer server = new ProtoConcurrentServer(serverPort, service);

            try {
                server.start();
            }
            catch (Exception e) {
                System.err.println("Error starting server\n" + e.getMessage());
            }
            finally {
                try {
                    server.stop();
                }
                catch (Exception e) {
                    System.err.println("Error stopping server\n" + e.getMessage());
                }
            }
        }
        catch (Exception e) {
            System.err.println("Exception " + e);
            e.printStackTrace();
        }
        finally {
            orm.close();
        }
    }

    private static void springMain() {
        new ClassPathXmlApplicationContext("classpath:spring_server.xml");
        System.out.println("Server up and running!");
    }

    private static void normalOrProtoMain() {
        int defaultPort = 55555;

        Properties props = new Properties();
        try {
            props.load(StartServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set.");
            props.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.properties\n" + e);
            return;
        }
        IUserRepository userRepo = new UserRepositoryDB(props);
        IChildRepository childRepo = new ChildRepositoryDB(props);
        IEntryRepository entryRepo = new EntryRepositoryDB(props);
        IServiceServer service = new ServiceServer(userRepo, childRepo, entryRepo);

        int serverPort = defaultPort;
        try {
            serverPort = Integer.parseInt(props.getProperty("server.port"));
        } catch (NumberFormatException e) {
            System.err.println("Wrong port number\n" + e.getMessage());
            System.err.println("Using default port " + defaultPort);
        }
        System.out.println("Starting server on port " + serverPort);

        AbstractServer server = new ConcurrentServer(serverPort, service);
//        AbstractServer server = new ProtoConcurrentServer(serverPort, service);

        try {
            server.start();
        } catch (Exception e) {
            System.err.println("Error starting server\n" + e.getMessage());
        } finally {
            try {
                server.stop();
            } catch (Exception e) {
                System.err.println("Error stopping server\n" + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
//        springMain();
//        normalOrProtoMain();
        ormMain();
    }
}
