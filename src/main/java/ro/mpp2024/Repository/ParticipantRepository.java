package ro.mpp2024.Repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.Domain.Angajat;
import ro.mpp2024.Domain.Participant;
import ro.mpp2024.Validator.AbstractValidator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class ParticipantRepository implements ParticipantAbstractRepository {

    private JdbcUtils dbUtils;
    private AbstractValidator<Participant> validator;
    private static final Logger logger= LogManager.getLogger();
    public ParticipantRepository(Properties props, AbstractValidator<Participant> validator) {
        logger.info("Initializing ParticipantRepository with properties: {} ",props);
        this.validator = validator;
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Optional<Participant> add(Participant participant) {
        logger.traceEntry("Saving participant {}",participant);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("insert into Participant(nume, prenume, varsta) values (?,?,?)"))
        {
            validator.validate(participant);

            preparedStatement.setString(1, participant.getNume());
            preparedStatement.setString(2, participant.getPrenume());
            preparedStatement.setInt(3, participant.getVarsta());

            int rows = preparedStatement.executeUpdate();
            logger.trace("Saved {} instances", rows);
            if(rows == 0)
                return Optional.of(participant);
            return Optional.empty();

        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Participant> update(Participant participant) {
        logger.traceEntry("Updating participant {}",participant);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("update Participant set nume=?, prenume=?, varsta=? where id_participant=?"))
        {
            validator.validate(participant);

            preparedStatement.setString(1, participant.getNume());
            preparedStatement.setString(2, participant.getPrenume());
            preparedStatement.setInt(3, participant.getVarsta());
            preparedStatement.setInt(3, participant.getId());

            int rows = preparedStatement.executeUpdate();
            logger.trace("Updated {} instances", rows);
            if(rows == 0)
                return Optional.of(participant);
            return Optional.empty();

        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Participant> deleteById(Integer id) {
        logger.traceEntry("Deleting participant with id {}",id);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("delete from Participant where id_participant=?"))
        {
            Optional<Participant> participant = findById(id);
            preparedStatement.setInt(1, id);

            int rows = preparedStatement.executeUpdate();
            logger.trace("Deleted {} instances", rows);
            return participant;

        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Participant> findById(Integer id) {
        logger.traceEntry("Finding participant with id {}", id);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from Participant where id_participant = ?"))
        {
            preparedStatement.setInt(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    Integer idParticipant = resultSet.getInt("id_participant");
                    String nume = resultSet.getString("nume");
                    String prenume = resultSet.getString("prenume");
                    Integer varsta = resultSet.getInt("varsta");

                    Participant participant = new Participant(nume, prenume, varsta);
                    participant.setId(idParticipant);
                    logger.traceExit("Found participant {}",participant);
                    return Optional.of(participant);
                }
                logger.traceExit("No participant with id {} was found",id);
                return Optional.empty();
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Participant> findAll() {
        logger.traceEntry();
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from Participant"))
        {
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                List<Participant> participanti = new ArrayList<>();
                while(resultSet.next()){
                    Integer idParticipant = resultSet.getInt("id_participant");
                    String nume = resultSet.getString("nume");
                    String prenume = resultSet.getString("prenume");
                    Integer varsta = resultSet.getInt("varsta");

                    Participant participant = new Participant(nume, prenume, varsta);
                    participant.setId(idParticipant);

                    participanti.add(participant);
                }
                logger.traceExit("Participanti: {}", participanti);
                return participanti;
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Participant> getAllParticipantiFromProba(Integer probaId) {
        return null;
    }
}
