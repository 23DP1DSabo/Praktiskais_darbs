import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class Main {
    private static final String USER_FILE = "users.csv";
    private static final String ACCOUNT_FILE = "accounts.csv";
    private static final String TRANSFER_FILE = "transfers.csv";
    private static Scanner scanner = new Scanner(System.in);
    private static Map<String, User> users = new HashMap<>();
    private static User loggedInUser = null;
    private static List<Transfer> transfers = new ArrayList<>();

    public static void main(String[] args) {
        loadUsers();
        loadAccounts();
        loadTransfers();

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
                        saveTransfers();
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
                    case "T":
                        performTransfer();
                        break;
                    case "CC":
                    String choice2 = scanner.nextLine().trim().toUpperCase();
                    switch (choice2) {
                        case "Create debit card":
                            createDebit();
                            break;
                        case "Create credit card":
                            createCredit();
                            break;
                        case "Back to main menu":
                            break;
                    }
                    case "H":
                        showTransferHistory();
                        break;
                    case "L-OUT":
                        System.out.println("Logging out...");
                        loggedInUser = null;
                        break;
                    case "E":
                        saveUsers();
                        saveAccounts();
                        saveTransfers();
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
        System.out.println("T - Transfer money");
        System.out.println("H - View transfer history");
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

    private static void createCredit() {
        return;
    }

    private static void createDebit() {
        return;
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

    private static void loadTransfers() {
        try (BufferedReader br = new BufferedReader(new FileReader(TRANSFER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                transfers.add(Transfer.fromCSV(line));
            }
        } catch (IOException e) {
            System.out.println("No existing transfer data found.");
        }
    }

    private static void saveTransfers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(TRANSFER_FILE))) {
            for (Transfer transfer : transfers) {
                bw.write(transfer.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving transfer data: " + e.getMessage());
        }
    }

    private static void performTransfer() {
        if (loggedInUser.getAccounts().isEmpty()) {
            System.out.println("You need to have at least one account to make transfers.");
            return;
        }

        System.out.println("\nYour accounts:");
        loggedInUser.printAccounts();
        
        System.out.print("\nEnter source account name: ");
        String sourceAccName = scanner.nextLine();
        
        Account sourceAccount = null;
        for (Account acc : loggedInUser.getAccounts()) {
            if (acc.getAccountName().equals(sourceAccName)) {
                sourceAccount = acc;
                break;
            }
        }
        
        if (sourceAccount == null) {
            System.out.println("Account not found!");
            return;
        }

        System.out.print("Enter target account name: ");
        String targetAccName = scanner.nextLine();
        
        Account targetAccount = null;
        for (User user : users.values()) {
            for (Account acc : user.getAccounts()) {
                if (acc.getAccountName().equals(targetAccName)) {
                    targetAccount = acc;
                    break;
                }
            }
            if (targetAccount != null) break;
        }
        
        if (targetAccount == null) {
            System.out.println("Target account not found!");
            return;
        }

        if (sourceAccount == targetAccount) {
            System.out.println("Cannot transfer to the same account!");
            return;
        }

        BigDecimal amount = null;
        while (amount == null) {
            System.out.print("Enter amount to transfer: ");
            if (scanner.hasNextBigDecimal()) {
                amount = scanner.nextBigDecimal();
                scanner.nextLine(); // Consume newline
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("Amount must be positive.");
                    amount = null;
                } else if (amount.compareTo(sourceAccount.getBalance()) > 0) {
                    System.out.println("Insufficient funds.");
                    amount = null;
                }
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next();
            }
        }

        // Perform the transfer
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
        targetAccount.setBalance(targetAccount.getBalance().add(amount));

        // Create and save transfer record
        Transfer transfer = new Transfer(sourceAccName, targetAccName, amount);
        transfer.setStatus("COMPLETED");
        transfers.add(transfer);
        
        saveAccounts();
        saveTransfers();
        
        System.out.println("Transfer completed successfully!");
    }

    private static void showTransferHistory() {
        System.out.println("\nTransfer History:");
        boolean found = false;
        for (Transfer transfer : transfers) {
            if (transfer.getSourceAccountName().equals(loggedInUser.getAccounts().get(0).getAccountName()) ||
                transfer.getTargetAccountName().equals(loggedInUser.getAccounts().get(0).getAccountName())) {
                System.out.printf("From: %s, To: %s, Amount: $%s, Date: %s, Status: %s%n",
                    transfer.getSourceAccountName(),
                    transfer.getTargetAccountName(),
                    transfer.getAmount(),
                    transfer.getTimestamp(),
                    transfer.getStatus());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No transfer history found.");
        }
    }
}