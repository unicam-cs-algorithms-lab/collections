package it.unicam.cs.asdl.slides.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Archivio di conti che usa il contratto {@link java.util.List} e
 * l'implementazione {@link java.util.ArrayList}.
 * <p>La lista ammetterebbe duplicati, ma {@link #insert(BankAccount)}
 * li rifiuta secondo l'uguaglianza logica definita dall'IBAN.
 * La sequenza conserva un ordine, che può essere modificato dai metodi di ordinamento.
 * 
 * @author Luca Tesei
 *
 */
public class Bank {

    private final String swiftCode;

    private String name;

    private String address;

    // Programmiamo rispetto all'interfaccia List, non alla classe ArrayList.
    private List<BankAccount> accounts;

    /**
     * @param swiftCode
     *                      codice identificativo della banca
     * @param name
     *                      nome della banca
     * @param address
     *                      indirizzo
     * @throws NullPointerException
     *                                  se lo swiftCode è nullo
     */
    public Bank(String swiftCode, String name, String address) {
        if (swiftCode == null)
            throw new NullPointerException(
                    "Tentativo di creare una banca con swiftCode nullo");
        this.swiftCode = swiftCode;
        this.name = name;
        this.address = address;
        this.accounts = new ArrayList<BankAccount>();
    }

    /**
     * Documenta il contratto dell’operazione getName. @return il nome corrente della banca, che può essere modificato */
    public String getName() {
        return name;
    }

    /**
     * Documenta il contratto dell’operazione setName. @param name nuovo nome della banca */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Documenta il contratto dell’operazione getAddress. @return l'indirizzo corrente della banca */
    public String getAddress() {
        return address;
    }

    /**
     * Documenta il contratto dell’operazione setAddress. @param address nuovo indirizzo della banca */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Documenta il contratto dell’operazione getSwiftCode. @return l'identificativo immutabile della banca */
    public String getSwiftCode() {
        return swiftCode;
    }

    /*
     * Si basa sull'hashCode delle stringhe, considerando l'hashCode dello
     * swiftCode della banca.
     * 
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return swiftCode.hashCode();
    }

    /*
     * Due banche sono uguali se e solo se hanno lo stesso swiftCode.
     * 
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Bank))
            return false;
        Bank other = (Bank) obj;
        if (!swiftCode.equals(other.swiftCode))
            return false;
        return true;
    }

    /**
     * Inserisce un nuovo conto.
     * 
     * @param b
     *              il nuovo conto
     * @return true se l'inserimento è avvenuto, false se il conto è già
     *         presente in questa banca e quindi non può essere reinserito.
     * 
     */
    public boolean insert(BankAccount b) {
        // nelle List sono consentiti elementi duplicati, quindi devo prima
        // effettuare un controllo per evitare che add inserisca il conto se già
        // presente
        if (!this.accounts.contains(b)) {
            this.accounts.add(b);
            return true;
        }
        return false;
    }

    /**
     * Esegue le procedure di fine mese su tutti gli accounts.
     */
    public void endOfMonth() {
        // chiamo il metodo su tutti gli accounts
        for (BankAccount b : this.accounts)
            b.endOfMonth();
    }

    /**
     * Cerca se un conto è già presente
     * 
     * @param b
     *              un conto
     * @return la posizione del conto nella lista se il conto esiste, -1
     *         altrimenti
     */
    public int search(BankAccount b) {
        // Ricerco il conto nella lista
        return this.accounts.indexOf(b);
    }

    /**
     * Cancella un certo conto.
     * 
     * @param b
     *              il conto da rimuovere
     * @return true se il conto è stato rimosso, false altrimenti
     */
    public boolean delete(BankAccount b) {
        return this.accounts.remove(b);
    }

    /**
     * Restituisce il primo conto della lista il cui nome intestatario
     * corrisponde a un nome dato.
     * 
     * @param name
     *                 il nome dell'intestatario da cercare
     * @return il primo conto della lista il cui intestatario è uguale a name,
     *         null se non c'è nessun conto con intestatario uguale a name
     */
    public BankAccount getAccountByName(String name) {
        Iterator<BankAccount> iterator = this.accounts.iterator();
        BankAccount b;
        while (iterator.hasNext()) {
            b = iterator.next();
            if (b.getNome().equals(name))
                return b;
        }
        return null;
    }

    /**
     * Elimina tutti i conti che hanno un saldo minore o uguale a quello dato.
     * 
     * @param amount
     *                   il saldo sotto il quale i conti verranno eliminati
     */
    public void removeAccountsWithBalanceLessThanOrEqualTo(double amount) {
        Iterator<BankAccount> iterator = this.accounts.iterator();
        BankAccount b;
        while (iterator.hasNext()) {
            b = iterator.next();
            if (b.getSaldo() <= amount)
                iterator.remove();
        }
    }

    /**
     * Trasforma tutti i conti con saldo minore di un certo importo in conti
     * CheckingAccount.
     * 
     * @param amount
     *                   il valore soglia, se un conto ha valore minore di
     *                   amount viene convertito in CheckingAccount
     */
    public void changeAccountsWithBalanceLessThan(double amount) {
        List<BankAccount> toAdd = new ArrayList<BankAccount>();
        Iterator<BankAccount> iterator = this.accounts.iterator();
        BankAccount b;
        while (iterator.hasNext()) {
            b = iterator.next();
            double vecchioSaldo = b.getSaldo();
            if (b.getSaldo() < amount) {
                iterator.remove();
                // non posso inserire mentre scorro la lista con iteratore
                // quindi accumulo i conti da inserire
                toAdd.add(new CheckingAccount(vecchioSaldo, b.getNome(), b.getIban()));
            }
        }
        // La nuova lista viene accodata: non si conservano le posizioni originarie.
        // Inserire nella lista percorsa invaliderebbe l'iteratore fail-fast.
        this.accounts.addAll(toAdd);
    }

    /**
     * Restituisce una stringa che elenca la descrizione di tutti i conti
     * presenti.
     * 
     * @return la lista delle descrizioni di tutti i conti presenti
     */
    public String getAccountDescriptions() {
        StringBuffer s = new StringBuffer();
        for (BankAccount b : this.accounts)
            s.append(b.toString() + "\n");
        return s.toString();
    }

    /**
     * Ordina i conti per iban e restituisce una stringa che elenca la loro
     * descrizione
     * 
     * @return lista delle descrizioni di tutti i conti presenti ordinati per
     *         iban
     */
    public String getAccountDescriptionsByIban() {
        // Collections.sort modifica la lista originale (non una copia).
        // L'ordine naturale è definito da BankAccount.compareTo, cioè dall'IBAN.
        // Ordino la lista in base all'ordinamento naturale
        Collections.sort(this.accounts);
        return getAccountDescriptions();
    }

    /**
     * Ordina i conti per intestatario e restituisce una stringa che elenca la
     * loro descrizione
     * 
     * @return lista delle descrizioni di tutti i conti presenti ordinati per
     *         intestatario
     */
    public String getAccountDescriptionsByName() {
        // Ordino la lista in base al comparatore per intestatario
        Collections.sort(this.accounts, BankAccount.ORDINA_PER_INTESTATARIO);
        return getAccountDescriptions();
    }

    /**
     * Ordina i conti per saldo e restituisce una stringa che elenca la loro
     * descrizione
     * 
     * @return lista delle descrizioni di tutti i conti presenti ordinati per
     *         saldo
     */
    public String getAccountDescriptionsBySaldo() {
        // Ordino la lista in base al comparatore per saldo
        Collections.sort(this.accounts, BankAccount.ORDINA_PER_SALDO);
        return getAccountDescriptions();
    }

    @Override
    public String toString() {
        return "Bank [swiftCode=" + swiftCode + ", name=" + name + ", address="
                + address + "]";
    }
    
}
