package final_project.travel_diary.Controllers;

import final_project.travel_diary.Models.Database;
import final_project.travel_diary.Models.JournalEntry;
import final_project.travel_diary.Models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddEntryController {

    public Scene dashboardScene;

    @FXML
    private TextField titleField;

    @FXML
    private TextField dateField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextArea descriptionField;

    @FXML
    private HBox imageContainer; // A container to hold the selected images

    private DashboardController dashboardController;
    private List<File> selectedImageFiles;

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    public AddEntryController() {
        selectedImageFiles = new ArrayList<>();
    }

@FXML
private void handleAddEntry(ActionEvent event) {
    try {
        String title = titleField.getText();
        String date = datePicker.getValue().toString(); // Get the date as a string
        String description = descriptionField.getText();

        if (title.isEmpty() || date.isEmpty() || description.isEmpty()) {
            dashboardController.showAlert("Validation Error", "Please fill in all fields.");
            return;
        }

        List<String> imagePaths = new ArrayList<>();
        for (File file : selectedImageFiles) {
            imagePaths.add(file.getAbsolutePath());
        }
        JournalEntry newEntry = new JournalEntry(title, date, description, String.join(";", imagePaths));
        User currentUser = Database.getCurrentUser();
        currentUser.addJournalEntry(newEntry);
        Database.saveUserToFile(currentUser); // Save user data to file
        dashboardController.addJournalEntry(newEntry); // Add entry using instance method

        // Switch back to the dashboard scene
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(dashboardScene);
        dashboardController.refreshEntries();
    } catch (Exception e) {
        e.printStackTrace();
        dashboardController.showAlert("Error", "An unexpected error occurred: " + e.getMessage());
    }
}

    @FXML
    private void handleChooseImages() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Images");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        List<File> files = fileChooser.showOpenMultipleDialog(null);
        if (files != null) {
            selectedImageFiles.addAll(files);
            displaySelectedImages();
        }
    }

    private void displaySelectedImages() {
        imageContainer.getChildren().clear();
        for (File file : new ArrayList<>(selectedImageFiles)) {
            Image image = new Image(file.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            imageView.setPreserveRatio(true);

            Image closeImage = new Image(getClass().getResource("/images/cross-mark.png").toExternalForm());
            ImageView closeImageView = new ImageView(closeImage);
            closeImageView.setFitWidth(15);
            closeImageView.setFitHeight(15);

            Button deleteButton = new Button();
            deleteButton.setGraphic(closeImageView);
            deleteButton.setStyle("-fx-background-color: transparent;");
            deleteButton.setOnAction(e -> {
                selectedImageFiles.remove(file);
                imageContainer.getChildren().remove(imageView.getParent());
            });

            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(imageView, deleteButton);
            StackPane.setAlignment(deleteButton, Pos.TOP_RIGHT);
            stackPane.setStyle("-fx-padding: 5;");

            imageContainer.getChildren().add(stackPane);
        }
    }

    @FXML
    private void handleCancel() {
        titleField.clear();
        dateField.clear();
        descriptionField.clear();
        imageContainer.getChildren().clear();
        selectedImageFiles.clear();
    }

    @FXML
    private void handleBackBtn(ActionEvent event) {
        dashboardController.loadFXML("/final_project/travel_diary/Dashboard.fxml", "Dashboard", event);
    }

    @FXML
    private void handleExitBtn(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void loadFXML(String fxmlFile, String title, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene newScene = dashboardScene;
            stage.setTitle(title);
            stage.setScene(newScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
