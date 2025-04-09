package main;
public class CardSprites {
    

    public static void ISICCard() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < i; j++) {
                System.out.println("\033[46m");   
            }
        }
    }


    public static void CreditCard() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < i; j++) {
                System.out.println("\033[214m");   
            }
        }
    }


    public static void DebitCard() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < i; j++) {
                System.out.println("\033[208m");   
            }
        }      
    }


}
