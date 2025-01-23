package final_project.travel_diary.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import final_project.travel_diary.Models.JournalEntry;

import java.io.File;

public class JournalEntryDetailController {

    @FXML
    private Label titleLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private HBox imageContainer;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setJournalEntry(JournalEntry journalEntry) {
        titleLabel.setText(journalEntry.getTitle());
        dateLabel.setText(journalEntry.getDate());
        descriptionLabel.setText(journalEntry.getDescription());

        imageContainer.getChildren().clear();
        for (String imagePath : journalEntry.getImagePaths()) {
            File file = new File(imagePath);
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(200);
                imageView.setFitHeight(200);
                imageView.setPreserveRatio(true);
                imageContainer.getChildren().add(imageView);
            }
        }
    }

    @FXML
    private void handleClose() {
        if (stage != null) {
            stage.close();
        }
    }
}
