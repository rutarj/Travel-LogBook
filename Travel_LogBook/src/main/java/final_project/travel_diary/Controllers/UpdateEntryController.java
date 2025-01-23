package final_project.travel_diary.Controllers;
import javafx.scene.control.DatePicker;
import final_project.travel_diary.Models.JournalEntry;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UpdateEntryController {

    public Scene dashboardScene;

    @FXML
    private TextField titleField;

    @FXML
    private DatePicker datePicker; // Use DatePicker instead of TextField for date

    @FXML
    private TextArea descriptionField;

    @FXML
    private HBox imageContainer; // A container to hold the selected images

    private DashboardController dashboardController;
    private JournalEntry journalEntry;
    private List<File> selectedImageFiles;

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    public UpdateEntryController() {
        selectedImageFiles = new ArrayList<>();
    }

    public void setJournalEntry(JournalEntry journalEntry) {
        this.journalEntry = journalEntry;
        titleField.setText(journalEntry.getTitle());
        datePicker.setValue(LocalDate.parse(journalEntry.getDate())); // Set the date value
        descriptionField.setText(journalEntry.getDescription());
        displayExistingImages();
    }

    private void displayExistingImages() {
        imageContainer.getChildren().clear();
        for (String imagePath : new ArrayList<>(journalEntry.getImagePaths())) {
            File file = new File(imagePath);
            if (file.exists()) {
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
                    journalEntry.getImagePaths().remove(imagePath);
                    imageContainer.getChildren().remove(imageView.getParent());
                });

                StackPane stackPane = new StackPane();
                stackPane.getChildren().addAll(imageView, deleteButton);
                StackPane.setAlignment(deleteButton, Pos.TOP_RIGHT);
                stackPane.setStyle("-fx-padding: 5;");

                imageContainer.getChildren().add(stackPane);
            }
        }
    }

    private void displaySelectedImages() {
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
    private void handleUpdateEntry(ActionEvent event) {
        String title = titleField.getText();
        String date = datePicker.getValue().toString(); // Get the date as a string
        String description = descriptionField.getText();

        if (title.isEmpty() || date.isEmpty() || description.isEmpty()) {
            dashboardController.showAlert("Validation Error", "Please fill in all fields.");
            return;
        }

        journalEntry.setTitle(title);
        journalEntry.setDate(date);
        journalEntry.setDescription(description);

        List<String> imagePaths = new ArrayList<>(journalEntry.getImagePaths());
        for (File file : selectedImageFiles) {
            imagePaths.add(file.getAbsolutePath());
        }
        journalEntry.setImagePaths(imagePaths);

        dashboardController.updateJournalEntry();

        // Switch back to the dashboard scene
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(dashboardScene);
        dashboardController.refreshEntries();
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

    @FXML
    private void handleCancel() {
        titleField.clear();
        datePicker.setValue(null);
        descriptionField.clear();
        imageContainer.getChildren().clear();
        selectedImageFiles.clear();
    }

    @FXML
    private void handleBackBtn(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(dashboardScene);
        dashboardController.refreshEntries();
    }

    @FXML
    private void handleExitBtn(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
