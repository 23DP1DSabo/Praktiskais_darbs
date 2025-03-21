import java.io.*;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class Main {
    private static final String USER_FILE = "users.csv";
    private static Scanner scanner = new Scanner(System.in);
    private static Map<String, User> users = new HashMap<>();
    private static User loggedInUser = null;

    public static void main(String[] args) {
        loadUsers();

        boolean running = true;
        while (running) {
            System.out.println("\nWelcome! Choose an option:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Create an account");
            System.out.println("4. View accounts");
            System.out.println("5. Log out");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    login();
                case 3:
                    createAccount();
                    break;
                case 4:
                    viewAccounts();
                    break;
                case 5:
                    System.out.println("Logging out...");
                    loggedInUser = null;
                    break;
                case 6:
                    saveUsers();
                    loggedInUser = null;
                    System.out.println("Exiting...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
        scanner.close();
    }

    private static void registerUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        String userId = UUID.randomUUID().toString().substring(0, 8);

        if (users.containsKey(username)) {
            System.out.println("Username already exists!");
            return;
        }

        User newUser = new User(username, userId);
        users.put(username, newUser);
        loggedInUser = users.get(username);
        saveUsers();
        System.out.println("User registered successfully! ID: " + userId);
        System.out.println("Welcome, " + username + "!");
    }

    private static void createAccount() {
        if (loggedInUser == null) {
            System.out.println("Please log in first.");
            return;
        }
        
        System.out.print("Enter account number: ");
        String accNumber = scanner.nextLine();
        System.out.print("Enter initial balance: ");
        String balance = scanner.nextLine();

        if (!balance.matches("\\d+\\.\\d{1,2}")) {
            System.out.println("Invalid balance format. Must have at most two decimal places.");
            return;
        }

        Account newAccount = new Account(accNumber, loggedInUser.getUsername(), balance);
        loggedInUser.addAccount(newAccount);
        saveUsers();
        System.out.println("Account created successfully!");
    }

    private static void viewAccounts() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        if (!users.containsKey(username)) {
            System.out.println("User not found!");
            return;
        }
        users.get(username).printAccounts();
    }

    private static void loadUsers() {
        try (BufferedReader br = Helper.getReader(USER_FILE)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String username = parts[0];
                    String userId = parts[1];
                    User user = new User(username, userId);
                    
                    for (int i = 2; i < parts.length; i += 2) {
                        if (i + 1 < parts.length) {
                            user.addAccount(new Account(parts[i], username, parts[i + 1]));
                        }
                    }
                    users.put(username, user);
                }
            }
        } catch (IOException e) {
            System.out.println("No existing user data found.");
        }
    }

    private static void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        if (!users.containsKey(username)) {
            System.out.println("User not found!");
            return;
        }
        loggedInUser = users.get(username);
        System.out.println("Welcome, " + username + "!");
    }

    private static void saveUsers() {
        try (BufferedWriter bw = Helper.getWriter(USER_FILE, StandardOpenOption.CREATE)) {
            for (User user : users.values()) {
                bw.write(user.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving user data: " + e.getMessage());
        }
    }
}