package repo;

import domain.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class UserRepositoryORM implements IUserRepository {

    private final SessionFactory sessionFactory;

    public UserRepositoryORM(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Long save(User user) {
        try(Session session = sessionFactory.openSession()) {
            Transaction trans = null;
            try {
                trans = session.beginTransaction();
                session.save(user);
                trans.commit();
            }
            catch (RuntimeException ex) {
                if (trans != null) {
                    trans.rollback();
                }
            }
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public User getById(Long id) {
        return null;
    }

    @Override
    public User getByUsername(String s) {
        List<User> userList = null;
        try(Session session = sessionFactory.openSession()) {
            Transaction trans = null;
            try {
                trans = session.beginTransaction();
                userList = session.createQuery("FROM User WHERE username = :username", User.class).setParameter("username", s).list();
                trans.commit();
            }
            catch (RuntimeException ex) {
                if (trans != null) {
                    trans.rollback();
                }
            }
        }
        assert userList != null;
        if (!userList.isEmpty()) {
            return userList.get(0);
        }
        return null;
    }

}
