import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String userID;
    private List<Account> accounts;

    public User(String name, String userID) {
        this.name = name;
        this.userID = userID;
        this.accounts = new ArrayList<>();
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public List<Account> getAccounts() {
        return accounts;
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
                System.out.println("Account: " + account.getAccountName() + ", Balance: $" + account.getBalance());
            }
        }
    }
}