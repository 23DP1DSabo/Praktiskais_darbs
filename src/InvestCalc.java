import java.util.Scanner;

public class InvestCalc {

    public static void calcInvestment() {
        try {
            Scanner scanner = Main.getScanner();
            double amount = -1;
            double annualRate = -1;
            int months = -1;
            String type = "";

            while (amount <= 0) {
                System.out.print("Enter investment amount: ");
                if (scanner.hasNextDouble()) {
                    amount = scanner.nextDouble();
                    if (amount <= 0) {
                        System.out.println("Amount must be greater than 0.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next();
                }
            }

            while (annualRate < 0) {
                System.out.print("Enter annual interest rate (%): ");
                if (scanner.hasNextDouble()) {
                    annualRate = scanner.nextDouble();
                    if (annualRate < 0) {
                        System.out.println("Interest rate cannot be negative.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next();
                }
            }

            if (annualRate == 0) {
                System.out.println("No interest rate applied.");
            }

            while (months <= 0) {
                System.out.print("Enter duration in months: ");
                if (scanner.hasNextInt()) {
                    months = scanner.nextInt();
                    if (months <= 0) {
                        System.out.println("Duration must be at least 1 month.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter an integer.");
                    scanner.next();
                }
            }

            scanner.nextLine();
            while (!type.equalsIgnoreCase("ot") && !type.equalsIgnoreCase("rec")) {
                System.out.print("Enter investment type (one-time: 'ot'; recurring: 'rec'): ");
                type = scanner.nextLine().trim().toLowerCase();
                if (!type.equals("ot") && !type.equals("rec")) {
                    System.out.println("Invalid type. Choose 'ot' or 'rec'.");
                }
            }

            double monthlyRate = (annualRate / 100) / 12;

            double finalAmount;
            if (type.equals("ot")) {
                finalAmount = amount * Math.pow(1 + monthlyRate, months);
            } else {
                finalAmount = 0;
                for (int i = 0; i < months; i++) {
                    finalAmount += amount * Math.pow(1 + monthlyRate, months - i);
                }
            }

            System.out.printf("Final investment value after %d months: ?%.2f%n", months, finalAmount);
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}