package main;
public class Card {

    private String cardNumber;
    private Account linkedAccount;
    private String cardType;

    public Card(String cardNumber, Account linkedAccount, String cardType){
        this.cardNumber = cardNumber;
        this.linkedAccount = linkedAccount;
        this.cardType = cardType;
    }
}