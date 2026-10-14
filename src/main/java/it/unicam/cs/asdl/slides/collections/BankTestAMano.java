package it.unicam.cs.asdl.slides.collections;

public class BankTestAMano {

    public static void main(String[] args) {
        Bank myBank = new Bank("MYSWIFT", "MYBANK",
                "Via Madonna delle Carceri 7, 62032, Camerino (MC)");
        System.out.println(myBank);
        System.out.println("\n");
        BankAccount b1 = new SavingsAccount(100, "Luca", "IT000000000000C");
        myBank.insert(b1);
        CheckingAccount b2 = new CheckingAccount(1000, "Marco",
                "IT000000000000A");
        myBank.insert(b2);
        SavingsAccount b3 = new TimedAccount(10000, "Luca", "IT000000000000B");
        myBank.insert(b3);
        System.out.println("Conti in ordine naturale, IBAN:");
        System.out.println(myBank.getAccountDescriptionsByIban());
        System.out.println("Conti in ordine di nome:");
        System.out.println(myBank.getAccountDescriptionsByName());
        System.out.println("Conti in ordine di saldo:");
        System.out.println(myBank.getAccountDescriptionsBySaldo());
        System.out.println("--- Chiamo EndOfMonth...");
        myBank.endOfMonth();
        System.out.println("Conti in ordine di saldo:");
        System.out.println(myBank.getAccountDescriptionsBySaldo());
        System.out.println("--- Chiamo EndOfMonth 19 volte...");
        for (int i = 1; i <= 19; i++)
            myBank.endOfMonth();
        System.out.println("Conti in ordine di saldo:");
        System.out.println(myBank.getAccountDescriptionsBySaldo());
        System.out.println("--- Prelevo 101 dal savings account...");
        b1.withdraw(101);
        System.out.println("Conti in ordine di saldo:");
        System.out.println(myBank.getAccountDescriptionsBySaldo());
        System.out.println(
                "--- Trasformo tutti i conti con saldo meno di 1 in "
                + "Checking Account...");
        myBank.changeAccountsWithBalanceLessThan(1);
        System.out.println("Conti in ordine di saldo:");
        System.out.println(myBank.getAccountDescriptionsBySaldo());
        System.out.println(
                "--- Cancello tutti i conti con saldo meno di o uguale a 1...");
        myBank.removeAccountsWithBalanceLessThanOrEqualTo(1);
        System.out.println("Conti in ordine di saldo:");
        System.out.println(myBank.getAccountDescriptionsBySaldo());
        System.out.println("--- Cancello il conto con iban IT000000000000B...");
        myBank.delete(new CheckingAccount(0, "", "IT000000000000B"));
        System.out.println("Conti in ordine di saldo:");
        System.out.println(myBank.getAccountDescriptionsBySaldo());

    }

}
