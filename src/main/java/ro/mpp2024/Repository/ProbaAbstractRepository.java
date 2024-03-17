package ro.mpp2024.Repository;

import ro.mpp2024.Domain.Proba;

import java.util.List;

public interface ProbaAbstractRepository extends AbstractRepository<Integer, Proba> {
    List<Proba> getAllProbaAfterToday();
}
