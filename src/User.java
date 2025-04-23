import java.util.Comparator;


import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String userID;
    private List<Account> accounts;
    private List<Card> cards;

    public User(String name, String userID) {
        this.name = name;
        this.userID = userID;
        this.accounts = new ArrayList<>();
        this.cards = new ArrayList<>();
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public List<Card> getCards() {
        return cards;
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