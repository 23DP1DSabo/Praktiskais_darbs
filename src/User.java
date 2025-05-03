import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String userID;
    private List<Account> accounts;
    private List<Card> cards;
    private List<Loan> loans;

    public User(String name, String userID) {
        this.name = name;
        this.userID = userID;
        this.accounts = new ArrayList<>();
        this.cards = new ArrayList<>();
        this.loans = new ArrayList<>();
    }

    public void addAccount(Account account) {
        // Check if account name already exists for this user
        for (Account existingAccount : accounts) {
            if (existingAccount.getAccountName().equals(account.getAccountName())) {
                throw new IllegalArgumentException("Account name already exists for this user");
            }
        }
        accounts.add(account);
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void addLoan(Loan loan) {
        loans.add(loan);
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public List<Card> getCards() {
        return cards;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public String getUserID() {
        return userID;
    }

    public String getUsername() {
        return name;
    }

    public String toCSV() {
        return name + "," + userID;
    }

    public void printAccounts() {
        if (accounts.isEmpty()) {
            System.out.println(name + " has no accounts.");
        } else {
            for (Account account : accounts) {
                System.out.printf("Account: %s, Balance: €%s%n", 
                    account.getAccountName(), 
                    account.getBalance());
            }
        }
    }

    public void printLoans() {
        if (loans.isEmpty()) {
            System.out.println(name + " has no loans.");
        } else {
            for (Loan loan : loans) {
                System.out.printf("Loan ID: %s, Account: %s, Principal: €%s, Remaining: €%s, Monthly Payment: €%s%n",
                    loan.getLoanId(),
                    loan.getAccountName(),
                    loan.getPrincipal(),
                    loan.getRemainingAmount(),
                    loan.getMonthlyPayment());
            }
        }
    }

    public void SortedByBalanceDescending() {
        if (accounts.isEmpty()) {
            System.out.println(name + " has no accounts.");
        } else {
            List<Account> sorted = new ArrayList<>(accounts);
            sorted.sort(Comparator.comparing(Account::getBalance).reversed());
    
            System.out.println("Accounts sorted by balance (highest to lowest):");
            for (Account account : sorted) {
                System.out.printf("Account: %s, Balance: €%s%n", 
                    account.getAccountName(), 
                    account.getBalance());
            }
        }
    }
    
    public void SortedByBalanceAscending() {
        if (accounts.isEmpty()) {
            System.out.println(name + " has no accounts.");
        } else {
            List<Account> sorted = new ArrayList<>(accounts);
            sorted.sort(Comparator.comparing(Account::getBalance));
    
            System.out.println("Accounts sorted by balance (lowest to highest):");
            for (Account account : sorted) {
                System.out.printf("Account: %s, Balance: €%s%n", 
                    account.getAccountName(), 
                    account.getBalance());
            }
        }
    }
}