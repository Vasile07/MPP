package ro.mpp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ro.mpp.Controller.LoginController;

import java.io.IOException;
import java.util.Properties;

public class StartClient extends Application {

    public static int defaultPort = 55555;
    public static String defaultHost = "localhost";
    public static void main(String[] args) throws IOException {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Properties clientProperties = new Properties();
        try{
            clientProperties.load(StartClient.class.getResourceAsStream("/server.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String serverIp = clientProperties.getProperty("server.host",defaultHost);
        int serverPort = defaultPort;
        try{
            serverPort = Integer.parseInt(clientProperties.getProperty("server.port"));
        }catch (NumberFormatException ignored){
            System.out.println("PROBLEMA");
        }
        System.out.println(serverPort);
        ServiceInterface service = new SwimmingCompetitionProxy(serverIp,serverPort);

        FXMLLoader loader = new FXMLLoader(StartClient.class.getResource("/View/login-view.fxml"));
        Scene scene = new Scene(loader.load());

        stage.setScene(scene);

        stage.setTitle("Swimming Competiton");

        stage.getIcons().add(new Image(StartClient.class.getResourceAsStream("/View/swim.jpg"))); // Pentru icon-ul aplicatiei

        LoginController loginController = loader.getController();
        loginController.setService(stage,service);

        stage.show();

    }
}
