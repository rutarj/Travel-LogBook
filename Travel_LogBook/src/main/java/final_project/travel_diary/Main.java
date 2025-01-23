package final_project.travel_diary;
import final_project.travel_diary.Models.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Load all users from files
        Database.loadAllUsers();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/final_project/travel_diary/auth.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            primaryStage.setTitle("Travel Diary");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
