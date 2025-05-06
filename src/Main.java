import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.time.LocalDateTime;

public class Main {
    private static final String USER_FILE = "data/users.csv";
    private static final String ACCOUNT_FILE = "data/accounts.csv";
    private static final String TRANSFER_FILE = "data/transfers.csv";
    private static final String LOAN_FILE = "data/loans.csv";
    private static Scanner scanner = new Scanner(System.in);
    private static Map<String, User> users = new HashMap<>();
    private static User loggedInUser = null;
    private static List<Transfer> transfers = new ArrayList<>();

    public static void main(String[] args) {
        loadUsers();
        loadAccounts();
        loadTransfers();
        loadLoans();

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
                        saveLoans();
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
                                loggedInUser.SortedByBalanceDescending();
                                break;
                            case "2":
                                loggedInUser.SortedByBalanceAscending();
                                break;
                            default:
                                System.out.println("Invalid sort option.");
                        }
                        break;
                    case "T":
                        performTransfer();
                        break;
                    case "D":
                        performDeposit();
                        break;
                    case "W":
                        performWithdraw();
                        break;
                    case "CC":
                        showCardMenu();
                        break;
                    case "L":
                        showLoanMenu();
                        break;
                    case "IC":
                        InvestCalc.calcInvestment();
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
                        saveLoans();
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

    public static void printMenuTable(String[][] options) {
        int col1Width = 0;
        int col2Width = 0;
        
        for (String[] row : options) {
            col1Width = Math.max(col1Width, row[0].length());
            col2Width = Math.max(col2Width, row[1].length());
        }
    
        col1Width = Math.max(col1Width, 20);
        col2Width = Math.max(col2Width, 5);
    
        String format = "| %-"+ (col1Width) +"s | %-"+ (col2Width) +"s |%n";
        String line = "+" + "-".repeat(col1Width + 2) + "+" + "-".repeat(col2Width + 2) + "+";
    
        System.out.println(line);
        System.out.printf(format, "Option desc.", "Symbol");
        System.out.println(line);
        
        for (String[] row : options) {
            System.out.printf(format, row[0], row[1]);
        }
        
        System.out.println(line);
    }    

    private static void showGuestMenu() {
        String[][] menuItems = {
        {"Register", "R"},
        {"Login", "L"},
        {"Exit", "E"}
    };
    System.out.println("\033[H\033[2J");
    System.out.flush();
    System.out.println("Welcome to the Banking System!");
    printMenuTable(menuItems);
    }

    public static void showUserMenu() {
        String[][] menuItems = {
            {"Create an account", "C"},
            {"View accounts", "V"},
            {"Sort accounts", "S"},
            {"Transfer money", "T"},
            {"Deposit money", "D"},
            {"Withdraw money", "W"},
            {"Card Management", "CC"},
            {"Loan Management", "L"},
            {"Investment Calculator", "IC"},
            {"View transfer history", "H"},
            {"Log out", "L-OUT"},
            {"Exit the program", "E"}
        };
        System.out.println("\033[H\033[2J");
        System.out.flush();
        System.out.println("Welcome, " + loggedInUser.getUsername() + "!");
        printMenuTable(menuItems);
    }

    private static void showCardMenu() {
        String[][] menuItems = {
        {"Card Management Menu", ""},
        {"Create Debit Card", "1"},
        {"Make Payment", "2"},
        {"Delete Card", "3"},
        {"Back to Main Menu", "4"}
        };
        System.out.println("\033[H\033[2J");
        System.out.flush();
        printMenuTable(menuItems);

        String card_choice = scanner.nextLine().trim().toUpperCase();
        switch (card_choice) {
            case "1":
                createDebit();
                break;
            case "2":
                makeCardPayment();
                break;
            case "3":
                deleteCard();
                break;
            case "4":
                break;
        }
    }

