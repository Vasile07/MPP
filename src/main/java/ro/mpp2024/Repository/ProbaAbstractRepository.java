package ro.mpp2024.Repository;

import ro.mpp2024.Domain.Proba;
import ro.mpp2024.Domain.ProbaDTO;

import java.util.List;

public interface ProbaAbstractRepository extends AbstractRepository<Integer, Proba> {
    List<ProbaDTO> getAllProbaAfterToday();
}
