package it.unicam.cs.asdl2526.slides.collections;
/**
 * Contenitore generico di un valore: il parametro di tipo {@code T} è
 * scelto dal client alla dichiarazione/istanziazione.
 * <p>Una {@code GenericBox<BankAccount>} non accetta una {@code String}:
 * l'errore viene rilevato dal compilatore, non con un cast a runtime.
 * @param <T> tipo del valore conservato
 */
public class GenericBox<T> {
    private T value;
    /**
     * Documenta il contratto dell’operazione GenericBox. @param value valore iniziale, eventualmente null */
    public GenericBox(T value) { this.value = value; }
    /**
     * Documenta il contratto dell’operazione get. @return valore conservato, del tipo T */
    public T get() { return value; }
    /**
     * Documenta il contratto dell’operazione set. @param value nuovo valore del tipo T */
    public void set(T value) { this.value = value; }
}
