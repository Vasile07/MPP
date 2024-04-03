package ro.mpp.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ro.mpp.Domain.Angajat;
import ro.mpp.ObserverInterface;
import ro.mpp.ServiceInterface;

import java.io.IOException;

public class LoginController implements ObserverInterface {
    @FXML
    private Label labelErori;
    @FXML
    private PasswordField passwordFieldParola;
    @FXML
    private TextField textFieldPrenume;
    @FXML
    private TextField textFieldNume;

    private Stage currentStage;

    private ServiceInterface service;

    public void setService(Stage stage, ServiceInterface service) {
        this.currentStage = stage;
        this.service = service;
        Platform.runLater(() -> {
            labelErori.setText("");
        });

    }

    public void handleLogIn(ActionEvent actionEvent) {
        try{
            String firstname = textFieldPrenume.getText();
            String lastname = textFieldNume.getText();
            String password = passwordFieldParola.getText();


            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/mainPage-view.fxml"));
            Scene scene = new Scene(loader.load());

            MainPageController mainPageController = loader.getController();
            Angajat angajat = service.findAngajatByFirstnameLastnamePassword(firstname,lastname,password, mainPageController);
            mainPageController.setService(currentStage,service, angajat);

            Platform.runLater(() -> {
                currentStage.setScene(scene);
            });


        }catch (RuntimeException e){
//            throw  new RuntimeException(e.getMessage());
            labelErori.setText(e.getMessage());
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setContentText(e.getMessage());
//            alert.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void userLoggedIn() {

    }

    @Override
    public void userLoggedOut() {

    }

    @Override
    public void insciereEfectuata() {

    }
}
