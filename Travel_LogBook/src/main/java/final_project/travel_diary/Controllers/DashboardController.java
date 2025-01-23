package final_project.travel_diary.Controllers;

import final_project.travel_diary.Models.Database;
import final_project.travel_diary.Models.JournalEntry;
import final_project.travel_diary.Models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;

public class DashboardController {

    @FXML
    private TableView<JournalEntry> journalTable;

    @FXML
    private TableColumn<JournalEntry, String> titleColumn;

    @FXML
    private TableColumn<JournalEntry, String> dateColumn;

    @FXML
    private TableColumn<JournalEntry, String> descriptionColumn;

    private ObservableList<JournalEntry> journalEntries;

    private User current;

    public DashboardController() {
        User currentUser = Database.getCurrentUser();
        this.current=currentUser;
        if (currentUser != null) {
            currentUser = Database.loadUserFromFile(currentUser.getUsername());
            Database.setCurrentUser(currentUser);  // Ensure Database has the updated current user
            if (currentUser != null) {
                journalEntries = FXCollections.observableArrayList(currentUser.getJournalEntries());
            } else {
                journalEntries = FXCollections.observableArrayList();
            }
        } else {
            journalEntries = FXCollections.observableArrayList();
        }
    }

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        loadJournalEntries();

        journalTable.setRowFactory(tv -> {
            TableRow<JournalEntry> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    JournalEntry rowData = row.getItem();
                    showJournalEntryDetail(rowData);
                }
            });
            return row;
        });
    }

    private void loadJournalEntries() {
        journalTable.setItems(journalEntries);
    }

    private void showJournalEntryDetail(JournalEntry journalEntry) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/final_project/travel_diary/JournalEntryDetailView.fxml"));
            Parent root = loader.load();
            JournalEntryDetailController controller = loader.getController();
            controller.setJournalEntry(journalEntry);

            Stage stage = new Stage();
            controller.setStage(stage);
            stage.setTitle("Journal Entry Detail");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshEntries() {
        journalTable.setItems(FXCollections.observableArrayList(Database.getCurrentUser().getJournalEntries()));
        journalTable.refresh();
    }

    @FXML
    private void showAddEntryView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/final_project/travel_diary/AddEntryView.fxml"));
            Parent root = loader.load();
            AddEntryController addEntryController = loader.getController();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            addEntryController.dashboardScene = stage.getScene();
            addEntryController.setDashboardController(this);

            Scene newScene = new Scene(root);
            stage.setTitle("Add Entry");
            stage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void addJournalEntry(JournalEntry entry) {
        journalEntries.add(entry);
        refreshEntries();
        Database.saveUserToFile(Database.getCurrentUser());
    }

    public void updateJournalEntry() {
        refreshEntries();
        Database.saveUserToFile(Database.getCurrentUser());
    }

    @FXML
    private void handleBackBtn(ActionEvent event) {
        loadFXML("/final_project/travel_diary/auth.fxml", "Authentication", event);
    }

    @FXML
    private void handleExitBtn(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleUpdateEntry(ActionEvent event) {
        JournalEntry selectedEntry = journalTable.getSelectionModel().getSelectedItem();
        if (selectedEntry != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/final_project/travel_diary/UpdateEntryView.fxml"));
                Parent root = loader.load();
                UpdateEntryController controller = loader.getController();
                controller.setJournalEntry(selectedEntry);
//                controller.setJournalEntry(this.current.getJournalEntries());
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                controller.dashboardScene = stage.getScene();
                controller.setDashboardController(this);

                Scene newScene = new Scene(root);
                stage.setTitle("Update Entry");
                stage.setScene(newScene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("No Selection", "Please select a journal entry to update.");
        }
    }

    @FXML
    private void handleDeleteEntry() {
        JournalEntry selectedEntry = journalTable.getSelectionModel().getSelectedItem();
        if (selectedEntry != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this journal entry?");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    User currentUser = Database.getCurrentUser();
                    currentUser.removeJournalEntry(selectedEntry);
                    Database.saveUserToFile(currentUser); // Save user data to file
                    journalEntries.remove(selectedEntry);
                    refreshEntries();
                }
            });
        } else {
            showAlert("No Selection", "Please select a journal entry to delete.");
        }
    }

    public void loadFXML(String fxmlFile, String title, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene newScene = new Scene(root);
            stage.setTitle(title);
            stage.setScene(newScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
