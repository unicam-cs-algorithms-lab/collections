package it.unicam.cs.asdl2526.slides.collections;
/**
 * Esempio di parametro di tipo limitato: {@code T extends BankAccount}
 * accetta BankAccount e le sue sottoclassi, non classi arbitrarie.
 * Il vincolo consente di chiamare {@code getIban()} su un valore di tipo T.
 * @param <T> tipo concreto di conto conservato
 */
public class AccountBox<T extends BankAccount> {
    private final T account;
    /**
     * Documenta il contratto dell’operazione AccountBox. @param account conto conservato */
    public AccountBox(T account) { if(account==null) throw new NullPointerException("account"); this.account=account; }
    /**
     * Documenta il contratto dell’operazione getAccount. @return il conto mantenendo il tipo T */
    public T getAccount() { return account; }
    /**
     * Restituisce l’identificativo immutabile del conto. @return l'IBAN ottenuto tramite un metodo del limite superiore */
    public String getIban() { return account.getIban(); }
}
