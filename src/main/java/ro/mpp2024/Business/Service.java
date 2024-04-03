package ro.mpp2024.Business;

import ro.mpp2024.Domain.*;
import ro.mpp2024.Repository.AngajatAbstractRepository;
import ro.mpp2024.Repository.ParticipantAbstractRepository;
import ro.mpp2024.Repository.ParticipareAbstractRepository;
import ro.mpp2024.Repository.ProbaAbstractRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class Service implements ServiceInterface{

    private final AngajatAbstractRepository angajatRepository;
    private final ParticipantAbstractRepository participantRepository;
    private final ParticipareAbstractRepository participareRepository;
    private final ProbaAbstractRepository probaRepository;

    public Service(AngajatAbstractRepository angajatRepository, ParticipantAbstractRepository participantRepository, ParticipareAbstractRepository participareRepository, ProbaAbstractRepository probaRepository) {
        this.angajatRepository = angajatRepository;
        this.participantRepository = participantRepository;
        this.participareRepository = participareRepository;
        this.probaRepository = probaRepository;
    }

    @Override
    public Angajat findAngajatByFirstnameLastnamePassword(String firstname, String lastname, String password) {
        Optional<Angajat> angajat = angajatRepository.findByFirstnameLastnamePassword(firstname,lastname,password);
        if(angajat.isEmpty())
            throw new RuntimeException("Date invalide!");
        return angajat.get();
    }

    @Override
    public List<ProbaDTO> getAllProbeAfterToday() {
        return probaRepository.getAllProbaAfterToday();
    }

    @Override
    public void addParticipari(String firstname, String lastname, List<Integer> probe) {
        Optional<Participant> participant = participantRepository.findByFirstnameAndLastname(firstname,lastname);
        if(participant.isEmpty())
            throw new RuntimeException("Participant inexistent!");
        for(Integer idProba : probe){
            Participare participare = new Participare(LocalDate.now());
            Proba proba = probaRepository.findById(idProba).get();
            participare.setId(new Pair<>(participant.get(),proba));
            participareRepository.add(participare);
        }
    }

    @Override
    public List<ParticipantDTO> getAllParticipantiFromProba(Integer probaId) {
        return participantRepository.getAllParticipantiFromProba(probaId);
    }
}
