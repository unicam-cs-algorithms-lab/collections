package it.unicam.cs.asdl2526.slides.collections;

/**
 * Banca che gestisce la collezione di accounts con un array che non viene
 * espanso quando le dimensioni non sono più sufficienti.
 * 
 * @author Luca Tesei
 *
 */
public class BankArray {

    private final String swiftCode;

    private String name;

    private String address;

    private BankAccount[] accounts;

    private int lastFreePos;

    /**
     * @param swiftCode
     *                      codice identificativo della banca
     * @param name
     *                      nome della banca
     * @param address
     *                      indirizzo
     * @param dimension
     *                      numero di accounts gestibili
     * @throws NullPointerException
     *                                      se lo swiftCode è nullo
     * @throws IllegalArgumentException
     *                                      se la dimensione è minore o uguale
     *                                      di zero
     */
    public BankArray(String swiftCode, String name, String address,
            int dimension) {
        if (swiftCode == null)
            throw new NullPointerException(
                    "Tentativo di creare una banca con swiftCode nullo");
        if (dimension <= 0)
            throw new IllegalArgumentException(
                    "Tentativo di creare una banca con dimensione minore o uguale di zero");
        this.swiftCode = swiftCode;
        this.name = name;
        this.address = address;
        this.accounts = new BankAccount[dimension];
        this.lastFreePos = 0;
    }

    /**
     * Documenta il contratto dell’operazione getName.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *                 the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Documenta il contratto dell’operazione getAddress.
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address
     *                    the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Documenta il contratto dell’operazione getSwiftCode.
     * @return the swiftCode
     */
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
        if (!(obj instanceof BankArray))
            return false;
        BankArray other = (BankArray) obj;
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
     * @throws BankDimensionException
     *                                    se il numero massimo dei accounts per
     *                                    questa banca è stato raggiunto
     */
    public boolean insert(BankAccount b) {
        // Controlla che il conto non è già stato inserito
        if (this.search(b) != -1)
            return false;
        // Inserisco il conto se c'è spazio sufficiente
        if (this.lastFreePos < this.accounts.length) {
            this.accounts[this.lastFreePos] = b;
            this.lastFreePos++;
            return true;
        } else
            throw new BankDimensionException(
                    "Tentativo di creare un conto in eccesso rispetto a quelli gestibili");
    }

    /**
     * Esegue le procedure di fine mese su tutti gli accounts.
     */
    public void endOfMonth() {
        // chiamo il metodo su tutti i accounts
        for (int i = 0; i < this.lastFreePos; i++)
            this.accounts[i].endOfMonth();
    }

    /**
     * Cerca se un conto è già presente
     * 
     * @param b
     *              un conto
     * @return la posizione del conto nell'array se il conto esiste, -1
     *         altrimenti
     */
    public int search(BankAccount b) {
        // Ricerco il conto nell'array
        int i = 0;
        boolean trovato = false;
        while (i < this.lastFreePos && !trovato)
            if (this.accounts[i].equals(b)) // si basa su equals di BankAccount!
                trovato = true;
            else
                i++;
        if (trovato)
            return i;
        else
            return -1;
    }

    /**
     * Cancella un certo conto.
     * 
     * @param b
     *              il conto da rimuovere
     * @return true se il conto è stato rimosso, false altrimenti
     */
    public boolean delete(BankAccount b) {
        // cerco la posizione del conto
        int p = this.search(b);
        if (p == -1) // il conto non c'è, non rimuovo niente
            return false;
        // rimuovo il conto in posizione p
        // faccio lo shift a sinistra di tutti i accounts a destra di p
        for (int i = p; i < this.lastFreePos - 1; i++)
            this.accounts[i] = this.accounts[i + 1];
        this.accounts[this.lastFreePos - 1] = null;
        this.lastFreePos--;
        return true;
    }

}
