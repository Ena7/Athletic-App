package controller;

import domain.AgeGroup;
import domain.Event;
import domain.User;
import dtos.ChildNoEventsDTO;
import dtos.EventCountDTO;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import service.IService;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MainWindowController {

    private IService service;
    private User user;

    public void setServiceAndUser(IService service, User user) throws Exception {
        this.service = service;
        this.user = user;

        eventSignedUpObservableList.setAll(service.getEventsNumber());
        loggedUserLabel.setText("User: " + user.getUsername());
    }

    // Main
    private final ObservableList<EventCountDTO> eventSignedUpObservableList = FXCollections.observableArrayList();
    private final ObservableList<ChildNoEventsDTO> ageGroupEventObservableList = FXCollections.observableArrayList();
    @FXML
    private TableView<EventCountDTO> eventSignedUpTableView;
    @FXML
    private TableColumn<EventCountDTO, Event> eventColumn;
    @FXML
    private TableColumn<EventCountDTO, AgeGroup> ageGroupColumn;
    @FXML
    private TableColumn<EventCountDTO, Long> signedUpColumn;
    @FXML
    private Label loggedUserLabel;
    @FXML
    private Button logoutButton;

    // Entries tab
    @FXML
    private TableView<ChildNoEventsDTO> ageGroupEventTableView;
    @FXML
    private TableColumn<ChildNoEventsDTO, String> nameColumn;
    @FXML
    private TableColumn<ChildNoEventsDTO, Long> ageColumn;
    @FXML
    private TableColumn<ChildNoEventsDTO, Long> noEventsColumn;
    @FXML
    private ChoiceBox<AgeGroup> ageGroupChoiceBox;
    @FXML
    private ChoiceBox<Event> eventChoiceBox;

    // Sign up tab
    @FXML
    private TextField nameTextField;
    @FXML
    private Spinner<Integer> ageSpinner;
    @FXML
    private ChoiceBox<Event> firstEventChoiceBox;
    @FXML
    private ChoiceBox<Event> secondEventChoiceBox;


    public MainWindowController() {

    }

    @FXML
    public void initialize() {
        ageGroupChoiceBox.getItems().setAll(AgeGroup.values());

        eventColumn.setCellValueFactory(new PropertyValueFactory<>("event"));
        ageGroupColumn.setCellValueFactory(new PropertyValueFactory<>("ageGroup"));
        signedUpColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
        eventSignedUpTableView.setItems(eventSignedUpObservableList);

        nameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getChild().getName()));
        ageColumn.setCellValueFactory(cell -> new SimpleLongProperty(cell.getValue().getChild().getAge()).asObject());
        noEventsColumn.setCellValueFactory(new PropertyValueFactory<>("noEvents"));
        ageGroupEventTableView.setItems(ageGroupEventObservableList);

        ageGroupChoiceBox.setOnAction(x -> {
            if (ageGroupChoiceBox.getValue() == AgeGroup.A6_8Y){
                eventChoiceBox.getItems().setAll(Event.E50M, Event.E100M);
            }
            else {
                eventChoiceBox.getItems().setAll(Arrays.stream(Event.values()).filter(e -> e != Event.NONE).collect(Collectors.toList()));
            }
        });

        // Sign up tab
        SpinnerValueFactory<Integer> spinnerRange = new SpinnerValueFactory.IntegerSpinnerValueFactory(6, 15);
        ageSpinner.setValueFactory(spinnerRange);

        firstEventChoiceBox.getItems().setAll(Event.E50M, Event.E100M);
        firstEventChoiceBox.setOnAction(x -> secondEventChoiceBox.getItems().setAll(Arrays.stream(Event.values()).filter(e -> (e != firstEventChoiceBox.getValue() && firstEventChoiceBox.getItems().contains(e)) || e == Event.NONE).collect(Collectors.toList())));

        ageSpinner.valueProperty().addListener((obs, oldAge, newAge) -> {
            if (newAge >= 6 && newAge <= 8){
                firstEventChoiceBox.getItems().setAll(Event.E50M, Event.E100M);
                firstEventChoiceBox.setOnAction(x -> secondEventChoiceBox.getItems().setAll(Arrays.stream(Event.values()).filter(e -> (e != firstEventChoiceBox.getValue() && firstEventChoiceBox.getItems().contains(e)) || e == Event.NONE).collect(Collectors.toList())));
            }
            else{
                firstEventChoiceBox.getItems().setAll(Arrays.stream(Event.values()).filter(e -> e != Event.NONE).collect(Collectors.toList()));
                firstEventChoiceBox.setOnAction(x -> secondEventChoiceBox.getItems().setAll(Arrays.stream(Event.values()).filter(e -> (e != firstEventChoiceBox.getValue() && firstEventChoiceBox.getItems().contains(e)) || e == Event.NONE).collect(Collectors.toList())));
            }
        });

    }

    private void showNotification(String message, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle("Notification");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void ageGroupEventFilterButtonHandler() throws Exception {
        if (ageGroupChoiceBox.getValue() == null || eventChoiceBox.getValue() == null){
            showNotification("Please select an age group and an event!", Alert.AlertType.WARNING);
            return;
        }
        ageGroupEventObservableList.setAll(service.getChildrenNoEvents(eventChoiceBox.getValue(), ageGroupChoiceBox.getValue()));
    }

    @FXML
    public void signUpButtonHandler() {
        String errors = "";
        if (nameTextField.getText().isBlank())
            errors += "Name is empty.\n";
        if (firstEventChoiceBox.getValue() == null)
            errors += "Please choose the first event.\n";
        if (secondEventChoiceBox.getValue() == null)
            errors += "Please choose the second event.\n";
        if (!errors.isEmpty()){
            showNotification(errors, Alert.AlertType.WARNING);
            return;
        }
        try {
            service.addParticipant(nameTextField.getText(), ageSpinner.getValue(), firstEventChoiceBox.getValue(), secondEventChoiceBox.getValue());
            showNotification("Registration complete.", Alert.AlertType.INFORMATION);
            eventSignedUpObservableList.setAll(service.getEventsNumber());
        }
        catch (Exception e) {
            showNotification(e.toString(), Alert.AlertType.ERROR);
        }

    }

    @FXML
    public void logoutButtonHandler(){
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginWindow.fxml"));
            Parent root = loader.load();
            LoginWindowController ctrl = loader.getController();
            ctrl.setService(this.service);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();

            Stage logoutStage = (Stage) logoutButton.getScene().getWindow();
            logoutStage.close();
        }
        catch (Exception e) {
            this.showNotification("Error while opening login window.\n" + e, Alert.AlertType.ERROR);
        }
    }

}
