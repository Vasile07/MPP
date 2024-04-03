package ro.mpp2024.Repository;

import ro.mpp2024.Domain.Participant;
import ro.mpp2024.Domain.ParticipantDTO;

import java.util.List;
import java.util.Optional;

public interface ParticipantAbstractRepository extends AbstractRepository<Integer, Participant> {
    List<ParticipantDTO> getAllParticipantiFromProba(Integer probaId);

    Optional<Participant> findByFirstnameAndLastname(String firstname, String lastname);
}
