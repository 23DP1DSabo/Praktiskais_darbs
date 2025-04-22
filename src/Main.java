import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class Main {
    private static final String USER_FILE = "data/users.csv";
    private static final String ACCOUNT_FILE = "data/accounts.csv";
    private static final String TRANSFER_FILE = "data/transfers.csv";
    private static Scanner scanner = new Scanner(System.in);
    private static Map<String, User> users = new HashMap<>();
    private static User loggedInUser = null;
    private static List<Transfer> transfers = new ArrayList<>();

    public static void main(String[] args) {
        loadCards();
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
                    case "S":
                        System.out.println("\nSort options:");
                        System.out.println("1 - Sort by balance (highest to lowest)");
                        System.out.println("2 - Sort by balance (lowest to highest)");
                        System.out.print("Enter your choice: ");
                        String sortChoice = scanner.nextLine();
                        switch (sortChoice) {
                            case "1":
                                loggedInUser.printAccountsSortedByBalanceDescending();
                                break;
                            case "2":
                                loggedInUser.printAccountsSortedByBalanceAscending();
                                break;
                            default:
                                System.out.println("Invalid sort option.");
                        }
                        break;
                    case "T":
                        performTransfer();
                        break;
                    case "CC":
                        showCardMenu();
                        break;
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
        System.out.println("S - Sort accounts");
        System.out.println("T - Transfer money");
        System.out.println("CC - Card Management");
        System.out.println("H - View transfer history");
        System.out.println("L-OUT - Log out");
        System.out.println("E - Exit");
    }

    private static void showCardMenu() {
        System.out.println("Card Management Menu:");
        System.out.println("1 - Create Debit Card");
        System.out.println("2 - Create Credit Card");
        System.out.println("3 - Back to Main Menu");
        String card_choice = scanner.nextLine().trim().toUpperCase();
        switch (card_choice) {
            case "1":
                createDebit();
                break;
            case "2":
                createCredit();
                break;
            case "3":
                break;
        }
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
        if (loggedInUser == null) {
            System.out.println("Please log in first.");
            return;
        }

        if (loggedInUser.getAccounts().isEmpty()) {
            System.out.println("You need to have at least one account to create a credit card.");
            return;
        }

        System.out.println("Your accounts:");
        loggedInUser.printAccounts();
        System.out.print("Enter account name to link the card to: ");
        String accName = scanner.nextLine();

        Account selectedAccount = null;
        for (Account acc : loggedInUser.getAccounts()) {
            if (acc.getAccountName().equals(accName)) {
                selectedAccount = acc;
                break;
            }
        }

        if (selectedAccount == null) {
            System.out.println("Account not found.");
            return;
        }

        System.out.print("Enter PIN (4 digits): ");
        String pin = scanner.nextLine();
        while (!pin.matches("\\d{4}")) {
            System.out.println("PIN must be 4 digits. Try again: ");
            pin = scanner.nextLine();
        }

        BigDecimal dailyLimit = null;
        while (dailyLimit == null) {
            System.out.print("Enter daily spending limit: ");
            if (scanner.hasNextBigDecimal()) {
                dailyLimit = scanner.nextBigDecimal();
                scanner.nextLine(); // Consume newline
                if (dailyLimit.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("Daily limit must be positive.");
                    dailyLimit = null;
                }
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next();
            }
        }

        BigDecimal creditLimit = null;
        while (creditLimit == null) {
            System.out.print("Enter credit limit: ");
            if (scanner.hasNextBigDecimal()) {
                creditLimit = scanner.nextBigDecimal();
                scanner.nextLine(); // Consume newline
                if (creditLimit.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("Credit limit must be positive.");
                    creditLimit = null;
                }
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next();
            }
        }

        BigDecimal interestRate = BigDecimal.valueOf(0.25);
        System.out.print("Annual interest rate is 25%");

        String cardNumber = generateCardNumber();
        CreditCard newCard = new CreditCard(cardNumber, selectedAccount, pin, dailyLimit, creditLimit, interestRate);
        loggedInUser.addCard(newCard);
        saveCards();
        System.out.println("Credit card created successfully!");
        System.out.println("Card Number: " + cardNumber);
    }

    private static void createDebit() {
        if (loggedInUser == null) {
            System.out.println("Please log in first.");
            return;
        }

        if (loggedInUser.getAccounts().isEmpty()) {
            System.out.println("You need to have at least one account to create a debit card.");
            return;
        }

        System.out.println("Your accounts:");
        loggedInUser.printAccounts();
        System.out.print("Enter account name to link the card to: ");
        String accName = scanner.nextLine();

        Account selectedAccount = null;
        for (Account acc : loggedInUser.getAccounts()) {
            if (acc.getAccountName().equals(accName)) {
                selectedAccount = acc;
                break;
            }
        }

        if (selectedAccount == null) {
            System.out.println("Account not found.");
            return;
        }

        System.out.print("Enter PIN (4 digits): ");
        String pin = scanner.nextLine();
        while (!pin.matches("\\d{4}")) {
            System.out.println("PIN must be 4 digits. Try again: ");
            pin = scanner.nextLine();
        }

        String cardNumber = generateCardNumber();
        // BigDecimal DailyLimit = new BigDecimal(""); // Default daily limit of 1000
        BigDecimal dailyLimit = null;
        while (dailyLimit == null) {
            System.out.print("Enter daily spending limit: ");
            if (scanner.hasNextBigDecimal()) {
                dailyLimit = scanner.nextBigDecimal();
                scanner.nextLine(); // Consume newline
                if (dailyLimit.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("Daily limit must be positive.");
                    dailyLimit = null;
                }
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next();
            }
        }
        DebitCard newCard = new DebitCard(cardNumber, selectedAccount, pin, dailyLimit);
        loggedInUser.addCard(newCard);
        saveCards();
        System.out.println("Debit card created successfully!");
        System.out.println("Card Number: " + cardNumber);
    }

    private static String generateCardNumber() {
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            cardNumber.append(random.nextInt(10));
            if ((i + 1) % 4 == 0 && i != 15) {
                cardNumber.append(" ");
            }
        }
        return cardNumber.toString();
    }

    private static void saveCards() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/cards.csv"))) {
            for (User user : users.values()) {
                for (Card card : user.getCards()) {
                    bw.write(card.toCSV());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving card data: " + e.getMessage());
        }
    }

    private static void loadCards() {
        try (BufferedReader br = new BufferedReader(new FileReader("data/cards.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String cardNumber = parts[0];
                    String accountName = parts[1];
                    String cardType = parts[2];
                    boolean isActive = Boolean.parseBoolean(parts[3]);
                    String pin = parts[4];

                    // Find the account
                    Account linkedAccount = null;
                    for (User user : users.values()) {
                        for (Account acc : user.getAccounts()) {
                            if (acc.getAccountName().equals(accountName)) {
                                linkedAccount = acc;
                                break;
                            }
                        }
                        if (linkedAccount != null) break;
                    }

                    if (linkedAccount != null) {
                        Card card;
                        if (cardType.equals("DEBIT")) {
                            BigDecimal dailyLimit = new BigDecimal(parts[5]);
                            card = new DebitCard(cardNumber, linkedAccount, pin, dailyLimit);
                        } else {
                            BigDecimal dailyLimit = new BigDecimal(parts[5]);
                            BigDecimal creditLimit = new BigDecimal(parts[6]);
                            BigDecimal interestRate = new BigDecimal(parts[7]);
                            card = new CreditCard(cardNumber, linkedAccount, pin, dailyLimit, creditLimit, interestRate);
                        }
                        card.setActive(isActive);
                        
                        // Find the user who owns the account
                        for (User user : users.values()) {
                            if (user.getAccounts().contains(linkedAccount)) {
                                user.addCard(card);
                                break;
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("No existing card data found.");
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
                    System.out.println("Insufficient funds. Transfer marked as incomplete.");
                    Transfer transfer = new Transfer(sourceAccName, targetAccName, amount);
                    transfer.setStatus("INCOMPLETE");
                    transfers.add(transfer);
                    saveTransfers();
                    return;
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