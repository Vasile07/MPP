package ro.mpp2024.Repository;

import ro.mpp2024.Domain.Participant;

import java.util.List;

public interface ParticipantAbstractRepository extends AbstractRepository<Integer, Participant> {
    List<Participant> getAllParticipantiFromProba(Integer probaId);
}
