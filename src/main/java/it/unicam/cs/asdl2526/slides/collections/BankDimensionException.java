/**
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
     * 
     */
    private static final long serialVersionUID = 6741919390005964432L;

    /**
     * 
     */
    public BankDimensionException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     */
    public BankDimensionException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public BankDimensionException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public BankDimensionException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

}
