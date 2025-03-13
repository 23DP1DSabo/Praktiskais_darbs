public class Account {
    private String accountNumber;
    private String balance;
    private String fullName;

    public Account(String accountNumber, String fullName, String balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.fullName = fullName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getBalance() {
        return balance;
    }
    public String getAccountDetails(String accountNumber, String fullName, String balance) {
        return "Account Number: " + accountNumber + "\nName:" + fullName + "\nBalance: " + balance;
    }
}