package it.unicam.cs.asdl2526.slides.collections;
import java.util.List;
/**
     * Documenta il contratto dell’operazione operazione. Esempi di metodi generici e wildcard, con tipi del dominio bancario. */
public final class GenericExamples {
    private GenericExamples() { }
    /**
     * Restituisce il primo elemento mantenendo il tipo T.
     * Il parametro di tipo del metodo è dichiarato prima del tipo di ritorno.
     * @param <T> tipo degli elementi
     * @param values valori da esaminare
     * @return primo elemento o null se l'array è vuoto
     */
    public static <T> T first(T[] values) {
        if (values == null) throw new NullPointerException("values");
        return values.length == 0 ? null : values[0];
    }
    /**
     * Legge conti di qualsiasi sottotipo di BankAccount.
     * Una {@code List<SavingsAccount>} è accettata, mentre non sarebbe
     * assegnabile direttamente a una {@code List<BankAccount>}.
     * @param accounts lista di conti, non modificata
     * @return somma dei saldi
     */
    public static double totalBalance(List<? extends BankAccount> accounts) {
        if (accounts == null) throw new NullPointerException("accounts");
        double total = 0;
        for (BankAccount a : accounts) total += a.getSaldo();
        return total;
    }
    /**
     * Aggiunge un conto a una lista il cui tipo accetta BankAccount.
     * Esempio del limite inferiore {@code ? super BankAccount}.
     * @param destination lista destinazione
     * @param account conto da aggiungere
     */
    public static void appendAccount(List<? super BankAccount> destination, BankAccount account) {
        if(destination==null) throw new NullPointerException("destination");
        destination.add(account);
    }
}
