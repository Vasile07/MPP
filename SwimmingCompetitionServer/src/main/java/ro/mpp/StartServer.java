package ro.mpp;

import ro.mpp.Validator.AngajatValidator;
import ro.mpp.Validator.ParticipantValidator;
import ro.mpp.Validator.ParticipareValdiator;
import ro.mpp.Validator.ProbaValidator;

import java.io.IOException;
import java.util.Properties;

public class StartServer {
    private static int defaultPort=5555;

    public static void main(String[] args) {
        Properties serverProperties = new Properties();
        try{
            serverProperties.load(StartServer.class.getResourceAsStream("/server.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        AngajatAbstractRepository angajatRepository = new AngajatRepository(serverProperties, new AngajatValidator());
        ParticipareAbstractRepository participareRepository = new ParticipareRepository(serverProperties, new ParticipareValdiator());
        ParticipantAbstractRepository participantRepository = new ParticipantRepository(serverProperties, new ParticipantValidator());
        ProbaAbstractRepository probaRepository = new ProbaRepository(serverProperties, new ProbaValidator());

        ServiceInterface service = new ServiceImplementation(angajatRepository,participantRepository,participareRepository,probaRepository);
        int serverPort = defaultPort;
        try{
            serverPort = Integer.parseInt(serverProperties.getProperty("server.port"));
        }catch (NumberFormatException e){
            throw new RuntimeException(e.getMessage());
        }
        AbstractServer server = new ConcurrentServer(serverPort, service);
        try{
            System.out.println(serverPort);
            server.start();
        }catch (RuntimeException e){
            throw new RuntimeException(e.getMessage());
        }

    }
}
