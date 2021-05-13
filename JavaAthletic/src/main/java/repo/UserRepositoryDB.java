package repo;

import domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class UserRepositoryDB implements IUserRepository {
    private final DBConnection dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public UserRepositoryDB(Properties props){
        logger.info("Initializing UserRepositoryDB with properties: {} ", props);
        dbUtils = new DBConnection(props);
    }

    @Override
    public Long save(User user){
        logger.traceEntry("Saving task {} ", user);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("INSERT INTO User (Username, Password) VALUES (?, ?);")){
            preStmt.setString(1, user.getUsername());
            preStmt.setString(2, user.getPassword());
            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
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
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        User user = null;
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM User WHERE Username = ?;")) {
            preStmt.setString(1, s);
            try (ResultSet result = preStmt.executeQuery()) {
                if (!result.next()) {
                    logger.traceExit(null);
                    return null;
                }
                Long id = result.getLong("ID");
                String username = result.getString("Username");
                String password = result.getString("Password");
                user = new User(username, password);
                user.setId(id);
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(user);
        return user;
    }
}
