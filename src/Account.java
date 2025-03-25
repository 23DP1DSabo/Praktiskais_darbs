import java.math.BigDecimal;

public class Account {
    private String accountNumber;
    private BigDecimal balance;
    private String fullName;

    public Account(String accountNumber, String fullName, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.fullName = fullName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getBalance() {
        return String.valueOf(balance);
    }
    public String getAccountDetails(String accountNumber, String fullName, String balance) {
        return "Account Number: " + accountNumber + "\nName:" + fullName + "\nBalance: " + balance;
    }
}