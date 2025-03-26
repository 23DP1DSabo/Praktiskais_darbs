import java.io.*;
import java.math.BigDecimal;
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
            System.out.println("R - Register");
            System.out.println("Li - Login");
            System.out.println("C - Create an account");
            System.out.println("V - View accounts");
            System.out.println("Lo - Log out");
            System.out.println("E - Exit");
            System.out.print("Enter choice: ");
            
            String choice = scanner.next();
            scanner.nextLine();

            if (choice.equalsIgnoreCase("R")) {
                    registerUser();
                    break;
            }
            if (choice.equalsIgnoreCase("Li")) {
                    login();
            }
            if (choice.equalsIgnoreCase("C")) {
                    createAccount();
                    break;
            }
            if (choice.equalsIgnoreCase("View")) {
                    viewAccounts();
                    break;
            }
            if (choice.equalsIgnoreCase("Lo")) {
                    System.out.println("Logging out...");
                    loggedInUser = null;
                    break;
            }
            if (choice.equalsIgnoreCase("E")) {
                    saveUsers();
                    loggedInUser = null;
                    System.out.println("Exiting...");
                    running = false;
                    break;
            }

            else {
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
        
        System.out.print("Enter user number: ");
        String accNumber = scanner.nextLine();
        System.out.print("Enter initial balance: ");
        BigDecimal balance = null;
        while (balance == null) {
            System.out.print("Enter initial balance: ");
            if (scanner.hasNextBigDecimal()) {
                balance = scanner.nextBigDecimal();
                if (balance.compareTo(BigDecimal.ZERO) < 0) {
                    System.out.println("Balance cannot be negative.");
                    balance = null;
                }
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next(); // Consume invalid input
            }
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
                            BigDecimal balance = new BigDecimal(parts[i + 1]);
                            user.addAccount(new Account(parts[i], username, balance));
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