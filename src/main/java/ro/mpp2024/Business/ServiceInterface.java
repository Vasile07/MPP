package ro.mpp2024.Business;

import ro.mpp2024.Domain.*;

import java.util.List;
import java.util.Optional;

public interface ServiceInterface {
    Angajat findAngajatByFirstnameLastnamePassword(String firstname, String lastname, String password);
    List<ProbaDTO> getAllProbeAfterToday();
    void addParticipari(String firstname, String lastname,List<Integer> probe);
    List<ParticipantDTO> getAllParticipantiFromProba(Integer probaId);
}
