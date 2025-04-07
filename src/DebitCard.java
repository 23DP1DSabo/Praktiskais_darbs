import java.math.BigDecimal;

public class DebitCard extends Card {
    private BigDecimal dailyLimit;
    private BigDecimal dailySpent;

    public DebitCard(String cardNumber, Account linkedAccount, String pin, BigDecimal dailyLimit) {
        super(cardNumber, linkedAccount, "DEBIT", pin);
        this.dailyLimit = dailyLimit;
        this.dailySpent = BigDecimal.ZERO;
    }

    public BigDecimal getDailyLimit() {
        return dailyLimit;
    }

    public BigDecimal getDailySpent() {
        return dailySpent;
    }

    public void resetDailySpent() {
        this.dailySpent = BigDecimal.ZERO;
    }

    @Override
    public boolean processTransaction(BigDecimal amount, String pin) {
        if (!isActive) {
            return false;
        }

        if (!validatePin(pin)) {
            return false;
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        // Check daily limit
        if (dailySpent.add(amount).compareTo(dailyLimit) > 0) {
            return false;
        }

        // Check account balance
        if (linkedAccount.getBalance().compareTo(amount) < 0) {
            return false;
        }

        // Process the transaction
        linkedAccount.setBalance(linkedAccount.getBalance().subtract(amount));
        dailySpent = dailySpent.add(amount);
        return true;
    }

    @Override
    public String toCSV() {
        return super.toCSV() + "," + dailyLimit + "," + dailySpent;
    }

    public static DebitCard fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length != 7) {
            throw new IllegalArgumentException("Invalid CSV format for DebitCard");
        }

        String cardNumber = parts[0];
        String accountName = parts[1];
        String cardType = parts[2];
        boolean isActive = Boolean.parseBoolean(parts[3]);
        String pin = parts[4];
        BigDecimal dailyLimit = new BigDecimal(parts[5]);
        BigDecimal dailySpent = new BigDecimal(parts[6]);

        // Note: We need to find the actual Account object to link to the card
        // This will be handled by the Main class when loading cards
        DebitCard card = new DebitCard(cardNumber, null, pin, dailyLimit);
        card.setActive(isActive);
        return card;
    }
} 