package ro.mpp;

import ro.mpp.Domain.Angajat;

import java.util.Optional;

public interface AngajatAbstractRepository extends AbstractRepository<Integer, Angajat> {
    Optional<Angajat> findByFirstnameLastnamePassword(String firstname, String lastname, String password);
}
