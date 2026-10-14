/**
     * Documenta il contratto dell’operazione operazione.
 * 
 */
package it.unicam.cs.asdl.slides.collections;

/**
 * Eccezione che viene lanciata quando il saldo attuale di un conto non è
 * sufficiente per eseguire l'operazione in corso.
 * 
 * @author Luca Tesei
 *
 */
public class InsufficientFundException extends RuntimeException {

    private static final long serialVersionUID = 6741919390005964432L;

    /**
     * Costruisce l'eccezione.
     */
    public InsufficientFundException() {
        super();
    }

    /**
     * Costruisce l'eccezione con il messaggio associato. 
     * 
     * @param message messaggio associato all'eccezione
     */
    public InsufficientFundException(String message) {
        super(message);
    }

}
