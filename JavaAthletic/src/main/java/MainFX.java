import controller.LoginWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import repo.ChildRepositoryDB;
import repo.EntryRepositoryDB;
import repo.UserRepositoryDB;
import service.Service;

import java.io.FileReader;
import java.util.Properties;

public class MainFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginWindow.fxml"));
            Parent root = loader.load();
            LoginWindowController ctrl = loader.getController();
            ctrl.setService(getService());
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Login");
            primaryStage.show();
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Error while opening login window.\n" + e);
            alert.showAndWait();
        }
    }

    static Service getService() throws Exception {
        try {
            Properties props = new Properties();
            props.load(new FileReader("bd.config"));

            UserRepositoryDB userRepo = new UserRepositoryDB(props);
            ChildRepositoryDB childRepo = new ChildRepositoryDB(props);
            EntryRepositoryDB entryRepo = new EntryRepositoryDB(props);

            return new Service(userRepo, childRepo, entryRepo);
        }
        catch(Exception e) {
            throw new Exception("Cannot find bd.config.");
        }
    }

}
