package ro.mpp2024.Repository;

import ro.mpp2024.Domain.Angajat;

import java.util.Optional;

public interface AngajatAbstractRepository extends AbstractRepository<Integer, Angajat> {
    Optional<Angajat> findByFirstnameLastnamePassword(String firstname, String lastname, String password);
}
