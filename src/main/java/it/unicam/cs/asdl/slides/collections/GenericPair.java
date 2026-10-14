package it.unicam.cs.asdl.slides.collections;
/**
 * Coppia immutabile di valori potenzialmente di tipi differenti.
 * @param <K> tipo della chiave
 * @param <V> tipo del valore
 */
public class GenericPair<K,V> {
    private final K key;
    private final V value;
    /**
     * Documenta il contratto dell’operazione GenericPair. @param key chiave della coppia @param value valore associato */
    public GenericPair(K key, V value) { this.key = key; this.value = value; }
    /**
     * Documenta il contratto dell’operazione getKey. @return la chiave */
    public K getKey() { return key; }
    /**
     * Documenta il contratto dell’operazione getValue. @return il valore */
    public V getValue() { return value; }
}
