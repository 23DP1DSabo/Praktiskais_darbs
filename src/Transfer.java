import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transfer {
    private String sourceAccountName;
    private String targetAccountName;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private String status;

    public Transfer(String sourceAccountName, String targetAccountName, BigDecimal amount) {
        this.sourceAccountName = sourceAccountName;
        this.targetAccountName = targetAccountName;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.status = "PENDING";
    }

    public String getSourceAccountName() {
        return sourceAccountName;
    }

    public String getTargetAccountName() {
        return targetAccountName;
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
        return sourceAccountName + "," + targetAccountName + "," + amount + "," + 
               timestamp.format(formatter) + "," + status;
    }

    public static Transfer fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length != 5) {
            throw new IllegalArgumentException("Invalid CSV format for Transfer");
        }

        Transfer transfer = new Transfer(parts[0], parts[1], new BigDecimal(parts[2]));
        transfer.timestamp = LocalDateTime.parse(parts[3], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        transfer.status = parts[4];
        return transfer;
    }

    public void sortedByDateDescending() {

    }

    public void sortedByDateAscending() {
        
    }
} 