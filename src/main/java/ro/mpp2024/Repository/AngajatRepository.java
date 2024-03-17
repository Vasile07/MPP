package ro.mpp2024.Repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.Domain.Angajat;
import ro.mpp2024.Validator.AbstractValidator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class AngajatRepository implements AngajatAbstractRepository{

    private JdbcUtils dbUtils;
    private AbstractValidator<Angajat> validator;
    private static final Logger logger= LogManager.getLogger();
    public AngajatRepository(Properties props, AbstractValidator<Angajat> validator)
    {
        logger.info("Initializing AngajatRepository with properties: {} ",props);
        this.validator = validator;
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Optional<Angajat> add(Angajat angajat) {
        logger.traceEntry("Saving angajat {}", angajat);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("insert into Angajat(nume, prenume, locatie, parola) values (?,?,?,?)"))
        {
            validator.validate(angajat);

            preparedStatement.setString(1, angajat.getNume());
            preparedStatement.setString(2, angajat.getPrenume());
            preparedStatement.setString(3, angajat.getLocatie());
            preparedStatement.setString(4, angajat.getParola());

            int rows = preparedStatement.executeUpdate();

            logger.trace("Saved {} instances", rows);
            if(rows == 0)
                return Optional.of(angajat);
            return Optional.empty();

        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Angajat> update(Angajat angajat) {
        logger.traceEntry("Updating angajat {}",angajat);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("update Angajat set nume = ?, prenume = ?, locatie = ?, parola = ? where id_angajat = ?"))
        {
            validator.validate(angajat);

            preparedStatement.setString(1, angajat.getNume());
            preparedStatement.setString(2, angajat.getPrenume());
            preparedStatement.setString(3, angajat.getLocatie());
            preparedStatement.setString(4, angajat.getParola());
            preparedStatement.setInt(5, angajat.getId());

            int rows = preparedStatement.executeUpdate();

            logger.trace("Updated {} instances", rows);
            if(rows == 0)
                return Optional.of(angajat);
            return Optional.empty();

        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Angajat> deleteById(Integer id) {
        logger.traceEntry("Deleting angajat with id {}",id);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("delete from Angajat where id_angajat = ?"))
        {
            Optional<Angajat> angajat = findById(id);

            preparedStatement.setInt(1, id);

            int rows = preparedStatement.executeUpdate();
            logger.trace("Deleted {} instances", rows);
            return angajat;

        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Angajat> findById(Integer id) {
        logger.traceEntry("Finding angajat with id {}", id);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from Angajat where id_angajat = ?"))
        {
            preparedStatement.setInt(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    Integer idAngajat = resultSet.getInt("id_angajat");
                    String nume = resultSet.getString("nume");
                    String prenume = resultSet.getString("prenume");
                    String locatie = resultSet.getString("locatie");
                    String parola = resultSet.getString("parola");

                    Angajat angajat = new Angajat(nume, prenume, locatie, parola);
                    angajat.setId(idAngajat);
                    logger.traceExit("Found angajat {}",angajat);
                    return Optional.of(angajat);
                }
                logger.traceExit("No angajat with id {} was found",id);
                return Optional.empty();
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Angajat> findAll() {
        logger.traceEntry();
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from Angajat"))
        {
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                List<Angajat> angajati = new ArrayList<>();
                while(resultSet.next()){
                    Integer idAngajat = resultSet.getInt("id_angajat");
                    String nume = resultSet.getString("nume");
                    String prenume = resultSet.getString("prenume");
                    String locatie = resultSet.getString("locatie");
                    String parola = resultSet.getString("parola");

                    Angajat angajat = new Angajat(nume, prenume, locatie, parola);
                    angajat.setId(idAngajat);

                    angajati.add(angajat);
                }
                logger.traceExit("Angajati: {}", angajati);
                return angajati;
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Angajat> findByFirstnameLastnamePassword(String firstname, String lastname, String password) {
        return Optional.empty();
    }
}
