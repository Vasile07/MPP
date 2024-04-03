package ro.mpp;

import ro.mpp.Domain.Proba;
import ro.mpp.Domain.ProbaDTO;

import java.util.List;

public interface ProbaAbstractRepository extends AbstractRepository<Integer, Proba> {
    List<ProbaDTO> getAllProbaAfterToday();
}
