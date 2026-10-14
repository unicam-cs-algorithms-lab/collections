/**
     * Documenta il contratto dell’operazione operazione.
 * 
 */
package it.unicam.cs.asdl2526.slides.collections;

/**
 * Un particolare conto di risparmio vincolato con un tasso aggiuntivo sui mesi
 * in cui l'importo è vincolato. Se non c'è nessuna operazione di prelievo nei
 * mesi vincolati alla fine del periodo del vincolo vengono accreditati gli
 * interessi aggiuntivi maturati. Se c'è una operazione di prelievo prima della
 * fine del vincolo si ricominciano a contare i mesi dall'inizio. Dopo la fine
 * del primo periodo di vincolo il contratto deve essere esplicitamente
 * rinnovato per continuare ad avere il tasso aggiuntivo.
 * 
 * @author Luca Tesei
 *
 */
public class TimedAccount extends SavingsAccount {
    private int numeroMesiVincolo;

    private int mesiRimanenti;

    private double tassoAggiuntivo;

    private boolean contrattoAttivo;

    private double accumuloInteressiVincolo;

    /**
     * Costruisce un conto con contratto di vincolo attivo di default per 18
     * mesi al tasso di interesse del 1% e al tasso aggiuntivo del 3%.
     * 
     * @param saldo
     *                  saldo iniziale
     * @param nome
     *                  nome titolare
     * @param iban
     *                  codice IBAN
     * 
     * @throws NullPointerException
     *                                      Exception se il nome o l'iban sono
     *                                      nulli
     * @throws IllegalArgumentException
     *                                      se il saldo iniziale è negativo
     */
    public TimedAccount(double saldo, String nome, String iban) {
        super(saldo, nome, iban, 1.0);
        this.numeroMesiVincolo = 18;
        this.mesiRimanenti = 18;
        this.tassoAggiuntivo = 3.0;
        this.contrattoAttivo = true;
        this.accumuloInteressiVincolo = 0;
    }

    /**
     * Costruisce un conto con contratto di vincolo attivo di default per 18
     * mesi a un tasso di interesse specifico e un tasso aggiuntivo del 3%.
     * 
     * @param saldo
     *                           saldo iniziale
     * @param nome
     *                           nome titolare
     * @param iban
     *                           codice IBAN
     * @param tassoInteresse
     *                           tasso di interesse di base
     * @throws NullPointerException
     *                                      Exception se il nome o l'iban sono
     *                                      nulli
     * @throws IllegalArgumentException
     *                                      se il saldo iniziale o il tasso è
     *                                      negativo
     */
    public TimedAccount(double saldo, String nome, String iban,
            double tassoInteresse) {
        super(saldo, nome, iban, tassoInteresse);
        this.numeroMesiVincolo = 18;
        this.mesiRimanenti = 18;
        this.tassoAggiuntivo = 3.0;
        this.contrattoAttivo = true;
        this.accumuloInteressiVincolo = 0;
    }

    /**
     * Costruisce un conto con contratto di vincolo attivo di default per 18
     * mesi a un tasso di interesse specifico e un tasso aggiuntivo del 3%.
     * 
     * @param saldo
     *                            saldo iniziale
     * @param nome
     *                            nome titolare
     * @param iban
     *                            codice IBAN
     * @param tassoInteresse
     *                            tasso di interesse di base
     * @param tassoAggiuntivo
     *                            tasso di interesse aggiuntivo
     * @throws NullPointerException
     *                                      Exception se il nome o l'iban sono
     *                                      nulli
     * @throws IllegalArgumentException
     *                                      se il saldo iniziale o uno dei due
     *                                      tassi sono negativi
     */
    public TimedAccount(double saldo, String nome, String iban,
            double tassoInteresse, double tassoAggiuntivo) {
        super(saldo, nome, iban, tassoInteresse);
        this.numeroMesiVincolo = 18;
        this.mesiRimanenti = 18;
        if (tassoAggiuntivo < 0)
            throw new IllegalArgumentException(
                    "Tentativo di creare un conto con un tasso aggiuntivo negativo");
        this.tassoAggiuntivo = tassoAggiuntivo;
        this.contrattoAttivo = true;
        this.accumuloInteressiVincolo = 0;
    }

