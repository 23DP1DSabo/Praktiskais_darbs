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

    public String getUsername() {
        return name;
    }

    public String toCSV() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(",").append(userID);
        for (Account account : accounts) {
            sb.append(",").append(account.getAccountNumber()).append(",").append(account.getBalance());
        }
        return sb.toString();
    }

    public void printAccounts() {
        if (accounts.isEmpty()) {
            System.out.println(name + " has no accounts.");
        } else {
            System.out.println(name + "'s Accounts:");
            for (Account account : accounts) {
                System.out.println("Account Number: " + account.getAccountNumber() +
                                   ", Balance: $" + account.getBalance());
            }
        }
    }
}