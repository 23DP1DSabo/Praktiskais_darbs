import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;
import java.util.UUID;

public class Loan {
    private String loanId;
    private String accountName;
    private BigDecimal principal;
    private BigDecimal remainingAmount;
    private BigDecimal interestRate; // Annual interest rate as a decimal
    private LocalDateTime startDate;
    private LocalDateTime lastInterestCalculation;
    private BigDecimal interestAccrued;
    private int termMonths;
    private BigDecimal monthlyPayment;

    public Loan(String accountName, BigDecimal principal, BigDecimal interestRate, int termMonths) {
        this.loanId = UUID.randomUUID().toString().substring(0, 8);
        this.accountName = accountName;
        this.principal = principal;
        this.remainingAmount = principal;
        this.interestRate = interestRate;
        this.startDate = LocalDateTime.now();
        this.lastInterestCalculation = startDate;
        this.interestAccrued = BigDecimal.ZERO;
        this.termMonths = termMonths;
        calculateMonthlyPayment();
    }

    private void calculateMonthlyPayment() {
        // Using the formula: P * (r(1+r)^n) / ((1+r)^n - 1)
        // where P is principal, r is monthly rate, n is number of payments
        BigDecimal monthlyRate = interestRate.divide(BigDecimal.valueOf(12), 10, BigDecimal.ROUND_HALF_UP);
        BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);
        BigDecimal numerator = principal.multiply(monthlyRate).multiply(onePlusR.pow(termMonths));
        BigDecimal denominator = onePlusR.pow(termMonths).subtract(BigDecimal.ONE);
        this.monthlyPayment = numerator.divide(denominator, 2, BigDecimal.ROUND_HALF_UP);
    }

    public void calculateInterest() {
        LocalDateTime now = LocalDateTime.now();
        long daysPassed = ChronoUnit.DAYS.between(lastInterestCalculation, now);
        
        if (daysPassed > 0) {
            // Calculate interest for the passed days
            BigDecimal dailyRate = interestRate.divide(BigDecimal.valueOf(365), 10, BigDecimal.ROUND_HALF_UP);
            BigDecimal interest = remainingAmount.multiply(dailyRate).multiply(BigDecimal.valueOf(daysPassed));
            interestAccrued = interestAccrued.add(interest);
            lastInterestCalculation = now;
        }
    }

    public boolean makePayment(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        calculateInterest();
        BigDecimal totalDue = remainingAmount.add(interestAccrued);

        if (amount.compareTo(totalDue) > 0) {
            return false;
        }

        // First pay off interest, then principal
        if (amount.compareTo(interestAccrued) <= 0) {
            interestAccrued = interestAccrued.subtract(amount);
        } else {
            BigDecimal remaining = amount.subtract(interestAccrued);
            interestAccrued = BigDecimal.ZERO;
            remainingAmount = remainingAmount.subtract(remaining);
        }

        return true;
    }

    public String getLoanId() {
        return loanId;
    }

    public String getAccountName() {
        return accountName;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public BigDecimal getRemainingAmount() {
        calculateInterest();
        return remainingAmount;
    }

    public BigDecimal getInterestAccrued() {
        calculateInterest();
        return interestAccrued;
    }

    public BigDecimal getMonthlyPayment() {
        return monthlyPayment;
    }

    public int getTermMonths() {
        return termMonths;
    }

    public String toCSV() {
        return loanId + "," + accountName + "," + principal + "," + remainingAmount + "," +
               interestRate + "," + startDate + "," + lastInterestCalculation + "," +
               interestAccrued + "," + termMonths + "," + monthlyPayment;
    }

    public static Loan fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length != 10) {
            throw new IllegalArgumentException("Invalid CSV format for Loan");
        }

        String loanId = parts[0];
        String accountName = parts[1];
        BigDecimal principal = new BigDecimal(parts[2]);
        BigDecimal remainingAmount = new BigDecimal(parts[3]);
        BigDecimal interestRate = new BigDecimal(parts[4]);
        LocalDateTime startDate = LocalDateTime.parse(parts[5]);
        LocalDateTime lastInterestCalculation = LocalDateTime.parse(parts[6]);
        BigDecimal interestAccrued = new BigDecimal(parts[7]);
        int termMonths = Integer.parseInt(parts[8]);
        BigDecimal monthlyPayment = new BigDecimal(parts[9]);

        Loan loan = new Loan(accountName, principal, interestRate, termMonths);
        loan.loanId = loanId;
        loan.remainingAmount = remainingAmount;
        loan.startDate = startDate;
        loan.lastInterestCalculation = lastInterestCalculation;
        loan.interestAccrued = interestAccrued;
        loan.monthlyPayment = monthlyPayment;
        return loan;
    }

    public static void processPayment(Loan loan, Account account, Scanner scanner) {
        BigDecimal amount = null;
        while (amount == null) {
            System.out.print("Enter payment amount: ");
            if (scanner.hasNextBigDecimal()) {
                amount = scanner.nextBigDecimal();
                scanner.nextLine(); // Consume newline
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("Amount must be positive.");
                    amount = null;
                } else if (amount.compareTo(account.getBalance()) > 0) {
                    System.out.println("Insufficient funds.");
                    return;
                }
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next();
            }
        }
    
        if (loan.makePayment(amount)) {
            account.setBalance(account.getBalance().subtract(amount));
            System.out.println("Payment successful!");
        } else {
            System.out.println("Payment failed. Please check the amount.");
        }
    }

    public static void displayLoanDetails(Loan loan) {
        String status = loan.getRemainingAmount().compareTo(BigDecimal.ZERO) == 0 ? "Repaid" : "In Process";
        System.out.printf("Loan ID: %s%n", loan.getLoanId());
        System.out.printf("Account: %s%n", loan.getAccountName());
        System.out.printf("Principal: $%.2f%n", loan.getPrincipal());
        System.out.printf("Remaining Amount: $%.2f%n", loan.getRemainingAmount());
        System.out.printf("Monthly Payment: $%.2f%n", loan.getMonthlyPayment());
        System.out.printf("Term: %d months%n", loan.getTermMonths());
        System.out.printf("Accrued Interest: $%.2f%n", loan.getInterestAccrued());
        System.out.printf("Status: %s%n", status);
    }
} 