    private static void showLoanMenu() {
        String[][] menuItems = {
        {"Create Loan", "1"},
        {"View Loans", "2"},
        {"Make Loan Payment", "3"}, 
        {"Back to Main Menu", "4"}
        };
        System.out.println("\033[H\033[2J");
        System.out.flush();
        System.oit.println("Loan management Menu");
        printMenuTable(menuItems);

        String loan_choice = scanner.nextLine().trim();
        switch (loan_choice) {
            case "1":
                createLoan();
                break;
            case "2":
                loggedInUser.printLoans();
                break;
            case "3":
                if (loggedInUser.getLoans().isEmpty()) {
                    System.out.println("You have no loans.");
                    return;
                }

                System.out.println("Your loans:");
                loggedInUser.printLoans();
                System.out.print("Enter loan ID to make payment: ");
                String loanId = scanner.nextLine();

                Loan selectedLoan = null;
                for (Loan loan : loggedInUser.getLoans()) {
                    if (loan.getLoanId().equals(loanId)) {
                        selectedLoan = loan;
                        break;
                    }
                }

                if (selectedLoan == null) {
                    System.out.println("Loan not found.");
                    return;
                }

                Account linkedAccount = null;
                for (Account acc : loggedInUser.getAccounts()) {
                    if (acc.getAccountName().equals(selectedLoan.getAccountName())) {
                        linkedAccount = acc;
                        break;
                    }
                }

                if (linkedAccount == null) {
                    System.out.println("Linked account not found.");
                    return;
                }

                Loan.processPayment(selectedLoan, linkedAccount, scanner);
                saveAccounts();
                saveLoans();
                break;
            case "4":
                break;
        }
    }

