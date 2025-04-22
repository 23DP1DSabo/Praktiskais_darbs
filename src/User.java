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

    public void sortAccountsByBalance() {
        accounts.sort((a1, a2) -> a2.getBalance().compareTo(a1.getBalance()));
    }

    public void printAccounts() {
        if (accounts.isEmpty()) {
            System.out.println(name + " has no accounts.");
        } else {
            sortAccountsByBalance();
            for (Account account : accounts) {
                System.out.printf("Account: %s, Balance: €%.2f%n", 
                    account.getAccountName(), 
                    account.getBalance().doubleValue());
            }
        }
    }
}