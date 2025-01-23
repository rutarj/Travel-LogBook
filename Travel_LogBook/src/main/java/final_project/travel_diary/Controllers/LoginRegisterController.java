package final_project.travel_diary.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginRegisterController {
    @FXML
    public VBox vbox;
    public Button registerBtn;

    @FXML
    private void handleLoginBtn(ActionEvent event) {
        loadFXML("/final_project/travel_diary/login.fxml", "Login", event);
    }

    @FXML
    private void handleRegisterBtn(ActionEvent event) {
        loadFXML("/final_project/travel_diary/register.fxml", "Register", event);
    }

    @FXML
    private void handleBackBtn(ActionEvent event) {
        loadFXML("jobPortal.fxml", "Home", event);
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
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}