/**
     * Documenta il contratto dell’operazione operazione.
 * 
 */
package it.unicam.cs.asdl2526.slides.collections;

/**
 * Eccezione che indica che il massimo numero di conti di una banca è stato
 * raggiunto.
 * 
 * @author Luca Tesei
 *
 */
public class BankDimensionException extends RuntimeException {

    /**
     * Documenta il contratto dell’operazione BankDimensionException.
     * 
     */
    private static final long serialVersionUID = 6741919390005964432L;

    /**
     * Documenta il contratto dell’operazione BankDimensionException.
     * 
     */
    public BankDimensionException() {
    }

    /**
     * Documenta il contratto dell’operazione BankDimensionException.
     * @param message
     */
    public BankDimensionException(String message) {
        super(message);
    }

    /**
     * Documenta il contratto dell’operazione BankDimensionException.
     * @param cause
     */
    public BankDimensionException(Throwable cause) {
        super(cause);
    }

    /**
     * Documenta il contratto dell’operazione BankDimensionException.
     * @param message
     * @param cause
     */
    public BankDimensionException(String message, Throwable cause) {
        super(message, cause);
    }

}
