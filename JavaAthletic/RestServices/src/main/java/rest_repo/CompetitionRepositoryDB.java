package rest_repo;

import org.springframework.stereotype.Component;
import rest_domain.Competition;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class CompetitionRepositoryDB implements ICompetitionRepository {
    private final DBConnection dbUtils;

    public CompetitionRepositoryDB(Properties props){
        dbUtils = new DBConnection(props);
    }

    @Override
    public void save(Competition competition) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("INSERT INTO Competition (Event, AgeGroup) VALUES (?, ?);")){
            preStmt.setString(1, competition.getEvent());
            preStmt.setString(2, competition.getAgeGroup());
            preStmt.executeUpdate();
        } catch (SQLException ex){
            System.err.println("Error DB " + ex);
        }
    }

    @Override
    public void update(Competition competition) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("UPDATE Competition SET Event = ?, AgeGroup = ? WHERE ID = ?")){
            preStmt.setString(1, competition.getEvent());
            preStmt.setString(2, competition.getAgeGroup());
            preStmt.setLong(3, competition.getId());
            preStmt.executeUpdate();
        } catch (SQLException ex){
            System.err.println("Error DB " + ex);
        }
    }

    @Override
    public void delete(Long id) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("DELETE FROM Competition WHERE ID = ?;")){
            preStmt.setLong(1, id);
            preStmt.executeUpdate();
        } catch(SQLException e){
            System.err.println("Error DB " + e);
        }
    }

    @Override
    public Competition getById(Long id) {
        Connection con = dbUtils.getConnection();
        Competition competition = null;
        try(PreparedStatement preStmt = con.prepareStatement("SELECT * FROM Competition WHERE ID = ?;")){
            preStmt.setLong(1, id);
            try(ResultSet result = preStmt.executeQuery()){
                while(result.next()){
                    long idc = result.getLong("ID");
                    String event = result.getString("Event");
                    String ageGroup = result.getString("AgeGroup");
                    competition = new Competition(event, ageGroup);
                    competition.setId(idc);
                }
            }
        } catch(SQLException e){
            System.err.println("Error DB " + e);
        }
        return competition;
    }

    @Override
    public Competition[] getAll() {
        Connection con = dbUtils.getConnection();
        List<Competition> competitions = new ArrayList<>();
        try(PreparedStatement preStmt = con.prepareStatement("SELECT * FROM Competition;")){
            try(ResultSet result = preStmt.executeQuery()){
                while(result.next()){
                    long idc = result.getLong("ID");
                    String event = result.getString("Event");
                    String ageGroup = result.getString("AgeGroup");
                    Competition competition = new Competition(event, ageGroup);
                    competition.setId(idc);
                    competitions.add(competition);
                }
            }
        } catch(SQLException e){
            System.err.println("Error DB " + e);
        }
        return competitions.toArray(new Competition[0]);
    }
}
