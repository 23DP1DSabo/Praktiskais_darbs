import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

    public static void printTransfersChronological(List<Transfer> transfers, String accountName) {
        System.out.println("\nTransfer History (oldest to newest):");
        boolean found = false;
        for (Transfer transfer : transfers) {
            if (transfer.getSourceAccountName().equals(accountName) ||
                transfer.getTargetAccountName().equals(accountName)) {
                System.out.printf("From: %s, To: %s, Amount: €%s, Date: %s, Status: %s%n",
                    transfer.getSourceAccountName(),
                    transfer.getTargetAccountName(),
                    transfer.getAmount(),
                    transfer.getTimestamp(),
                    transfer.getStatus());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No transfer history found.");
        }
    }

    public static void printTransfersReverseChronological(List<Transfer> transfers, String accountName) {
        System.out.println("\nTransfer History (newest to oldest):");
        boolean found = false;
        List<Transfer> userTransfers = new ArrayList<>();
        
        // First collect all relevant transfers
        for (Transfer transfer : transfers) {
            if (transfer.getSourceAccountName().equals(accountName) ||
                transfer.getTargetAccountName().equals(accountName)) {
                userTransfers.add(transfer);
                found = true;
            }
        }
        
        // Then print them in reverse order
        for (int i = userTransfers.size() - 1; i >= 0; i--) {
            Transfer transfer = userTransfers.get(i);
            System.out.printf("From: %s, To: %s, Amount: €%s, Date: %s, Status: %s%n",
                transfer.getSourceAccountName(),
                transfer.getTargetAccountName(),
                transfer.getAmount(),
                transfer.getTimestamp(),
                transfer.getStatus());
        }
        
        if (!found) {
            System.out.println("No transfer history found.");
        }
    }

    public void sortedByDateDescending() {

    }

    public void sortedByDateAscending() {
        
    }
} 