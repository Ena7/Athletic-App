package utils;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class ORMSessionFactory {

    private SessionFactory sessionFactory = null;

    private SessionFactory initialize() {
        SessionFactory ses = null;
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            ses = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.err.println("Exception " + e);
            StandardServiceRegistryBuilder.destroy(registry);
        }
        return ses;
    }

    public SessionFactory getSessionFactory() {
        try {
            if (sessionFactory == null || sessionFactory.isClosed()) {
                sessionFactory = initialize();
            }
        }
        catch (Exception e) {
            System.err.println("Exception " + e);
        }
        return sessionFactory;
    }

    public void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
