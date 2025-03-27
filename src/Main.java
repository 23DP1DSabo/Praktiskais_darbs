import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class Main {
    private static final String USER_FILE = "users.csv";
    private static final String ACCOUNT_FILE = "accounts.csv";
    private static Scanner scanner = new Scanner(System.in);
    private static Map<String, User> users = new HashMap<>();
    private static User loggedInUser = null;

    public static void main(String[] args) {
        loadUsers();
        loadAccounts();

        boolean running = true;
        while (running) {
            if (loggedInUser == null) {
                showGuestMenu();
            } else {
                showUserMenu();
            }

            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim().toUpperCase();

            if (loggedInUser == null) {
                switch (choice) {
                    case "R":
                        registerUser();
                        break;
                    case "L":
                        login();
                        break;
                    case "E":
                        saveUsers();
                        saveAccounts();
                        System.out.println("Exiting...");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            } else {
                switch (choice) {
                    case "C":
                        createAccount();
                        break;
                    case "V":
                        loggedInUser.printAccounts();
                        break;
                    case "L-OUT":
                        System.out.println("Logging out...");
                        loggedInUser = null;
                        break;
                    case "E":
                        saveUsers();
                        saveAccounts();
                        System.out.println("Exiting...");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            }
        }
        scanner.close();
    }

    private static void showGuestMenu() {
        System.out.println("Welcome! Choose an option:");
        System.out.println("R - Register");
        System.out.println("L - Login");
        System.out.println("E - Exit");
    }

    private static void showUserMenu() {
        System.out.println("Welcome, " + loggedInUser.getUsername() + "! Choose an option:");
        System.out.println("C - Create an account");
        System.out.println("V - View accounts");
        System.out.println("L-OUT - Log out");
        System.out.println("E - Exit");
    }

    private static void loadUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) { // Ensure correct format
                    users.put(parts[0], new User(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            System.out.println("No existing user data found.");
        }
    }

    private static void loadAccounts() {
        try (BufferedReader br = new BufferedReader(new FileReader(ACCOUNT_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String accName = parts[0];
                    String ownerId = parts[1];
                    BigDecimal balance = new BigDecimal(parts[2]);

                    Account account = new Account(accName, ownerId, balance);
                    for (User user : users.values()) {
                        if (user.getUserID().equals(ownerId)) {
                            user.addAccount(account);
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("No existing account data found.");
        }
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
    }

    private static void createAccount() {
        if (loggedInUser == null) {
            System.out.println("Please log in first.");
            return;
        }

        System.out.print("Enter account name: ");
        String accName = scanner.nextLine();
        BigDecimal balance = null;
        while (balance == null) {
            System.out.print("Enter initial balance: ");
            if (scanner.hasNextBigDecimal()) {
                balance = scanner.nextBigDecimal();
                scanner.nextLine(); // Consume newline
                if (balance.compareTo(BigDecimal.ZERO) < 0) {
                    System.out.println("Balance cannot be negative.");
                    balance = null;
                }
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next();
            }
        }

        Account newAccount = new Account(accName, loggedInUser.getUserID(), balance);
        loggedInUser.addAccount(newAccount);
        saveAccounts();
        System.out.println("Account created successfully!");
    }

    private static void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();

        if (!users.containsKey(username) || !users.get(username).getUserID().equals(userId)) {
            System.out.println("Invalid username or user ID.");
            return;
        }

        loggedInUser = users.get(username);
        System.out.println("Welcome, " + username + "!");
    }

    private static void saveUsers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (User user : users.values()) {
                bw.write(user.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving user data: " + e.getMessage());
        }
    }

    private static void saveAccounts() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ACCOUNT_FILE))) {
            for (User user : users.values()) {
                for (Account account : user.getAccounts()) {
                    bw.write(account.toCSV());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving account data: " + e.getMessage());
        }
    }
}