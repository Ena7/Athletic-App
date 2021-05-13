import domain.AgeGroup;
import domain.Event;
import domain.User;
import dtos.ChildNoEventsDTO;
import dtos.EventCountDTO;
import javafx.application.Platform;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.Serializable;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainWindowController extends UnicastRemoteObject implements IObserver, Serializable {

    private IServiceServer service;
    private User user;

    public void setService(IServiceServer service) {
        this.service = service;
    }

    public void setUser(User user) {
        this.user = user;
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


    public MainWindowController() throws Exception {

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

        eventChoiceBox.setOnAction(y -> {
            if (eventChoiceBox.getValue() != null) {
                try {
                    ageGroupEventObservableList.setAll(service.getChildrenNoEvents(eventChoiceBox.getValue(), ageGroupChoiceBox.getValue()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        }
        catch (Exception e) {
            showNotification(e.toString(), Alert.AlertType.ERROR);
        }

    }

    void logout() {
        try {
            service.logout(user, this);
            System.exit(0);
        } catch (Exception e) {
            System.out.println("Logout error " + e);
        }
    }

    @FXML
    public void logoutButtonHandler(){
        logout();
    }

    @Override
    public void refreshEvents(List<EventCountDTO> lst) {
        Platform.runLater(() -> eventSignedUpObservableList.setAll(lst));
    }

    public void setEvents() throws Exception {
        eventSignedUpObservableList.setAll(service.getEventsNumber());
    }
}
