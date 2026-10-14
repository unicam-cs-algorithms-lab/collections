/**
     * Documenta il contratto dell’operazione operazione.
 * 
 */
package it.unicam.cs.asdl2526.slides.collections;

/**
 * Un conto corrente con spese fisse per ogni operazione effettuata.
 * 
 * @author Luca Tesei
 *
 */
public class CheckingAccount extends BankAccount {

    private int numeroOperazioni;

    private double costoPerOperazione;

    /**
     * Costruisce un conto specificando come costo per ogni operazione il valore
     * di default di 1 EUR.
     * 
     * @param saldo
     *                  saldo iniziale
     * @param nome
     *                  nome titolare
     * @param iban
     *                  codice IBAN
     * @throws NullPointerException
     *                                      Exception se il nome o l'iban sono
     *                                      nulli
     * @throws IllegalArgumentException
     *                                      se il saldo iniziale è negativo
     */
    public CheckingAccount(double saldo, String nome, String iban) {
        super(saldo, nome, iban);
        this.costoPerOperazione = 1.0;
        this.numeroOperazioni = 0;
    }

    /**
     * Costruisce un conto specificando il costo per ogni operazione.
     * 
     * @param saldo
     *                               saldo iniziale
     * @param nome
     *                               nome titolare
     * @param iban
     *                               codice IBAN
     * @param costoPerOperazione
     *                               costo di una singola operazione sul conto
     * @throws NullPointerException
     *                                      Exception se il nome o l'iban sono
     *                                      nulli
     * @throws IllegalArgumentException
     *                                      se il saldo iniziale o il costo per
     *                                      ogperazione è negativo
     */
    public CheckingAccount(double saldo, String nome, String iban,
            double costoPerOperazione) {
        super(saldo, nome, iban);
        if (costoPerOperazione < 0)
            throw new IllegalArgumentException(
                    "Tentativo di creare un CheckingAccount con un costo per operazione negativo");
        this.costoPerOperazione = costoPerOperazione;
        this.numeroOperazioni = 0;
    }

    /**
     * Addebita le spese delle operazioni a fine mese. Il costo viene calcolato
     * moltiplicando il numero di operazioni contate fino a questo momento per
     * il costo unitario. Il numero delle operazioni viene azzerato per
     * effettuare il conto per il prossimo mese.
     * 
     * @throws InsufficientFundException
     *                                       se il saldo attuale non è
     *                                       sufficiente a coprire le spese
     *                                       delle operazioni
     * 
     */
    @Override
    public void endOfMonth() {
        double costo = this.numeroOperazioni * this.costoPerOperazione;
        if (this.getSaldo() < costo)
            throw new InsufficientFundException(
                    "Saldo insufficiente per pagare le spese di operazione");
        super.withdraw(costo);
        this.numeroOperazioni = 0;
    }

    /*
     * Una operazione di deposit va contata per il calcolo delle spese. Per il
     * resto si usa il metodo ereditato, che può accedere alla variabile privata
     * della superclasse.
     */
    public void deposit(double importo) {
        this.numeroOperazioni++;
        super.deposit(importo);
    }

    /*
     * Una operazione di withdraw va contata per il calcolo delle spese. Per il
     * resto si usa il metodo ereditato, che può accedere alla variabile privata
     * della superclasse.
     */
    public void withdraw(double importo) {
        this.numeroOperazioni++;
        super.withdraw(importo);
    }

}
