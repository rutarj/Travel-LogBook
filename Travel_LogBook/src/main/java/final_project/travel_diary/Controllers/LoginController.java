package final_project.travel_diary.Controllers;

import final_project.travel_diary.Models.Database;
import final_project.travel_diary.Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Check if user exists in the database
        User user = Database.getUser(username);
        if (user == null) {
            user = Database.loadCredentials(username);
            if (user != null) {
                Database.addUser(user);
            }
        }

        if (user == null) {
            // Show error if user does not exist
            showError("User not found", "User with username '" + username + "' does not exist.");
            return;
        }

        // Check if password matches
        if (!user.checkPassword(password)) {
            // Show error if password is incorrect
            showError("Incorrect password", "The password entered is incorrect.");
            return;
        }

        // If login successful, set the current user and navigate to the dashboard
        Database.setCurrentUser(user);

        // Load user's journal entries from file
        user = Database.loadUserFromFile(username);
        Database.setCurrentUser(user);

        loadFXML("/final_project/travel_diary/Dashboard.fxml", "Main", event);
    }

    @FXML
    private void handleRegisterButtonAction(ActionEvent event) {
        // Switch to register view
        loadFXML("/final_project/travel_diary/register.fxml", "Register", event);
    }

    @FXML
    private void handleBackBtn(ActionEvent event) {
        // Navigate back to the previous view (auth screen)
        loadFXML("/final_project/travel_diary/auth.fxml", "Authentication", event);
    }

    @FXML
    private void handleExitBtn(ActionEvent event) {
        // Close the application
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void loadFXML(String fxmlFile, String title, ActionEvent event) {
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

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
