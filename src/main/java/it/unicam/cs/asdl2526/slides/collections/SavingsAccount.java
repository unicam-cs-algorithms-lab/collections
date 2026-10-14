/**
     * Documenta il contratto dell’operazione operazione.
 * 
 */
package it.unicam.cs.asdl2526.slides.collections;

/**
 * Un conto di risparmio con un tasso di interesse.
 * 
 * @author Luca Tesei
 *
 */
public class SavingsAccount extends BankAccount {
    private double interestRate;

    /**
     * Costruisce un conto con un tasso di interesse di default dell 1%.
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
    public SavingsAccount(double saldo, String nome, String iban) {
        super(saldo, nome, iban);
        this.interestRate = 1.0;
    }

    /**
     * Costruisce un conto con un tasso di interesse specifico.
     * 
     * @param saldo
     *                         saldo iniziale
     * @param nome
     *                         nome titolare
     * @param iban
     *                         codice IBAN
     * @param interestRate
     *                         tasso di interesse
     * @throws NullPointerException
     *                                      Exception se il nome o l'iban sono
     *                                      nulli
     * @throws IllegalArgumentException
     *                                      se il saldo iniziale o il tasso di
     *                                      interesse è negativo
     */
    public SavingsAccount(double saldo, String nome, String iban,
            double interestRate) {
        super(saldo, nome, iban);
        if (interestRate < 0)
            throw new IllegalArgumentException(
                    "Tentativo di creare un conto con un tasso di "
                            + "interesse negativo");
        this.interestRate = interestRate;
    }

    /**
     * Calcola gli interessi mensili e li aggiunge al saldo. Si assume per
     * semplicità che nel mese corrente l'importo sia rimasto costante
     * (altrimenti il calcolo degli interessi dovrebbe tener conto degli importi
     * di ogni giorno). Il tasso è annuale quindi viene diviso per 12 mesi.
     * 
     */
    @Override
    public void endOfMonth() {
        double interesse = (getSaldo() * interestRate / 100) / 12;
        /*
         * Uso il metodo ereditato dalla superclasse che può accedere alla
         * variabile istanza privata saldo
         */
        deposit(interesse);
    }

    /**
     * Documenta il contratto dell’operazione getInterestRate.
     * @return the interestRate
     */
    public double getInterestRate() {
        return interestRate;
    }

    /**
     * @param tassoDiInteresse
     *                         the interestRate to set
     */
    public void setTassoDiInteresse(double tassoDiInteresse) {
        this.interestRate = tassoDiInteresse;
    }

}
