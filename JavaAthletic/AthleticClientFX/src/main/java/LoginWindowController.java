import domain.User;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginWindowController {

    private IServiceServer server;
    private MainWindowController mainWindowController;
    private Parent mainWindowControllerParent;

    public void setService(IServiceServer server){
        this.server = server;
    }

    public void setMainController(MainWindowController mwc) {
        mainWindowController = mwc;
    }

    public void setParent(Parent p) {
        mainWindowControllerParent = p;
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
        User user = new User(username, password);
        try {
            server.login(user, mainWindowController);
        } catch (Exception e) {
            this.showNotification("Incorrect username or password.", Alert.AlertType.WARNING);
            return;
        }
        try {
            Stage stage = new Stage();
            stage.setTitle("User: " + username);
            stage.setScene(new Scene(mainWindowControllerParent));

            stage.setOnCloseRequest(event -> {
                mainWindowController.logout();
                System.exit(0);
            });

            stage.show();
            mainWindowController.setUser(user);
            mainWindowController.setEvents();

            Stage this_stage = (Stage) loginButton.getScene().getWindow();
            this_stage.close();
        }
        catch (Exception e) {
            this.showNotification("Error while opening main window.\n" + e, Alert.AlertType.ERROR);
        }
    }

}
