module final_project.travel_diary {
    requires javafx.controls;
    requires javafx.fxml;


    opens final_project.travel_diary to javafx.fxml;
    opens final_project.travel_diary.Controllers to javafx.fxml;
    exports final_project.travel_diary;
    exports final_project.travel_diary.Controllers;
    exports final_project.travel_diary.Models;
}