package ro.mpp2024.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ro.mpp2024.Business.ServiceInterface;
import ro.mpp2024.Domain.Angajat;
import ro.mpp2024.Main;

import java.io.IOException;

public class LoginController {
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

        labelErori.setText("");
    }

    public void handleLogIn(ActionEvent actionEvent) {
        try{

            String firstname = textFieldPrenume.getText();
            String lastname = textFieldNume.getText();
            String password = passwordFieldParola.getText();
            Angajat angajat = service.findAngajatByFirstnameLastnamePassword(firstname,lastname,password);

            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/View/mainPage-view.fxml"));
            Scene scene = new Scene(loader.load());

            MainPageController mainPageController = loader.getController();
            mainPageController.setService(currentStage,service, angajat);


            currentStage.setScene(scene);

        }catch (RuntimeException e){
//            throw  new RuntimeException(e.getMessage());
            labelErori.setText(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
