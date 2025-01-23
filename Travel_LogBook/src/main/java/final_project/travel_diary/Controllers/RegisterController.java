package final_project.travel_diary.Controllers;

import final_project.travel_diary.Models.Database;
import final_project.travel_diary.Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private void handleRegisterButtonAction(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Check if username already exists
        if (Database.getUser(username) != null) {
            showErrorAlert("Username already exists. Please choose a different username.");
            clearFields();
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            showErrorAlert("Passwords do not match. Please re-enter your passwords.");
            clearFields();
            return;
        }

        // If no errors, add user to the database
        User newUser = new User(username, password);
        Database.addUser(newUser);
        showInformationAlert("Registration successful! You can now login.");
        loadFXML("/final_project/travel_diary/login.fxml", "Login", event);
        clearFields();
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        // Implement cancel action or switch to another view
        System.out.println("Canceling registration");
    }

    @FXML
    private void handleBackBtn(ActionEvent event) {
        loadFXML("/final_project/travel_diary/auth.fxml", "Home", event);
    }

    @FXML
    private void handleExitBtn(ActionEvent event) {
        // Close the application
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

    private void loadFXML(String fxmlFile, String title, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setTitle(title);
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInformationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }
}
