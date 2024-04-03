package ro.mpp;

import ro.mpp.Domain.Angajat;
import ro.mpp.Domain.ParticipantDTO;
import ro.mpp.Domain.ProbaDTO;
import ro.mpp.dto.AngajatDTO;
import ro.mpp.dto.InscriereDTO;
import ro.mpp.objectprotocol.*;

import javax.management.ObjectName;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class SwimmingCompetitionProxy implements ServiceInterface{
    private String host;
    private int port;
    private ObserverInterface client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;
    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public SwimmingCompetitionProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<>();
    }

    @Override
    public Angajat findAngajatByFirstnameLastnamePassword(String firstname, String lastname, String password, ObserverInterface client) {
        initializeConnection();
        AngajatDTO angajatDTO = new AngajatDTO(firstname, lastname, password);
        sendRequest(new LogInRequest(angajatDTO));
        Response response = readResponse();
        if(response instanceof LogInResponse){
            this.client = client;
//            System.out.println(client.getClass());
            return ((LogInResponse) response).getAngajat();
        }
        if(response instanceof ErrorResponse){
            closeConnection();
            throw new RuntimeException(((ErrorResponse) response).getMessage());
        }
        throw new RuntimeException("Invalid response!\n");
    }

    @Override
    public List<ProbaDTO> getAllProbeAfterToday() {

        sendRequest(new GetAllProbeRequest());
        Response response = readResponse();
        if(response instanceof GetAllProbeResponse){
            return ((GetAllProbeResponse) response).getProbaDTOS();
        }
        if(response instanceof ErrorResponse){
            throw new RuntimeException(((ErrorResponse) response).getMessage());
        }
        throw new RuntimeException("Invalid response!\n");
    }

    @Override
    public void addParticipari(String firstname, String lastname, List<Integer> probe) {
        InscriereDTO inscriereDTO = new InscriereDTO(firstname, lastname, probe);
        sendRequest(new InscriereRequest(inscriereDTO));
        Response response = readResponse();
        if(response instanceof OkResponse){
            return;
        }
        if(response instanceof ErrorResponse){
            throw new RuntimeException(((ErrorResponse) response).getMessage());
        }
        throw new RuntimeException("Invalid response!\n");
    }

    @Override
    public List<ParticipantDTO> getAllParticipantiFromProba(Integer probaId) {

        sendRequest(new GetParticipantiFromProbaRequest(probaId));
        Response response = readResponse();
        if(response instanceof GetParticipantiFromProbaResponse){
            return ((GetParticipantiFromProbaResponse) response).getParticipantDTOS();
        }
        if(response instanceof ErrorResponse){
            throw new RuntimeException(((ErrorResponse) response).getMessage());
        }
        throw new RuntimeException("Invalid response!\n");
    }

    @Override
    public void logout(Angajat angajat) {
        AngajatDTO angajatDTO = new AngajatDTO(angajat.getPrenume(), angajat.getNume(), angajat.getParola());
        sendRequest(new LogOutRequest(angajatDTO));
        Response response = readResponse();
        if(response instanceof OkResponse){
//            closeConnection();
            return ;
        }
        if(response instanceof ErrorResponse){
            throw new RuntimeException(((ErrorResponse) response).getMessage());
        }
        throw new RuntimeException("Invalid response!\n");
    }

    private void initializeConnection(){
        try{
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void closeConnection(){
        finished = true;
        try{
            input.close();
            output.close();
            connection.close();
            client = null;
        }catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }
    private void sendRequest(Request request){
        try{
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private Response readResponse(){
        Response response = null;
        try{
            response = qresponses.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response;
    }


    private void handleUpdate(UpdateResponse response){
        if(response instanceof InscriereEfectuataResponse){
            client.insciereEfectuata();
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private class ReaderThread implements Runnable {
        @Override
        public void run() {
            while (!finished) {
                try {
                    System.out.println("Entered");
                    Object response = input.readObject();
                    System.out.println("Received " + response);
                    if (response instanceof UpdateResponse) {
                        handleUpdate((UpdateResponse) response);
                    } else {
                        try {
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } catch (IOException e) {
                    System.out.println(e.getClass());
                    throw new RuntimeException(e.getMessage());
                } catch (ClassNotFoundException e) {
                    System.out.println(e.getClass());
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
