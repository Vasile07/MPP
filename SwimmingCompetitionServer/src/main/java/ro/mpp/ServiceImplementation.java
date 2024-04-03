package ro.mpp;

import ro.mpp.Domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceImplementation implements ServiceInterface{
    private final AngajatAbstractRepository angajatRepository;
    private final ParticipantAbstractRepository participantRepository;
    private final ParticipareAbstractRepository participareRepository;
    private final ProbaAbstractRepository probaRepository;
    private Map<String, ObserverInterface> loggedClients;

    public ServiceImplementation(AngajatAbstractRepository angajatRepository, ParticipantAbstractRepository participantRepository, ParticipareAbstractRepository participareRepository, ProbaAbstractRepository probaRepository) {
        this.angajatRepository = angajatRepository;
        this.participantRepository = participantRepository;
        this.participareRepository = participareRepository;
        this.probaRepository = probaRepository;
        loggedClients = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized Angajat findAngajatByFirstnameLastnamePassword(String firstname, String lastname, String password, ObserverInterface client) {
        Optional<Angajat> angajat = angajatRepository.findByFirstnameLastnamePassword(firstname,lastname,password);
        if(angajat.isEmpty())
            throw new RuntimeException("Date invalide!");
        Angajat a = angajat.get();
        if(loggedClients.get(a.getNume() + " " + a.getPrenume()) != null)
            throw new RuntimeException("User already logged in!\n");
        loggedClients.put(a.getNume() + " " + a.getPrenume(),client);
        return a;
    }

    private final int defaulThreadsNumber = 5;
    private void notifyAllUsersLoggedIn(){
        ExecutorService executor = Executors.newFixedThreadPool(defaulThreadsNumber);
        for(String user : loggedClients.keySet()){
            ObserverInterface client = loggedClients.get(user);
            executor.execute(() ->{
                try{
                    client.insciereEfectuata();
                }catch (Exception e){
                    System.err.println(e.getMessage());
                }
            });
        }
        executor.shutdown();
    }

    @Override
    public synchronized List<ProbaDTO> getAllProbeAfterToday() {
        return probaRepository.getAllProbaAfterToday();
    }

    @Override
    public synchronized void addParticipari(String firstname, String lastname, List<Integer> probe) {
        Optional<Participant> participant = participantRepository.findByFirstnameAndLastname(firstname,lastname);
        if(participant.isEmpty())
            throw new RuntimeException("Participant inexistent!");
        for(Integer idProba : probe){
            Participare participare = new Participare(LocalDate.now());
            Proba proba = probaRepository.findById(idProba).get();
            participare.setId(new Pair<>(participant.get(),proba));
            participareRepository.add(participare);
        }
        notifyAllUsersLoggedIn();
    }

    @Override
    public synchronized List<ParticipantDTO> getAllParticipantiFromProba(Integer probaId) {
        return participantRepository.getAllParticipantiFromProba(probaId);
    }

    @Override
    public synchronized void logout(Angajat angajat) {
        loggedClients.remove(angajat.getNume() + " " + angajat.getPrenume());
    }


}
