package ro.mpp;

import ro.mpp.Domain.Angajat;
import ro.mpp.Domain.ParticipantDTO;
import ro.mpp.Domain.ProbaDTO;
import ro.mpp.dto.AngajatDTO;
import ro.mpp.dto.InscriereDTO;
import ro.mpp.objectprotocol.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class SwimmingCompetitionClientWorker implements Runnable,ObserverInterface {
    private ServiceInterface service;
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public SwimmingCompetitionClientWorker(ServiceInterface service, Socket connection) {
        this.service = service;
        this.connection = connection;
        try{
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while(connected){
            try{
                Object request = input.readObject();
                Object response = handleRequest((Request) request);
                if(response != null) {
                    sendResponse((Response) response);
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try{
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private Response handleRequest(Request request) {

        if(request instanceof LogInRequest){
            LogInRequest logInRequest = (LogInRequest) request;
            AngajatDTO angajatDTO = logInRequest.getAngajatDTO();
            try{
                String firstname = angajatDTO.getFirstname();
                String lastname = angajatDTO.getLastname();
                String password = angajatDTO.getPassword();
                Angajat angajat = service.findAngajatByFirstnameLastnamePassword(firstname, lastname, password, this);
                return new LogInResponse(angajat);
            }catch (Exception exception){
                connected = false;
                return new ErrorResponse(exception.getMessage());
            }
        }

        if(request instanceof LogOutRequest){
            LogOutRequest logOutRequest = (LogOutRequest) request;
            AngajatDTO angajatDTO = logOutRequest.getAngajatDTO();
            try{
                Angajat angajat = new Angajat(angajatDTO.getLastname(), angajatDTO.getFirstname(), "", angajatDTO.getPassword());
                service.logout(angajat);
                connected = false;
                return new OkResponse();
            }catch (Exception e){
                return new ErrorResponse(e.getMessage());
            }
        }

        if(request instanceof InscriereRequest){
            InscriereRequest inscriereRequest = (InscriereRequest) request;
            InscriereDTO inscriereDTO = inscriereRequest.getInscriereDTO();
            try{
                service.addParticipari(inscriereDTO.getFirstname(), inscriereDTO.getLastname(), inscriereDTO.getProbe());
                return new OkResponse();
            }catch (Exception e){
                return new ErrorResponse(e.getMessage());
            }
        }

        if(request instanceof GetParticipantiFromProbaRequest){
            GetParticipantiFromProbaRequest getParticipantiFromProbaRequest = (GetParticipantiFromProbaRequest) request;
            Integer idProba = getParticipantiFromProbaRequest.getIdProba();
            try{
                List<ParticipantDTO> participantDTOList =  service.getAllParticipantiFromProba(idProba);
                return new GetParticipantiFromProbaResponse(participantDTOList);
            }catch (Exception e){
                return new ErrorResponse(e.getMessage());
            }
        }

        if(request instanceof GetAllProbeRequest){
            GetAllProbeRequest getAllProbeRequest = (GetAllProbeRequest) request;
            try{
                List<ProbaDTO> probaDTOS = service.getAllProbeAfterToday();
                return new GetAllProbeResponse(probaDTOS);
            }catch (Exception e){
                return new ErrorResponse(e.getMessage());
            }
        }
        return new ErrorResponse("Invalid request");
    }


    private void sendResponse(Response response) throws IOException {
        System.out.println("Sending: " + response);
        output.writeObject(response);
        output.flush();
    }

    @Override
    public void userLoggedIn() {

    }

    @Override
    public void userLoggedOut() {

    }

    @Override
    public void insciereEfectuata() {
        try {
            sendResponse(new InscriereEfectuataResponse());
        }catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
