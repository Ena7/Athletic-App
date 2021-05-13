package repo;

import domain.AgeGroup;
import domain.Entry;
import domain.Event;
import dtos.ChildNoEventsDTO;
import dtos.EventCountDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EntryRepositoryDB implements IEntryRepository {
    private final DBConnection dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public EntryRepositoryDB(Properties props){
        logger.info("Initializing EntryRepositoryDB with properties: {} ", props);
        dbUtils = new DBConnection(props);
    }

    @Override
    public Long save(Entry entry) {
        logger.traceEntry("Saving task {} ", entry);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("INSERT INTO Entry (IDChild, Event1, Event2, AgeGroup) VALUES (?, ?, ?, ?);")){
            preStmt.setLong(1, entry.getChild().getId());
            preStmt.setString(2, entry.getEvent1().toString());
            preStmt.setString(3, entry.getEvent2().toString());
            preStmt.setString(4, entry.getAgeGroup().toString());
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
    public List<Entry> getAll() {
        return null;
    }

    @Override
    public Entry getById(Long aLong) {
        return null;
    }

    @Override
    public List<EventCountDTO> getEventsNumber() {
        /*
        Returneaza lista cu probele si cati participanti s-au inscrisi la fiecare proba
         */
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<EventCountDTO> events = new ArrayList<>();
        try(PreparedStatement preStmt = con.prepareStatement("SELECT Event, AgeGroup, COUNT(*) AS Number FROM (SELECT Event1 AS Event, AgeGroup FROM Entry UNION ALL SELECT Event2, AgeGroup FROM Entry WHERE Event2 != 'NONE') GROUP BY Event, AgeGroup;")){
            try(ResultSet result = preStmt.executeQuery()){
                while(result.next()){
                    Event event = Event.valueOf(result.getString("Event"));
                    AgeGroup ageGroup = AgeGroup.valueOf(result.getString("AgeGroup"));
                    long number = result.getLong("Number");
                    EventCountDTO eventDTO = new EventCountDTO(event, ageGroup, number);
                    events.add(eventDTO);
                }
            }
        } catch(SQLException e){
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(events);
        return events;
    }


}
