package ro.mpp2024.Repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.Domain.Angajat;
import ro.mpp2024.Domain.Pair;
import ro.mpp2024.Domain.Participant;
import ro.mpp2024.Domain.Participare;
import ro.mpp2024.Validator.AbstractValidator;

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

            preparedStatement.setInt(1, participare.getId().getId1());
            preparedStatement.setInt(2, participare.getId().getId2());
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
            preparedStatement.setInt(2, participare.getId().getId1());
            preparedStatement.setInt(3, participare.getId().getId2());

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
    public Optional<Participare> deleteById(Pair<Integer, Integer> id) {
        logger.traceEntry("Deleting participare with id {}",id);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("delete from Participare  where id_participant=? and id_proba=?"))
        {
            Optional<Participare> participare = findById(id);

            preparedStatement.setInt(1, id.getId1());
            preparedStatement.setInt(2, id.getId2());

            int rows = preparedStatement.executeUpdate();
            logger.trace("Deleted {} instances", rows);
            return participare;

        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Participare> findById(Pair<Integer, Integer> id) {
        logger.traceEntry("Finding participare with id {}", id);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from Participare where id_participant = ? and id_proba = ?"))
        {
            preparedStatement.setInt(1, id.getId1());
            preparedStatement.setInt(2, id.getId2());
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    Integer idParticipant = resultSet.getInt("id_participant");
                    Integer idProba = resultSet.getInt("id_proba");
                    LocalDate dataInscrierii = resultSet.getTimestamp("data_inscrierii").toLocalDateTime().toLocalDate();

                    Participare participare = new Participare(dataInscrierii);
                    participare.setId(new Pair<>(idParticipant, idProba));
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
                    participare.setId(new Pair<>(idParticipant, idProba));

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
