package ro.mpp.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ro.mpp.Domain.Angajat;
import ro.mpp.Domain.ParticipantDTO;
import ro.mpp.Domain.ProbaDTO;
import ro.mpp.Domain.StilInot;
import ro.mpp.ObserverInterface;
import ro.mpp.ServiceInterface;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class MainPageController implements ObserverInterface {
    @FXML
    private Label labelHello;
    @FXML
    private TableView<ProbaDTO> tableViewProbe;
    private ObservableList<ProbaDTO> modelTableViewProbe = FXCollections.observableArrayList();
    @FXML
    private TableColumn<ProbaDTO,Integer> tableColumnDistanta;
    @FXML
    private TableColumn<ProbaDTO, StilInot> tableColumnStil;
    @FXML
    private TableColumn<ProbaDTO, LocalDateTime> tableColumnDataDesfasurarii;
    @FXML
    private TableColumn<ProbaDTO,String> tableColumnLocatie;
    @FXML
    private TableColumn<ProbaDTO,Integer> tableColumnNrInscrieri;
    @FXML
    private ListView<ParticipantDTO> listViewParticipanti;
    private ObservableList<ParticipantDTO> modelListViewParticipanti = FXCollections.observableArrayList();
    @FXML
    private Label TestFieldPrenume;
    @FXML
    private TextField textFieldNume;
    @FXML
    private TextField textFieldPrenume;

    private Stage currentStage;
    private ServiceInterface service;
    private Angajat currentAngajat;

    public void setService(Stage currentStage, ServiceInterface service, Angajat angajat) {
        this.currentStage = currentStage;
        this.service = service;
        currentAngajat = angajat;
        Platform.runLater(() -> {
            labelHello.setText("Hello " + angajat.getNume() + " " + angajat.getPrenume() + "!");
        });


        initializeContent();
        initTableModel();
    }

    public void initializeContent(){
        Platform.runLater(() -> {
            tableViewProbe.setItems(modelTableViewProbe);
            tableColumnDistanta.setCellValueFactory(new PropertyValueFactory<>("distanta"));
            tableColumnStil.setCellValueFactory(new PropertyValueFactory<>("stil"));
            tableColumnDataDesfasurarii.setCellValueFactory(new PropertyValueFactory<>("dataDesfasurarii"));
            tableColumnLocatie.setCellValueFactory(new PropertyValueFactory<>("locatie"));
            tableColumnNrInscrieri.setCellValueFactory(new PropertyValueFactory<>("nrInscrieri"));

            tableViewProbe.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

            listViewParticipanti.setItems(modelListViewParticipanti);

            listViewParticipanti.setCellFactory(lv -> new ListCell<ParticipantDTO>(){
                @Override
                public void updateItem(ParticipantDTO participant, boolean empty){
                    super.updateItem(participant,empty);
                    if(empty){
                        setText(null);
                    }else{
                        String text = participant.getNume() + " " + participant.getPrenume() + ", " + participant.getVarsta() + " ani\n";
                        String probe = "Probe: ";
                        for(Integer idProba : participant.getProbe())
                            probe +=  idProba + " ";
                        text = text + probe;
                        setText(text);
                    }
                }
            });
        });
    }

    public void initTableModel(){
        Platform.runLater(() -> {
            List<ProbaDTO> probe = service.getAllProbeAfterToday();
            modelTableViewProbe.setAll(probe);
        });
    }

    public void initListModel(){
        Platform.runLater(() -> {
            try {
                Integer idProba = tableViewProbe.getSelectionModel().getSelectedItem().getIdProba();

                List<ParticipantDTO> participanti = service.getAllParticipantiFromProba(idProba);

                modelListViewParticipanti.setAll(participanti);
            }catch (Exception ignored){

            }
        });

    }



    public void handleLogOut(ActionEvent actionEvent) {
        try {
            service.logout(currentAngajat);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/login-view.fxml"));
            Scene scene = new Scene(loader.load());

            currentStage.setScene(scene);

            LoginController loginController = loader.getController();
            loginController.setService(currentStage, service);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleSeeParticipanti(ActionEvent actionEvent) {
        initListModel();
    }

    public void handleInregistreaza(ActionEvent actionEvent) {
        try{
           String firstname = textFieldPrenume.getText();
            String lastname = textFieldNume.getText();
            List<Integer> probe = tableViewProbe.getSelectionModel().getSelectedItems().stream().map(ProbaDTO::getIdProba).toList();
            service.addParticipari(firstname,lastname,probe);
        }catch (RuntimeException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
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
        initTableModel();
        initListModel();
    }
}
