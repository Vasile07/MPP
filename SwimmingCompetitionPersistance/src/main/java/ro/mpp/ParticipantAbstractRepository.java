package ro.mpp;

import ro.mpp.Domain.Participant;
import ro.mpp.Domain.ParticipantDTO;

import java.util.List;
import java.util.Optional;

public interface ParticipantAbstractRepository extends AbstractRepository<Integer, Participant> {
    List<ParticipantDTO> getAllParticipantiFromProba(Integer probaId);

    Optional<Participant> findByFirstnameAndLastname(String firstname, String lastname);
}
