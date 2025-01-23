package final_project.travel_diary.Models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JournalEntry {
    private String title;
    private String date;
    private String description;
    private List<String> imagePaths;

    public JournalEntry(String title, String date, String description, List<String> imagePaths) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.imagePaths = new ArrayList<>(imagePaths);
    }

    public JournalEntry(String title, String date, String description, String imagePaths) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.imagePaths = new ArrayList<>(Arrays.asList(imagePaths.split(";")));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    public String getImagePathsAsString() {
        return String.join(";", imagePaths);
    }
}
