package ro.mpp2024;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ro.mpp2024.Business.Service;
import ro.mpp2024.Business.ServiceInterface;
import ro.mpp2024.Controller.LoginController;
import ro.mpp2024.Domain.*;
import ro.mpp2024.Repository.*;
import ro.mpp2024.Validator.AngajatValidator;
import ro.mpp2024.Validator.ParticipantValidator;
import ro.mpp2024.Validator.ParticipareValdiator;
import ro.mpp2024.Validator.ProbaValidator;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Properties;

public class Main extends Application {

    public static void main(String[] args) {
        launch();
    }


    @Override
    public void start(Stage stage) throws Exception {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("bd.config"));

            AngajatAbstractRepository angajatRepository = new AngajatRepository(properties, new AngajatValidator());
            ParticipareAbstractRepository participareRepository = new ParticipareRepository(properties, new ParticipareValdiator());
            ParticipantAbstractRepository participantRepository = new ParticipantRepository(properties, new ParticipantValidator());
            ProbaAbstractRepository probaRepository = new ProbaRepository(properties, new ProbaValidator());

            ServiceInterface service = new Service(angajatRepository,participantRepository,participareRepository,probaRepository);

            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/View/login-view.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);

            stage.setTitle("Swimming Competiton");

            stage.getIcons().add(new Image(Main.class.getResourceAsStream("/View/swim.jpg"))); // Pentru icon-ul aplicatiei

            LoginController loginController = loader.getController();
            loginController.setService(stage,service);

            stage.show();

        } catch (IOException e) {
            System.out.println("Cannot find bd.config " + e);
        }
    }



    private static void teste() {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("bd.config"));
//            testAngajat(properties);
//            testParticipant(properties);
//            testParticipare(properties);
//            testProba(properties);
        } catch (IOException e) {
            System.out.println("Cannot find bd.config " + e);
        }
    }

    private static void testAngajat(Properties properties) {
        AngajatAbstractRepository angajatRepository = new AngajatRepository(properties, new AngajatValidator());
        angajatRepository.findAll().forEach(System.out::println);
        Angajat angajat = new Angajat("Test", "Test", "Locatie Test", "Parola Test");
        angajat.setId(6);

        angajatRepository.add(angajat);
        angajatRepository.findAll().forEach(System.out::println);

        angajat.setNume("Test Nou");
        angajat.setPrenume("Test Nou");
        angajatRepository.update(angajat);
        angajatRepository.findAll().forEach(System.out::println);

        System.out.println(angajatRepository.findById(6));

        angajatRepository.deleteById(6);
        angajatRepository.findAll().forEach(System.out::println);
    }

    private static void testParticipant(Properties properties) {
        ParticipantAbstractRepository participantRepository = new ParticipantRepository(properties, new ParticipantValidator());

        participantRepository.findAll().forEach(System.out::println);

        Participant participant = new Participant("Test", "Test", 20);
        participant.setId(12);

        participantRepository.add(participant);
        participantRepository.findAll().forEach(System.out::println);

        participant.setNume("Test nou");
        participant.setPrenume("Test nou");
        participantRepository.update(participant);
        participantRepository.findAll().forEach(System.out::println);

        System.out.println(participantRepository.findById(12));

        participantRepository.deleteById(12);
        participantRepository.findAll().forEach(System.out::println);
    }

    private static void testParticipare(Properties properties) {

    }

    private static void testProba(Properties properties) {
        ProbaAbstractRepository probaRepository = new ProbaRepository(properties, new ProbaValidator());

        probaRepository.findAll().forEach(System.out::println);
        System.out.println();
        Proba proba = new Proba(200, StilInot.liber, LocalDateTime.of(2023, 12, 12, 12, 00, 00), "Cluj-Napoca");
        proba.setId(16);

        probaRepository.add(proba);
        probaRepository.findAll().forEach(System.out::println);
        System.out.println();
        proba.setStil(StilInot.fluture);

        probaRepository.update(proba);
        probaRepository.findAll().forEach(System.out::println);
        System.out.println(probaRepository.findById(16));
        System.out.println();

        probaRepository.deleteById(16);
        probaRepository.findAll().forEach(System.out::println);
        System.out.println();
    }
}
