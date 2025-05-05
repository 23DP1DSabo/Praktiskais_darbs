import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Account {
    private String accName;
    private BigDecimal balance;
    private String ownerId;

    public Account(String accName, String ownerId, BigDecimal balance) {
        this.accName = accName;
        this.balance = balance;
        this.ownerId = ownerId;
    }

    public String getAccountName() {
        return accName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String toCSV() {
        return accName + "," + ownerId + "," + balance;
    }

    public boolean withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        if (amount.compareTo(balance) > 0) {
            return false;
        }
        balance = balance.subtract(amount);
        return true;
    }

    public boolean deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        balance = balance.add(amount);
        return true;
    }

    public static void searchAccountsByName(List<Account> accounts, Scanner scanner) {
        if (accounts.isEmpty()) {
            System.out.println("No accounts available to search.");
            return;
        }

        System.out.print("\nEnter account name to search: ");
        String searchName = scanner.nextLine().trim();

        boolean found = false;
        System.out.println("\nSearch Results:");
        System.out.println("----------------");

        for (Account account : accounts) {
            if (account.getAccountName().toLowerCase().contains(searchName.toLowerCase())) {
                System.out.printf("Account Name: %s%n", account.getAccountName());
                System.out.printf("Balance: €%s%n", account.getBalance());
                System.out.printf("Owner ID: %s%n", account.getOwnerId());
                System.out.println("----------------");
                found = true;
            }
        }

        if (!found) {
            System.out.println("No accounts found matching the search criteria.");
        }
    }
}