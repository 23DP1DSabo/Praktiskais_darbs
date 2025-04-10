import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class CreditCard extends Card {
    private BigDecimal dailyLimit;
    private BigDecimal dailySpent;
    private BigDecimal creditLimit;
    private BigDecimal currentBalance;
    private BigDecimal interestRate; // Annual interest rate as a decimal (e.g., 0.15 for 15%)
    private LocalDateTime lastInterestCalculation;
    private BigDecimal interestAccrued;

    public CreditCard(String cardNumber, Account linkedAccount, String pin, BigDecimal dailyLimit, BigDecimal creditLimit, BigDecimal interestRate) {
        super(cardNumber, linkedAccount, "CREDIT", pin);
        this.dailyLimit = dailyLimit;
        this.dailySpent = BigDecimal.ZERO;
        this.creditLimit = creditLimit;
        this.currentBalance = BigDecimal.ZERO;
        this.interestRate = interestRate;
        this.lastInterestCalculation = LocalDateTime.now();
        this.interestAccrued = BigDecimal.ZERO;
    }

    public BigDecimal getDailyLimit() {
        return dailyLimit;
    }

    public BigDecimal getDailySpent() {
        return dailySpent;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public BigDecimal getCurrentBalance() {
        calculateInterest();
        return currentBalance.add(interestAccrued);
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void resetDailySpent() {
        this.dailySpent = BigDecimal.ZERO;
    }

    private void calculateInterest() {
        LocalDateTime now = LocalDateTime.now();
        long minutesPassed = ChronoUnit.MINUTES.between(lastInterestCalculation, now);
        
        if (minutesPassed > 0) {
            // Calculate interest for the passed minutes
            // Convert annual rate to per-minute rate
            BigDecimal perMinuteRate = interestRate.divide(BigDecimal.valueOf(525600), 10, BigDecimal.ROUND_HALF_UP); // 365 * 24 * 60 = 525600 minutes in a year
            
            // Calculate interest on the current balance
            BigDecimal interest = currentBalance.multiply(perMinuteRate)
                                              .multiply(BigDecimal.valueOf(minutesPassed));
            
            interestAccrued = interestAccrued.add(interest);
            lastInterestCalculation = now;
        }
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

        // Calculate current balance including interest
        calculateInterest();
        BigDecimal totalBalance = currentBalance.add(interestAccrued);

        // Check if transaction would exceed credit limit
        if (totalBalance.add(amount).compareTo(creditLimit) > 0) {
            return false;
        }

        // Process the transaction
        currentBalance = currentBalance.add(amount);
        dailySpent = dailySpent.add(amount);
        return true;
    }

    public boolean makePayment(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        calculateInterest();
        BigDecimal totalBalance = currentBalance.add(interestAccrued);

        if (amount.compareTo(totalBalance) > 0) {
            return false;
        }

        // First pay off interest, then principal
        if (amount.compareTo(interestAccrued) <= 0) {
            interestAccrued = interestAccrued.subtract(amount);
        } else {
            BigDecimal remaining = amount.subtract(interestAccrued);
            interestAccrued = BigDecimal.ZERO;
            currentBalance = currentBalance.subtract(remaining);
        }

        return true;
    }

    @Override
    public String toCSV() {
        return super.toCSV() + "," + dailyLimit + "," + dailySpent + "," + 
               creditLimit + "," + currentBalance + "," + interestRate + "," + 
               lastInterestCalculation + "," + interestAccrued;
    }

    public static CreditCard fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length != 10) {
            throw new IllegalArgumentException("Invalid CSV format for CreditCard");
        }

        String cardNumber = parts[0];
        String accountName = parts[1];
        String cardType = parts[2];
        boolean isActive = Boolean.parseBoolean(parts[3]);
        String pin = parts[4];
        BigDecimal dailyLimit = new BigDecimal(parts[5]);
        BigDecimal dailySpent = new BigDecimal(parts[6]);
        BigDecimal creditLimit = new BigDecimal(parts[7]);
        BigDecimal currentBalance = new BigDecimal(parts[8]);
        BigDecimal interestRate = new BigDecimal(parts[9]);
        LocalDateTime lastInterestCalculation = LocalDateTime.parse(parts[10]);
        BigDecimal interestAccrued = new BigDecimal(parts[11]);

        CreditCard card = new CreditCard(cardNumber, null, pin, dailyLimit, creditLimit, interestRate);
        card.setActive(isActive);
        card.dailySpent = dailySpent;
        card.currentBalance = currentBalance;
        card.lastInterestCalculation = lastInterestCalculation;
        card.interestAccrued = interestAccrued;
        return card;
    }
} 