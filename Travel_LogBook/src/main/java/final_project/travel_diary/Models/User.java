package final_project.travel_diary.Models;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class User implements Serializable {
    private String username;
    String hashedPassword;
    private List<JournalEntry> journalEntries;

    public User(String username, String password) {
        this.username = username;
        this.hashedPassword = hashPassword(password);
        this.journalEntries = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public List<JournalEntry> getJournalEntries() {
        return journalEntries;
    }

    public void addJournalEntry(JournalEntry entry) {
        journalEntries.add(entry);
    }

    public void removeJournalEntry(JournalEntry entry) {
        journalEntries.remove(entry);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkPassword(String password) {
        String hashed = hashPassword(password);
        return hashed.equals(this.hashedPassword);
    }

    public static void saveToFile(User user) throws IOException {
        File directory = new File("data");
        if (!directory.exists()) {
            directory.mkdir();
        }

        File file = new File(directory, user.getUsername() + ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (JournalEntry entry : user.getJournalEntries()) {
                writer.write(entry.getTitle() + ";" + entry.getDate() + ";" + entry.getDescription() + ";" + entry.getImagePathsAsString());
                writer.newLine();
            }
        }
    }

    public static User loadFromFile(String username) throws IOException {
        File file = new File("data", username + ".txt");
        if (!file.exists()) {
            return null;
        }

        User user = Database.loadCredentials(username);
        if (user == null) {
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 3) {
                    String title = parts[0];
                    String date = parts[1];
                    String description = parts[2];

                    List<String> imagePaths = Collections.emptyList();
                    if (parts.length > 3) {
                        imagePaths = Arrays.asList(parts).subList(3, parts.length);
                    }

                    user.addJournalEntry(new JournalEntry(title, date, description, imagePaths));
                }
            }
        }
        return user;
    }
}