    private static void createLoan() {
        if (loggedInUser == null) {
            System.out.println("Please log in first.");
            return;
        }

        if (loggedInUser.getAccounts().isEmpty()) {
            System.out.println("You need to have at least one account to create a loan.");
            return;
        }

        System.out.println("Your accounts:");
        loggedInUser.printAccounts();
        System.out.print("Enter account name to link the loan to: ");
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

        BigDecimal principal = null;
        while (principal == null) {
            System.out.print("Enter loan amount: ");
            if (scanner.hasNextBigDecimal()) {
                principal = scanner.nextBigDecimal();
                scanner.nextLine(); // Consume newline
                if (principal.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("Loan amount must be positive.");
                    principal = null;
                }
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next();
            }
        }

        int termMonths = 0;
        while (termMonths <= 0) {
            System.out.print("Enter loan term in months: ");
            if (scanner.hasNextInt()) {
                termMonths = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (termMonths <= 0) {
                    System.out.println("Term must be positive.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next();
            }
        }

        BigDecimal interestRate = BigDecimal.valueOf(0.10); // 10% annual interest rate
        System.out.println("Annual interest rate is 10%");

        Loan newLoan = new Loan(accName, principal, interestRate, termMonths);
        loggedInUser.addLoan(newLoan);
        saveLoans();
        System.out.println("Loan created successfully!");
        System.out.println("Loan ID: " + newLoan.getLoanId());
        System.out.printf("Monthly Payment: €%s%n", newLoan.getMonthlyPayment());
    }

    private static void loadLoans() {
        try (BufferedReader br = new BufferedReader(new FileReader(LOAN_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                Loan loan = Loan.fromCSV(line);
                for (User user : users.values()) {
                    for (Account acc : user.getAccounts()) {
                        if (acc.getAccountName().equals(loan.getAccountName())) {
                            user.addLoan(loan);
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("No existing loan data found.");
        }
    }

    private static void saveLoans() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(LOAN_FILE))) {
            for (User user : users.values()) {
                for (Loan loan : user.getLoans()) {
                    bw.write(loan.toCSV());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving loan data: " + e.getMessage());
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
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/cards.csv", true))) {
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

    public static void loadCards() {
        try (BufferedReader br = new BufferedReader(new FileReader("data/cards.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String cardNumber = parts[0];
                    String accountName = parts[1];
                    String cardType = parts[2];
                    boolean isActive = Boolean.parseBoolean(parts[3]);
                    String pin = parts[4];
                    String userId = parts[5];

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
                            BigDecimal dailyLimit = new BigDecimal(parts[6]);
                            card = new DebitCard(cardNumber, linkedAccount, pin, dailyLimit);
                            card.setActive(isActive);
                            
                            // Find the user who owns the account
                            for (User user : users.values()) {
                                if (user.getUserID().equals(userId)) {
                                    user.addCard(card);
                                    break;
                                }
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
        
        // Check username length
        if (username.length() > 16) {
            System.out.println("Username cannot be longer than 16 characters.");
            return;
        }

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

        // Check for reserved account names
        if (accName.equalsIgnoreCase("DEPOSIT") || 
            accName.equalsIgnoreCase("WITHDRAWAL") || 
            accName.equalsIgnoreCase("CARD_PAYMENT")) {
            System.out.println("This account name is reserved and cannot be used.");
            return;
        }

        // Check account name length
        if (accName.length() > 16) {
            System.out.println("Account name cannot be longer than 16 characters.");
            return;
        }

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
        System.out.println("\nTransfer History Options:");
        System.out.println("1 - View transfers (oldest to newest)");
        System.out.println("2 - View transfers (newest to oldest)");
        System.out.println("3 - Search transfers by date");
        System.out.print("Enter your choice: ");
        String historyChoice = scanner.nextLine();
        
        switch (historyChoice) {
            case "1":
                Transfer.printTransfersChronological(transfers, loggedInUser.getAccounts().get(0).getAccountName());
                break;
            case "2":
                Transfer.printTransfersReverseChronological(transfers, loggedInUser.getAccounts().get(0).getAccountName());
                break;
            case "3":
                Transfer.searchTransfersByDate(transfers, loggedInUser.getAccounts(), scanner);
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private static void makeCardPayment() {
        if (loggedInUser == null) {
            System.out.println("Please log in first.");
            return;
        }

        if (loggedInUser.getCards().isEmpty()) {
            System.out.println("You have no cards.");
            return;
        }

        System.out.println("Your cards:");
        for (Card card : loggedInUser.getCards()) {
            System.out.println("Card Number: " + card.getCardNumber());
            System.out.println("Linked Account: " + card.getLinkedAccount().getAccountName());
            System.out.println("Status: " + (card.isActive() ? "Active" : "Inactive"));
            System.out.println("------------------------");
        }

        System.out.print("Enter card number to use: ");
        String cardNumber = scanner.nextLine();

        Card selectedCard = null;
        for (Card card : loggedInUser.getCards()) {
            if (card.getCardNumber().equals(cardNumber)) {
                selectedCard = card;
                break;
            }
        }

        if (selectedCard == null) {
            System.out.println("Card not found.");
            return;
        }

        if (!selectedCard.isActive()) {
            System.out.println("This card is inactive.");
            return;
        }

        System.out.print("Enter payment amount: ");
        BigDecimal amount = null;
        while (amount == null) {
            if (scanner.hasNextBigDecimal()) {
                amount = scanner.nextBigDecimal();
                scanner.nextLine(); // Consume newline
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("Amount must be positive.");
                    amount = null;
                }
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next();
            }
        }

        // Process the payment
        Account linkedAccount = selectedCard.getLinkedAccount();
        if (amount.compareTo(linkedAccount.getBalance()) > 0) {
            System.out.println("Insufficient funds.");
            return;
        }

        // Create a transfer record with empty target account
        Transfer payment = new Transfer(linkedAccount.getAccountName(), "CARD_PAYMENT", amount);
        payment.setStatus("COMPLETED");
        transfers.add(payment);

        // Update account balance
        linkedAccount.setBalance(linkedAccount.getBalance().subtract(amount));

        saveAccounts();
        saveTransfers();
        System.out.println("Payment completed successfully!");
    }

    private static void performDeposit() {
        if (loggedInUser.getAccounts().isEmpty()) {
            System.out.println("You need to have at least one account to make a deposit.");
            return;
        }

        System.out.println("\nYour accounts:");
        loggedInUser.printAccounts();
        
        System.out.print("\nEnter account name: ");
        String accName = scanner.nextLine();
        
        Account selectedAccount = null;
        for (Account acc : loggedInUser.getAccounts()) {
            if (acc.getAccountName().equals(accName)) {
                selectedAccount = acc;
                break;
            }
        }
        
        if (selectedAccount == null) {
            System.out.println("Account not found!");
            return;
        }

        BigDecimal amount = null;
        while (amount == null) {
            System.out.print("Enter amount to deposit: ");
            if (scanner.hasNextBigDecimal()) {
                amount = scanner.nextBigDecimal();
                scanner.nextLine(); // Consume newline
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("Amount must be positive.");
                    amount = null;
                }
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next();
            }
        }

        Deposit deposit = new Deposit(accName, amount);
        if (selectedAccount.deposit(amount)) {
            deposit.setStatus("COMPLETED");
            saveAccounts();
            System.out.println("Deposit completed successfully!");
        } else {
            deposit.setStatus("INCOMPLETE");
            System.out.println("Deposit failed. Please check the amount.");
        }

        // Record the deposit
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(TRANSFER_FILE, true))) {
            bw.write(deposit.toCSV());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error recording deposit: " + e.getMessage());
        }
    }

    private static void performWithdraw() {
        if (loggedInUser.getAccounts().isEmpty()) {
            System.out.println("You need to have at least one account to make a withdrawal.");
            return;
        }

        System.out.println("\nYour accounts:");
        loggedInUser.printAccounts();
        
        System.out.print("\nEnter account name: ");
        String accName = scanner.nextLine();
        
        Account selectedAccount = null;
        for (Account acc : loggedInUser.getAccounts()) {
            if (acc.getAccountName().equals(accName)) {
                selectedAccount = acc;
                break;
            }
        }
        
        if (selectedAccount == null) {
            System.out.println("Account not found!");
            return;
        }

        BigDecimal amount = null;
        while (amount == null) {
            System.out.print("Enter amount to withdraw: ");
            if (scanner.hasNextBigDecimal()) {
                amount = scanner.nextBigDecimal();
                scanner.nextLine(); // Consume newline
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("Amount must be positive.");
                    amount = null;
                }
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next();
            }
        }

        Withdrawal withdrawal = new Withdrawal(accName, amount);
        if (selectedAccount.withdraw(amount)) {
            withdrawal.setStatus("COMPLETED");
            saveAccounts();
            System.out.println("Withdrawal completed successfully!");
        } else {
            withdrawal.setStatus("INCOMPLETE");
            System.out.println("Withdrawal failed. Please check the amount and available balance.");
        }

        // Record the withdrawal
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(TRANSFER_FILE, true))) {
            bw.write(withdrawal.toCSV());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error recording withdrawal: " + e.getMessage());
        }
    }

    private static void deleteCard() {
        if (loggedInUser == null) {
            System.out.println("Please log in first.");
            return;
        }

        if (loggedInUser.getCards().isEmpty()) {
            System.out.println("You have no cards to delete.");
            return;
        }

        System.out.print("Enter card number to delete: ");
        String cardNumber = scanner.nextLine();

        Card selectedCard = null;
        for (Card card : loggedInUser.getCards()) {
            if (card.getCardNumber().equals(cardNumber)) {
                selectedCard = card;
                break;
            }
        }

        if (selectedCard == null) {
            System.out.println("Card not found.");
            return;
        }

        // Remove card from user's cards list
        loggedInUser.getCards().remove(selectedCard);

        // Rewrite the entire cards.csv file without the deleted card
        try {
            // Read all cards from the file
            List<String> remainingCards = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader("data/cards.csv"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 1 && !parts[0].equals(cardNumber)) {
                        remainingCards.add(line);
                    }
                }
            }

            // Write back all cards except the deleted one
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/cards.csv"))) {
                for (String cardLine : remainingCards) {
                    bw.write(cardLine);
                    bw.newLine();
                }
            }

            System.out.println("Card deleted successfully!");
        } catch (IOException e) {
            System.out.println("Error deleting card: " + e.getMessage());
        }
    }
}
