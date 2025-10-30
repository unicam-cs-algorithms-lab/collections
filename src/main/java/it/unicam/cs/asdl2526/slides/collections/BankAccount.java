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
            Double bb1 = new Double(b1.saldo);
            Double bb2 = new Double(b2.saldo);
            int c = bb1.compareTo(bb2);
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
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome
     *                 the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the saldo
     */
    public double getSaldo() {
        return saldo;
    }

    /**
     * @return the iban
     */
    public String getIban() {
        return iban;
    }

    /*
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

    /*
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

    /*
     * L'ordinamento tra conti correnti segue l'ordinamento delle stringhe iban.
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
