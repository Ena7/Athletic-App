package controller;

import domain.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.IService;

public class LoginWindowController {

    private IService service;

    public void setService(IService service){
        this.service = service;
    }

    public LoginWindowController() {

    }

    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private Button loginButton;

    private void showNotification(String message, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle("Notification");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void loginButtonHandler() {
        String username = usernameTextField.getText();
        String password = passwordPasswordField.getText();
        User user = service.checkUserCredentials(username, password);
        if (user == null){
            this.showNotification("Incorrect username or password.", Alert.AlertType.WARNING);
            return;
        }
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainWindow.fxml"));
            Parent root = loader.load();
            MainWindowController ctrl = loader.getController();
            ctrl.setServiceAndUser(this.service, user);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Control Panel");
            stage.show();

            Stage loginStage = (Stage) loginButton.getScene().getWindow();
            loginStage.close();
        }
        catch (Exception e) {
            this.showNotification("Error while opening main window.\n" + e, Alert.AlertType.ERROR);
        }
    }

}
