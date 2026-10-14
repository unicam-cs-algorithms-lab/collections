package it.unicam.cs.asdl2526.slides.collections;

import java.util.Comparator;

/**
 * 
 * Conto corrente di base, senza nessuna particolare caratteristica. La classe è
 * astratta perché il comportamento del metodo {@code endOfMonth()} non è
 * definito a questo livello della gerarchia dei conti correnti.
 * 
 * @author Luca Tesei
 *
 */
public abstract class BankAccount implements Comparable<BankAccount> {

    private double saldo;

    private String nome;

    // Chiave logica stabile: equals, hashCode e compareTo usano questo campo.
    private final String iban;

    /**
     * Comparatore fra BankAccount per nome dell'intestatario. L'ordinamento è
     * compatibile con equals, cioè se due conti hanno lo stesso intestatario
     * allora il conto che ha l'iban che precede l'iban dell'altro è più piccolo
     * e i due conti risultano uguali nell'ordinamento solo se hanno lo stesso
     * iban.
     */
    public static final Comparator<BankAccount> ORDINA_PER_INTESTATARIO = new Comparator<BankAccount>() {
        public int compare(BankAccount b1, BankAccount b2) {
            int c = b1.nome.compareTo(b2.nome);
            if (c != 0)
                return c;
            else
                return b1.iban.compareTo(b2.iban);
        }
    };

    /**
     * Comparatore fra BankAccount per saldo. L'ordinamento è compatibile con
     * equals, cioè se due conti hanno lo stesso saldo allora il conto che ha
     * l'iban che precede l'iban dell'altro è più piccolo e i due conti
     * risultano uguali nell'ordinamento solo se hanno lo stesso iban.
     */
    public static final Comparator<BankAccount> ORDINA_PER_SALDO = new Comparator<BankAccount>() {
        public int compare(BankAccount b1, BankAccount b2) {
            // Double.compare evita la creazione di oggetti involucro.
            int c = Double.compare(b1.saldo, b2.saldo);
            if (c != 0)
                return c;
            else
                return b1.iban.compareTo(b2.iban);
        }
    };

    /**
     * @param saldo
     *                  saldo iniziale
     * @param nome
     *                  nome dell'intestatario
     * @param iban
     *                  codice unico identificativo del conto, immutabile
     * @throws NullPointerException
     *                                      Exception se il nome o l'iban sono
     *                                      nulli
     * @throws IllegalArgumentException
     *                                      se il saldo iniziale è negativo
     */
    public BankAccount(double saldo, String nome, String iban) {
        if (nome == null || iban == null)
            throw new NullPointerException(
                    "Tentativo di creare un conto con nome o iban null");
        if (saldo < 0)
            throw new IllegalArgumentException(
                    "Tentativo di creare un conto con un saldo iniziale negativo");
        this.saldo = saldo;
        this.nome = nome;
        this.iban = iban;
    }

    /**
     * Restituisce il nome dell’intestatario. @return il nome dell'intestatario, modificabile e non parte della chiave logica */
    public String getNome() {
        return nome;
    }

    /**
     * Modifica il nome dell’intestatario. @param nome nuovo intestatario; non cambia l'identità del conto */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce il saldo corrente. @return il saldo corrente, che non identifica il conto */
    public double getSaldo() {
        return saldo;
    }

    /**
     * Restituisce l’identificativo immutabile del conto. @return l'IBAN, chiave logica immutabile del conto */
    public String getIban() {
        return iban;
    }

    /**
     * Due conti sono logicamente uguali se hanno lo stesso IBAN,
     * anche se appartengono a sottoclassi differenti o hanno saldo diverso.
     * <p>La chiave immutabile mantiene stabile l'uguaglianza.
     * @param obj oggetto da confrontare
     * @return true se obj è un BankAccount con lo stesso IBAN
     *
     * L'equals di conti correnti si basa sull'iban, identificatore unico
     * immutabile
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof BankAccount))
            return false;
        BankAccount other = (BankAccount) obj;
        if (iban == null) {
            if (other.iban != null)
                return false;
        } else if (!iban.equals(other.iban))
            return false;
        return true;
    }

    /**
     * Restituisce un codice hash basato sull'IBAN, coerente con equals.
     * @return codice hash del conto
     *
     * L'hashcode viene calcolato a partire dall'iban, in accordo con equals.
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((iban == null) ? 0 : iban.hashCode());
        return result;
    }

    /**
     * Metodo astratto che viene chiamato sul conto alla fine di ogni mese.
     * Esegue azioni specifiche del conto da fare alla fine del mese. Dipende
     * dalle varie sottoclassi.
     * 
     */
    public abstract void endOfMonth();

    /**
     * Deposita un importo nel conto.
     * 
     * @param importo
     *                    l'ammontare da depositare
     * @throws IllegalArgumentException
     *                                      se si tenta di depositare un importo
     *                                      negativo
     */
    public void deposit(double importo) {
        if (importo < 0)
            throw new IllegalArgumentException(
                    "Tentativo di deposito di importo negativo");
        saldo += importo;
    }

    /**
     * Preleva un importo dal conto.
     * 
     * @param importo
     *                    l'ammontare da prelevare
     * @throws IllegalArgumentException
     *                                       se si tenta di prelevare un importo
     *                                       negativo
     * @throws InsufficientFundException
     *                                       se si tenta di prelevare più del
     *                                       saldo corrente
     */
    public void withdraw(double importo) {
        if (importo < 0)
            throw new IllegalArgumentException(
                    "Tentativo di prelievo di importo negativo");
        if (importo > saldo)
            throw new InsufficientFundException(
                    "Tentativo di prelevare più del posseduto");
        saldo -= importo;

    }

    /**
     * Confronta i conti per IBAN in ordine lessicografico.
     * @param other conto da confrontare
     * @return negativo, zero o positivo secondo l'ordine degli IBAN
     * @throws NullPointerException se other è null
     */
    @Override
    public int compareTo(BankAccount other) {
        return this.iban.compareTo(other.iban);
    }

    /**
     * Trasferisce fondi da questo conto a un altro conto.
     * 
     * @param importo
     *                       valore da trasferire
     * @param altroConto
     *                       conto su cui trasferire l'importo
     * @throws NullPointerException
     *                                      se il riferimento all'altro conto è
     *                                      null
     * @throws IllegalArgumentException
     *                                      se l'importo da trasferire è
     *                                      negativo
     */
    public void transfer(double importo, BankAccount altroConto) {
        if (altroConto == null)
            throw new NullPointerException(
                    "Tentativo di trasferire un importo su un conto null");
        if (importo < 0)
            throw new IllegalArgumentException(
                    "Tentativo di trasferire un importo negativo su un altro conto");
        this.withdraw(importo);
        altroConto.deposit(importo);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [iban=" + this.iban + ", nome=" + this.nome
                + ", saldo=" + this.saldo + "]";
    }

}
