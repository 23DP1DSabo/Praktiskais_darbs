import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deposit {
    private String accountName;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private String status;

    public Deposit(String accountName, BigDecimal amount) {
        this.accountName = accountName;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.status = "PENDING";
    }

    public String getAccountName() {
        return accountName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toCSV() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return accountName + ",DEPOSIT," + amount + "," + timestamp.format(formatter) + "," + status;
    }

    public static Deposit fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length != 5) {
            throw new IllegalArgumentException("Invalid CSV format for Deposit");
        }
        String accountName = parts[0];
        BigDecimal amount = new BigDecimal(parts[2]);
        LocalDateTime timestamp = LocalDateTime.parse(parts[3], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String status = parts[4];
        
        Deposit deposit = new Deposit(accountName, amount);
        deposit.timestamp = timestamp;
        deposit.status = status;
        return deposit;
    }
} 