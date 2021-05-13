package repo;

import domain.AgeGroup;
import domain.Child;
import domain.Event;
import dtos.ChildNoEventsDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ChildRepositoryDB implements IChildRepository{
    private final DBConnection dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public ChildRepositoryDB(Properties props){
        logger.info("Initializing ChildRepositoryDB with properties: {} ", props);
        dbUtils = new DBConnection(props);
    }

    @Override
    public Long save(Child child) {
        logger.traceEntry("Saving task {} ", child);
        long generatedID = -1;
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("INSERT INTO Child (Name, Age) VALUES (?, ?);", Statement.RETURN_GENERATED_KEYS)){
            preStmt.setString(1, child.getName());
            preStmt.setInt(2, child.getAge());
            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);

            try (ResultSet generatedKeys = preStmt.getGeneratedKeys()){
                if (generatedKeys.next()){
                    generatedID = generatedKeys.getLong(1);
                }
                else{
                    throw new SQLException("Failed. No ID obtained.");
                }
            }

        } catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
        return generatedID;
    }

    @Override
    public List<Child> getAll() {
        return null;
    }

    @Override
    public Child getById(Long id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        Child child = null;
        try(PreparedStatement preStmt = con.prepareStatement("SELECT * FROM Child WHERE ID = ?;")){
            preStmt.setLong(1, id);
            try(ResultSet result = preStmt.executeQuery()){
                while(result.next()){
                    long idc = result.getLong("ID");
                    String name = result.getString("Name");
                    int age = result.getInt("Age");
                    child = new Child(name, age);
                    child.setId(idc);
                }
            }
        } catch(SQLException e){
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(child);
        return child;
    }

    @Override
    public List<ChildNoEventsDTO> getChildrenNoEvents(Event event, AgeGroup ageGroup) {
        /*
        Returneaza lista cu participantii de la un anumita proba si un anumita categorie de varsta, plus
        la cate probe s-a inscris
         */
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<ChildNoEventsDTO> children = new ArrayList<>();
        try(PreparedStatement preStmt = con.prepareStatement("SELECT C.ID, C.Name, C.Age, CASE WHEN Event2 != 'NONE' THEN 2 ELSE 1 END AS NoEvents FROM Entry E INNER JOIN Child C on C.ID = E.IDChild WHERE (Event1 = ? OR Event2 = ?) AND AgeGroup = ?;")){
            preStmt.setString(1, event.toString());
            preStmt.setString(2, event.toString());
            preStmt.setString(3, ageGroup.toString());
            try(ResultSet result = preStmt.executeQuery()){
                while(result.next()){
                    long id = result.getLong("ID");
                    String name = result.getString("Name");
                    int age = result.getInt("Age");
                    int noEvents = result.getInt("NoEvents");
                    Child child = new Child(name, age);
                    child.setId(id);
                    ChildNoEventsDTO childDTO = new ChildNoEventsDTO(child, noEvents);
                    children.add(childDTO);
                }
            }
        } catch(SQLException e){
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(children);
        return children;
    }

    @Override
    public List<Child> getUnsignedChildren() {
        /*
        Returneaza o lista cu copiii care nu sunt inscrisi
         */
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Child> children = new ArrayList<>();
        try(PreparedStatement preStmt = con.prepareStatement("SELECT * FROM Child c LEFT JOIN Entry e ON c.ID = e.IDChild WHERE IDChild IS NULL;")){
            try(ResultSet result = preStmt.executeQuery()){
                while(result.next()){
                    long id = result.getLong("ID");
                    String name = result.getString("Name");
                    int age = result.getInt("Age");
                    Child child = new Child(name, age);
                    child.setId(id);
                    children.add(child);
                }
            }
        } catch(SQLException e){
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(children);
        return children;
    }
}
