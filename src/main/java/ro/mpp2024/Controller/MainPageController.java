package ro.mpp2024.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ro.mpp2024.Business.ServiceInterface;
import ro.mpp2024.Domain.*;
import ro.mpp2024.Main;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MainPageController {
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

        labelHello.setText("Hello " + angajat.getNume() + " " + angajat.getPrenume() + "!");

        initializeContent();
        initTableModel();
    }

    public void initializeContent(){
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
    }

    public void initTableModel(){
        List<ProbaDTO> probe = service.getAllProbeAfterToday();
        modelTableViewProbe.setAll(probe);
    }

    public void initListModel(){
        Integer idProba = tableViewProbe.getSelectionModel().getSelectedItem().getIdProba();

        List<ParticipantDTO> participanti = service.getAllParticipantiFromProba(idProba);

        modelListViewParticipanti.setAll(participanti);
    }



    public void handleLogOut(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/View/login-view.fxml"));
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
            initTableModel();
        }catch (RuntimeException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
