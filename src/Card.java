import java.math.BigDecimal;

public abstract class Card {
    protected String cardNumber;
    protected Account linkedAccount;
    protected String cardType;
    protected boolean isActive;
    protected String pin;

    public Card(String cardNumber, Account linkedAccount, String cardType, String pin) {
        this.cardNumber = cardNumber;
        this.linkedAccount = linkedAccount;
        this.cardType = cardType;
        this.pin = pin;
        this.isActive = true;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public Account getLinkedAccount() {
        return linkedAccount;
    }

    public String getCardType() {
        return cardType;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean validatePin(String inputPin) {
        return pin.equals(inputPin);
    }

    public String toCSV() {
        return cardNumber + "," + linkedAccount.getAccountName() + "," + cardType + "," + isActive + "," + pin;
    }

    // Abstract methods that must be implemented by subclasses
    public abstract boolean processTransaction(BigDecimal amount, String pin);
}