    /**
     * Costruisce un conto con contratto di vincolo attivo per un numero di mesi
     * specifico a un tasso aggiuntivo specifico.
     * 
     * @param saldo
     *                              saldo iniziale
     * @param nome
     *                              nome titolare
     * @param iban
     *                              codice IBAN
     * @param tassoInteresse
     *                              tasso di interesse di base
     * @param tassoAggiuntivo
     *                              tasso di interesse aggiuntivo
     * 
     * @param numeroMesiVincolo
     *                              durata del vincolo in mesi
     * @throws NullPointerException
     *                                      Exception se il nome o l'iban sono
     *                                      nulli
     * @throws IllegalArgumentException
     *                                      se il saldo iniziale, uno dei due
     *                                      tassi o il numero di mesi del
     *                                      vincolo sono negativi
     *
     */
    public TimedAccount(double saldo, String nome, String iban,
            double tassoInteresse, double tassoAggiuntivo,
            int numeroMesiVincolo) {
        super(saldo, nome, iban, tassoInteresse);
        if (tassoAggiuntivo < 0)
            throw new IllegalArgumentException(
                    "Tentativo di creare un conto con un tasso aggiuntivo negativo");
        this.tassoAggiuntivo = tassoAggiuntivo;
        if (numeroMesiVincolo < 0)
            throw new IllegalArgumentException(
                    "Tentativo di creare un conto con un numero di mesi di vincolo negativo");
        this.numeroMesiVincolo = numeroMesiVincolo;
        this.mesiRimanenti = numeroMesiVincolo;
        this.contrattoAttivo = true;
        this.accumuloInteressiVincolo = 0;
    }

    /*
     * Un prelievo fa ripartire il numero di mesi del vincolo.
     */
    public void withdraw(double importo) {
        super.withdraw(importo);
        if (this.contrattoAttivo) {
            if (this.mesiRimanenti > 0)
                // Rotto il vincolo, riparte il conteggio dei mesi
                this.mesiRimanenti = this.numeroMesiVincolo;
        }
    }

    /*
     * Oltre all'interesse di base, viene accreditato anche l'interesse
     * aggiuntivo dal vincolo alla fine del vincolo.
     * 
     * @see it.unicam.cs.asdl1617.bankaccounts.SavingsAccount#endOfMonth()
     */
    @Override
    public void endOfMonth() {
        super.endOfMonth();
        if (!this.contrattoAttivo)
            // se il contratto non è attivo non faccio niente
            return;
        // Calcolo interessi vincolo e accumulo
        double interessiVincolo = (this.getSaldo() * this.tassoAggiuntivo / 100)
                / 12;
        this.accumuloInteressiVincolo += interessiVincolo;
        if (this.mesiRimanenti > 0)
            this.mesiRimanenti--;
        else if (this.mesiRimanenti == 0) {
            // Accredito gli intressi del vincolo e concludo il contratto
            super.deposit(this.accumuloInteressiVincolo);
            this.contrattoAttivo = false;
        }
    }

    /**
     * Rinnova il contratto per un ulteriore periodo.
     * 
     * @param numeroMesiVincolo
     *                              numero di mesi del vincolo
     * @param tassoAggiuntivo
     *                              tasso aggiuntivo del vincolo
     * 
     * @throws IllegalStateException
     *                                      se viene chiamato durante un periodo
     *                                      di vincolo attivo
     * @throws IllegalArgumentException
     *                                      se il numero di mesi del vincolo o
     *                                      il tasso aggiuntivo sono negativi
     */
    public void renewContract(int numeroMesiVincolo, double tassoAggiuntivo) {
        if (this.contrattoAttivo)
            throw new IllegalStateException(
                    "Tentativo di rinnovare un contratto non ancora scaduto");
        if (numeroMesiVincolo < 0)
            throw new IllegalArgumentException(
                    "Tentativo di rinnovare un contratto con un numero di mesi di vincolo negativo");

        if (tassoAggiuntivo < 0)
            throw new IllegalArgumentException(
                    "Tentativo di rinnovare un contratto con un tasso aggiuntivo negativo");
        this.numeroMesiVincolo = numeroMesiVincolo;
        this.mesiRimanenti = numeroMesiVincolo;
        this.tassoAggiuntivo = tassoAggiuntivo;
        this.accumuloInteressiVincolo = 0;
        this.contrattoAttivo = true;
    }

    /**
     * Restituisce lo stato del contratto di vincolo: attivo o no.
     * 
     * @return se il vincolo è attualmente attivo, false altrimenti
     */
    public boolean isActive() {
        return this.contrattoAttivo;
    }
}
