package ro.mpp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp.Domain.*;
import ro.mpp.Validator.AbstractValidator;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class ParticipareRepository implements ParticipareAbstractRepository {
    private JdbcUtils dbUtils;
    private AbstractValidator<Participare> validator;
    private static final Logger logger= LogManager.getLogger();
    public ParticipareRepository(Properties props, AbstractValidator<Participare> validator) {
        logger.info("Initializing ParticipareRepository with properties: {} ",props);
        this.validator = validator;
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Optional<Participare> add(Participare participare) {
        logger.traceEntry("Saving participare {}",participare);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("insert into Participare(id_participant, id_proba, data_inscrierii) values (?,?,?)"))
        {
            validator.validate(participare);

            preparedStatement.setInt(1, participare.getId().getId1().getId());
            preparedStatement.setInt(2, participare.getId().getId2().getId());
            LocalDateTime dateTime = participare.getDataInscrierii().atStartOfDay();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
            preparedStatement.setString(3, dateTime.format(dateTimeFormatter));

            int rows = preparedStatement.executeUpdate();
            logger.trace("Saved {} instances", rows);
            if(rows == 0)
                return Optional.of(participare);
            return Optional.empty();

        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Participare> update(Participare participare) {
        logger.traceEntry("Updating participare {}",participare);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("update Participare set data_inscrierii=? where id_participant=? and id_proba=?"))
        {
            validator.validate(participare);

            LocalDateTime dateTime = participare.getDataInscrierii().atStartOfDay();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
            preparedStatement.setTimestamp(1, Timestamp.valueOf(dateTime.format(dateTimeFormatter)));
            preparedStatement.setInt(2, participare.getId().getId1().getId());
            preparedStatement.setInt(3, participare.getId().getId2().getId());

            int rows = preparedStatement.executeUpdate();
            logger.trace("Updated {} instances", rows);
            if(rows == 0)
                return Optional.of(participare);
            return Optional.empty();

        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Participare> deleteById(Pair<Participant, Proba> id) {
        logger.traceEntry("Deleting participare with id {}",id);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("delete from Participare  where id_participant=? and id_proba=?"))
        {
            Optional<Participare> participare = findById(id);

            preparedStatement.setInt(1, id.getId1().getId());
            preparedStatement.setInt(2, id.getId2().getId());

            int rows = preparedStatement.executeUpdate();
            logger.trace("Deleted {} instances", rows);
            return participare;

        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Participare> findById(Pair<Participant, Proba> id) {
        logger.traceEntry("Finding participare with id {}", id);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from Participare where id_participant = ? and id_proba = ?"))
        {
            preparedStatement.setInt(1, id.getId1().getId());
            preparedStatement.setInt(2, id.getId2().getId());
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    Integer idParticipant = resultSet.getInt("id_participant");
                    Integer idProba = resultSet.getInt("id_proba");
                    LocalDate dataInscrierii = resultSet.getTimestamp("data_inscrierii").toLocalDateTime().toLocalDate();

                    Participare participare = new Participare(dataInscrierii);
                    participare.setId(new Pair<>(findParticipantById(idParticipant), findProbaById(idProba)));
                    logger.traceExit("Found participare {}",participare);
                    return Optional.of(participare);
                }
                logger.traceExit("No participare with id {} was found",id);
                return Optional.empty();
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    private Participant findParticipantById(Integer id) {
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
                    return participant;
                }
                logger.traceExit("No participant with id {} was found",id);
                return null;
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }
    private Proba findProbaById(Integer id) {
        logger.traceEntry("Finding proba with id {}", id);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from Proba where id_proba = ?"))
        {
            preparedStatement.setInt(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    Integer idProba = resultSet.getInt("id_proba");
                    Integer distanta = resultSet.getInt("distanta");
                    StilInot stilInot = StilInot.valueOf(resultSet.getString("stil"));
                    LocalDateTime dataDesfasurarii = resultSet.getTimestamp("data_desfasurarii").toLocalDateTime();
                    String locatie = resultSet.getString("locatie");

                    Proba proba = new Proba(distanta, stilInot, dataDesfasurarii, locatie);
                    proba.setId(idProba);
                    logger.traceExit("Found proba {}",proba);
                    return proba;
                }
                logger.traceExit("No proba with id {} was found",id);
                return null;
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<Participare> findAll() {
        logger.traceEntry();
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from Participare"))
        {
            List<Participare> participari = new ArrayList<>();
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    Integer idParticipant = resultSet.getInt("id_participant");
                    Integer idProba = resultSet.getInt("id_proba");
                    LocalDate dataInscrierii = resultSet.getTimestamp("data_inscrierii").toLocalDateTime().toLocalDate();

                    Participare participare = new Participare(dataInscrierii);
                    participare.setId(new Pair<>(findParticipantById(idParticipant), findProbaById(idProba)));

                    participari.add(participare);
                }
                logger.traceExit("Participari: {}", participari);
                return participari;
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }
}
