package final_project.travel_diary.Models;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Database {
    private static final Map<String, User> users = new HashMap<>();
    private static User currentUser;

    private Database() {
    }

    public static void addUser(User user) {
        users.put(user.getUsername(), user);
        saveUserToFile(user);
        saveCredentials(user);
    }

    public static User getUser(String name) {
        return users.get(name);
    }

    public static void removeUser(String name) {
        users.remove(name);
        deleteUserFile(name);
        deleteCredentialsFile(name);
    }

    public static Map<String, User> getAllUsers() {
        return users;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void saveUserToFile(User user) {
        try {
            User.saveToFile(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static User loadUserFromFile(String username) {
        try {
            return User.loadFromFile(username);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void loadAllUsers() {
        File directory = new File("data");
        if (!directory.exists() || !directory.isDirectory()) {
            return;
        }
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files != null) {
            for (File file : files) {
                try {
                    User user = User.loadFromFile(file.getName().replace(".txt", ""));
                    if (user != null) {
                        users.put(user.getUsername(), user);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void deleteUserFile(String username) {
        File file = new File("data", username + ".txt");
        if (file.exists()) {
            file.delete();
        }
    }

    public static void saveCredentials(User user) {
        File directory = new File("credentials");
        if (!directory.exists()) {
            directory.mkdir();
        }
        File file = new File(directory, user.getUsername() + ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(user.getUsername());
            writer.newLine();
            writer.write(user.getHashedPassword());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static User loadCredentials(String username) {
        File file = new File("credentials", username + ".txt");
        if (!file.exists()) {
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String usernameFromFile = reader.readLine();
            String hashedPassword = reader.readLine();
            User user = new User(usernameFromFile, "");
            user.hashedPassword = hashedPassword;
            return user;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void deleteCredentialsFile(String username) {
        File file = new File("credentials", username + ".txt");
        if (file.exists()) {
            file.delete();
        }
    }
}
