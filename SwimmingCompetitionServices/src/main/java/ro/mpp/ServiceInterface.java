package ro.mpp;

import ro.mpp.Domain.Angajat;
import ro.mpp.Domain.ParticipantDTO;
import ro.mpp.Domain.ProbaDTO;

import java.util.List;

public interface ServiceInterface {
    Angajat findAngajatByFirstnameLastnamePassword(String firstname, String lastname, String password, ObserverInterface client);
    List<ProbaDTO> getAllProbeAfterToday();
    void addParticipari(String firstname, String lastname,List<Integer> probe);
    List<ParticipantDTO> getAllParticipantiFromProba(Integer probaId);

    void logout(Angajat angajat);

}
