import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.Properties;

public class StartClientFX extends Application {

    @Override
    public void start(Stage primaryStage) {
//        ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring_client.xml");
//        IServiceServer server = (IServiceServer) factory.getBean("srv");

        Properties props = new Properties();
        try {
            props.load(StartClientFX.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set");
            props.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find client.properties\n" + e);
            return;
        }
        String defaultServer = "localhost";
        String serverIP = props.getProperty("server.host", defaultServer);
        int defaultPort = 55555;
        int serverPort = defaultPort;
        try {
            serverPort = Integer.parseInt(props.getProperty("server.port"));
        } catch (NumberFormatException e) {
            System.err.println("Wrong port number " + e.getMessage());
            System.out.println("Using default port: " + defaultPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        IServiceServer server = new ServiceProxy(serverIP, serverPort);
//        IServiceServer server = new ProtoProxy(serverIP, serverPort);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginWindow.fxml"));
            Parent root = loader.load();
            LoginWindowController ctrl = loader.getController();
            ctrl.setService(server);

            primaryStage.setOnCloseRequest(event -> System.exit(0));

            FXMLLoader loaderM = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
            Parent rootM = loaderM.load();
            MainWindowController ctrlM = loaderM.getController();
            ctrlM.setService(server);

            ctrl.setParent(rootM);
            ctrl.setMainController(ctrlM);

            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Login");
            primaryStage.show();
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Error while opening windows.\n" + e);
            alert.showAndWait();
        }

    }
}
