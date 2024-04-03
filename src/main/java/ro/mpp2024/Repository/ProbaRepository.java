package ro.mpp2024.Repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.Domain.Angajat;
import ro.mpp2024.Domain.Proba;
import ro.mpp2024.Domain.ProbaDTO;
import ro.mpp2024.Domain.StilInot;
import ro.mpp2024.Validator.AbstractValidator;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class ProbaRepository implements ProbaAbstractRepository{
    private JdbcUtils dbUtils;
    private AbstractValidator<Proba> validator;
    private static final Logger logger= LogManager.getLogger();

    public ProbaRepository(Properties props, AbstractValidator<Proba> validator)
    {
        logger.info("Initializing ProbaRepository with properties: {} ",props);
        this.validator = validator;
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Optional<Proba> add(Proba proba) {
        logger.traceEntry("Saving proba {}",proba);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("insert into Proba(distanta, stil, data_desfasurarii, locatie) values (?,?,?,?)"))
        {
            validator.validate(proba);

            preparedStatement.setInt(1, proba.getDistanta());
            preparedStatement.setString(2, proba.getStil().toString());
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
            preparedStatement.setTimestamp(3, Timestamp.valueOf(proba.getDataDesfasurarii().format(dateTimeFormatter)));
            preparedStatement.setString(4, proba.getLocatie());

            int rows = preparedStatement.executeUpdate();
            logger.trace("Saved {} instances", rows);
            if(rows == 0)
                return Optional.of(proba);
            return Optional.empty();

        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Proba> update(Proba proba) {
        logger.traceEntry("Updating proba {}",proba);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("update Proba set distanta=?, stil=?, data_desfasurarii=?, locatie=? where id_proba = ?"))
        {
            validator.validate(proba);

            preparedStatement.setInt(1, proba.getDistanta());
            preparedStatement.setString(2, proba.getStil().toString());
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
            preparedStatement.setTimestamp(3, Timestamp.valueOf(proba.getDataDesfasurarii().format(dateTimeFormatter)));
            preparedStatement.setString(4, proba.getLocatie());
            preparedStatement.setInt(5, proba.getId());

            int rows = preparedStatement.executeUpdate();
            logger.trace("Updated {} instances", rows);
            if(rows == 0)
                return Optional.of(proba);
            return Optional.empty();

        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Proba> deleteById(Integer id) {
        logger.traceEntry("Deleting proba with id {}",id);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("delete from Proba where id_proba = ?"))
        {
            Optional<Proba> proba = findById(id);

            preparedStatement.setInt(1, id);

            int rows = preparedStatement.executeUpdate();
            logger.trace("Deleted {} instances", rows);
            return proba;

        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Proba> findById(Integer id) {
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
                    return Optional.of(proba);
                }
                logger.traceExit("No proba with id {} was found",id);
                return Optional.empty();
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Proba> findAll() {
        logger.traceEntry();
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from Proba"))
        {
            List<Proba> probe = new ArrayList<>();
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    Integer idProba = resultSet.getInt("id_proba");
                    Integer distanta = resultSet.getInt("distanta");
                    StilInot stilInot = StilInot.valueOf(resultSet.getString("stil"));
                    LocalDateTime dataDesfasurarii = resultSet.getTimestamp("data_desfasurarii").toLocalDateTime();
                    String locatie = resultSet.getString("locatie");

                    Proba proba = new Proba(distanta, stilInot, dataDesfasurarii, locatie);
                    proba.setId(idProba);
                    probe.add(proba);
                }
                logger.traceExit("Probe: {}", probe);
                return probe;
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ProbaDTO> getAllProbaAfterToday() {
        logger.traceEntry("Entry get all proba after today");
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select P.*,count(pa.id_participant) as nrInregistrari from Proba P left join Participare Pa on Pa.id_proba = P.id_proba where date(P.data_desfasurarii) > current_date group by P.id_proba"))
        {
            List<ProbaDTO> probe = new ArrayList<>();
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    Integer idProba = resultSet.getInt("id_proba");
                    Integer distanta = resultSet.getInt("distanta");
                    StilInot stilInot = StilInot.valueOf(resultSet.getString("stil"));
                    LocalDateTime dataDesfasurarii = resultSet.getTimestamp("data_desfasurarii").toLocalDateTime();
                    String locatie = resultSet.getString("locatie");
                    Integer nrInregistrari = resultSet.getInt("nrInregistrari");

                    ProbaDTO proba = new ProbaDTO(idProba,distanta, stilInot, dataDesfasurarii, locatie,nrInregistrari);
                    probe.add(proba);
                }
                logger.traceExit("Probe: {}", probe);
                return probe;
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }
}
