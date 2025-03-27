import java.math.BigDecimal;

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

    public String getOwnerId() {
        return ownerId;
    }

    public String toCSV() {
        return accName + "," + ownerId + "," + balance;
    }
}