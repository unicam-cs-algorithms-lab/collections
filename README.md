# Collections e generics in Java — Laboratorio ASD

Questo progetto Maven accompagna le slide **Collections** e il modulo introduttivo **Generics in Java**. Richiede Java 8.

## Obiettivi

1. Confrontare la gestione manuale di un insieme dinamico con `BankArray` (array di capacità fissa, indice `lastFreePos`, ricerca lineare, compattazione) e la gestione tramite `Bank` (`List<BankAccount>` realizzata con `ArrayList<BankAccount>`).
2. Riconoscere la distinzione tra **interfaccia** e **implementazione** e il vantaggio delle variabili polimorfe.
3. Comprendere il ruolo dei parametri di tipo: `E`, `T`, `K`, `V`, bounded types, wildcard e type erasure; riconoscere gli errori di tipo a compilazione.
4. Riutilizzare i contratti `equals`, `hashCode`, `Comparable<BankAccount>` e `Comparator<BankAccount>` per ricerca, duplicati e ordinamenti.
5. Confrontare `for-each`, iteratore e accesso per indice; usare `Iterator.remove()` per rimuovere durante la scansione.
6. Comprendere `List` e `Set` come contratti differenti, e il ruolo di `HashSet` e `TreeSet`.

## Mappa delle classi

- `BankAccount`: classe astratta, identità definita dall'IBAN; ordine naturale per IBAN; comparatori alternativi per nome e saldo.
- `SavingsAccount`, `CheckingAccount`, `TimedAccount`: sottoclassi che illustrano il polimorfismo nei contenitori.
- `BankArray`: archivio a capacità fissa, ricerca e cancellazione implementate manualmente.
- `Bank`: archivio tramite `List<BankAccount>`; usa `contains`, `indexOf`, `remove`, iteratori e `Collections.sort`.
- `GenericBox<T>`, `GenericPair<K,V>`, `AccountBox<T extends BankAccount>`, `GenericExamples`: esempi autonomi per i generics, collegati al dominio bancario.
- `BankTestAMano`: dimostrazione manuale; i test JUnit si trovano in `src/test/java`.

## Da array a List

`BankArray` mantiene un array di dimensione prefissata e un contatore degli elementi occupati; quando la capacità è esaurita segnala `BankDimensionException`. `Bank` delega la gestione della capacità ad `ArrayList`. Entrambe le classi vietano duplicati **secondo `equals`**, anche se `List` in generale ammette duplicati. L'ordine di una lista è significativo; non confondere questa proprietà con l'ordinamento naturale degli elementi.

La ricerca in `ArrayList` con `contains`/`indexOf` è normalmente lineare: passare a una libreria non significa che ogni operazione diventi più veloce. `Collections.sort` riordina la lista stessa: anche i metodi che restituiscono descrizioni ordinate cambiano l'ordine interno dei conti.

## Identità, ordinamento e mutabilità

L'identità del conto dipende dall'IBAN, immutabile; nome e saldo possono cambiare. I comparatori per nome e saldo usano l'IBAN per distinguere conti diversi quando il primo criterio coincide. Poiché il nome e il saldo sono mutabili, non è sicuro modificarli mentre gli oggetti sono chiavi di un insieme ordinato secondo tali comparatori.

## Iterazione e modifiche strutturali

Il `for-each` è adatto alla lettura. Per rimuovere elementi mentre si percorre una `ArrayList`, usare l'`Iterator` che effettua la scansione. Il comportamento fail-fast è un controllo *best effort*: non è una garanzia di rilevazione di ogni modifica concorrente. Il metodo `changeAccountsWithBalanceLessThan` raccoglie separatamente i nuovi conti e li aggiunge dopo la scansione: questo ne cambia anche la posizione relativa nella lista.

## Generics: che cosa provare

In `GenericExamples` si trovano un contenitore tipizzato, una coppia chiave-valore, un limite superiore `extends BankAccount`, un metodo generico e un metodo con wildcard. `List<SavingsAccount>` **non è** un sottotipo di `List<BankAccount>`; `List<? extends BankAccount>` consente di leggere i valori come `BankAccount`, ma non di aggiungere un conto arbitrario. Non è possibile verificare `instanceof GenericBox<String>` a runtime: il parametro di tipo è soggetto a erasure.

## Esecuzione

Dalla radice del progetto:

```bash
mvn test
mvn package
```

Per generare la documentazione API:

```bash
javadoc -d docs -encoding UTF-8 -sourcepath src/main/java -subpackages it.unicam.cs.asdl2526.slides.collections
```

## Limiti intenzionali degli esempi

Il modello bancario è semplificato, non adatto a gestire denaro reale: usa `double` anziché `BigDecimal`, non offre persistenza né concorrenza e non garantisce transazioni atomiche fra conti.
