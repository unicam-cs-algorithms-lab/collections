# Collections e generics in Java

Questo progetto Maven accompagna le slide **Collections in Java** del Laboratorio di Algoritmi e Strutture Dati. Il codice riprende l'esempio dei conti bancari utilizzato nelle lezioni su interfacce, polimorfismo ed ereditarietà e lo sviluppa per mostrare come gestire **collezioni dinamiche di oggetti**.

L'obiettivo non è realizzare un'applicazione bancaria completa: le classi servono a comprendere il passaggio dalla gestione manuale di un array alle interfacce e alle implementazioni del **Java Collections Framework**, e a introdurre i **generics** che rendono queste API tipizzate e riutilizzabili.

## Il problema: una banca con molti conti

Un oggetto `Bank` deve conservare più `BankAccount` e offrire operazioni come inserimento, ricerca, cancellazione, visualizzazione e ordinamento. Il numero di conti può variare durante la vita dell'oggetto. Come rappresentare questa collezione nello stato della banca?

Il progetto mette a confronto due soluzioni:

- **`BankArray`** conserva i riferimenti ai conti in un array di capacità fissata alla costruzione. Il campo `lastFreePos` separa la porzione occupata da quella libera. Ricerca, controllo dei duplicati e compattazione dopo una rimozione sono implementati esplicitamente. Se lo spazio termina, l'operazione di inserimento segnala `BankDimensionException`.
- **`Bank`** conserva i conti attraverso un campo dichiarato `List<BankAccount>`, inizializzato con `ArrayList<BankAccount>`. La lista gestisce la propria capacità; la classe `Bank` può concentrarsi sul contratto applicativo, ad esempio impedendo l'inserimento di conti con IBAN già presente.

Le due implementazioni permettono di osservare una distinzione importante: **utilizzare una libreria non elimina la necessità di progettare il contratto della propria classe**. Una `List` ammette duplicati, mentre `Bank.insert` sceglie di non ammettere due conti logicamente uguali.

## Obiettivi didattici

Studiando le classi, le Javadoc e i test è possibile:

- distinguere la **capacità** di un array dal numero di elementi effettivamente memorizzati;
- confrontare un'implementazione manuale delle operazioni di ricerca, inserimento e cancellazione con i metodi di `List<E>`;
- riconoscere il ruolo di un'**interfaccia** (`List`) e di una sua **implementazione** (`ArrayList`), utilizzando riferimenti polimorfi;
- comprendere **parametri di tipo**, classi e metodi generici, vincoli (`extends`), wildcard e type erasure;
- applicare i contratti di `equals`, `hashCode`, `Comparable` e `Comparator` alle collezioni;
- distinguere l'ordine delle posizioni in una lista dall'**ordinamento** degli oggetti contenuti;
- confrontare la scansione con indici, `for-each` e `Iterator`, compresa la rimozione durante la scansione;
- riconoscere le differenze tra `List`, `Set`, `HashSet` e `TreeSet`.

## Come sono organizzate le classi

Tutte le classi principali si trovano nel package `it.unicam.cs.asdl.slides.collections`, sotto `src/main/java`.

### Conti bancari ed ereditarietà

`BankAccount` è la superclasse astratta della gerarchia. `SavingsAccount`, `CheckingAccount` e `TimedAccount` rappresentano specializzazioni con comportamenti differenti. Un riferimento di tipo `BankAccount` può quindi designare oggetti di queste sottoclassi: la banca può gestirli nella stessa collezione e invocare metodi polimorfi, come `endOfMonth()`.

L'identità logica di un conto è determinata dall'**IBAN**. Per questo `BankAccount` ridefinisce `equals` e `hashCode` usando l'identificativo e definisce l'ordinamento naturale per IBAN mediante `Comparable<BankAccount>`. Sono inoltre disponibili comparatori alternativi per intestatario e saldo. Gli esempi riprendono i contratti studiati nelle precedenti lezioni e mostrano perché diventano importanti nelle collezioni.

### Le due banche: `BankArray` e `Bank`

In `BankArray` osservare soprattutto la ricerca lineare, l'inserimento nella prima posizione libera e lo spostamento verso sinistra dei riferimenti dopo una cancellazione. La capacità è fissa per scelta didattica.

In `Bank` individuare le operazioni di `List<BankAccount>` che sostituiscono il codice manuale: `contains`, `indexOf`, `add`, `remove` e i metodi di `Collections` per l'ordinamento. Alcuni metodi che producono descrizioni ordinate **modificano l'ordine della lista interna**: è un effetto da riconoscere consultando il contratto e il codice.

### Gli esempi sui generics

- `GenericBox<T>`: un contenitore il cui tipo del valore è scelto da chi lo utilizza.
- `GenericPair<K,V>`: una classe con due parametri di tipo indipendenti.
- `AccountBox<T extends BankAccount>`: un parametro limitato alla gerarchia dei conti, che consente di usare i metodi dichiarati da `BankAccount`.
- `GenericExamples`: metodi generici ed esempi di wildcard, collegati agli oggetti bancari.

In una dichiarazione come `List<BankAccount>`, `BankAccount` è un **argomento di tipo**; nella dichiarazione `List<E>`, `E` è un **parametro di tipo**. I generics permettono al compilatore di rilevare molti errori che con i *raw types* emergerebbero soltanto durante l'esecuzione.

Un punto da verificare negli esempi: `List<SavingsAccount>` **non** è un sottotipo di `List<BankAccount>`. Una lista dichiarata `List<? extends BankAccount>` permette di leggere elementi come `BankAccount`, ma non di inserirvi un `BankAccount` arbitrario. Una `List<? super BankAccount>` permette invece l'inserimento di conti, mentre in lettura garantisce soltanto il tipo `Object`.

## Scansione delle liste e modifiche strutturali

Un ciclo `for-each` su una `List` utilizza un iteratore per la scansione. Modificare direttamente la struttura della lista durante quel ciclo, per esempio con `accounts.remove(b)`, può provocare una `ConcurrentModificationException`: quella chiamata **non** viene trasformata automaticamente in `iterator.remove()`.

Per rimuovere elementi durante una scansione con `Iterator`, se l'operazione è supportata, occorre usare `iterator.remove()`. Il metodo `Bank.removeAccountsWithBalanceLessThanOrEqualTo` ne fornisce un esempio. In `changeAccountsWithBalanceLessThan`, invece, i conti sostitutivi sono accumulati in una seconda lista e aggiunti al termine della scansione. Questo modifica anche la loro posizione rispetto ai conti conservati.

Il comportamento *fail-fast* degli iteratori è una verifica *best effort*, non una garanzia che ogni modifica impropria venga rilevata. Modificare lo **stato di un conto** (per esempio con `deposit`) non equivale a modificare strutturalmente la **lista dei riferimenti ai conti**.

## Liste, insiemi e ordinamenti

Una `List<E>` conserva una sequenza indicizzata e può contenere elementi duplicati. Un `Set<E>` rappresenta invece una collezione senza duplicati secondo il criterio di equivalenza adottato dall'implementazione.

`HashSet` si basa su `hashCode` ed `equals`; `TreeSet` mantiene gli elementi ordinati e usa l'ordinamento naturale o un `Comparator` anche per stabilire quando due elementi sono equivalenti ai fini del set. Per ottenere un comportamento coerente con il contratto generale di `Set`, l'ordinamento dovrebbe essere compatibile con `equals`.

Gli ordinamenti alternativi dei conti usano l'IBAN come criterio finale per distinguere conti diversi con lo stesso nome o saldo. Poiché nome e saldo sono mutabili, modificarli dopo l'inserimento in un insieme ordinato con tali comparatori può comprometterne il funzionamento.

## Come studiare con il progetto

Un possibile percorso è leggere prima `BankArray`, individuando le responsabilità della gestione manuale; confrontarla poi con `Bank`, cercando le operazioni delegate ad `ArrayList`; esaminare le classi sui generics insieme alle slide; infine osservare nei test il comportamento di ricerca, uguaglianza, ordinamento, iteratori e insiemi.

`BankTestAMano` contiene un esempio eseguibile tramite `main`. I test automatici JUnit 5 si trovano in `src/test/java`, in particolare in `CollectionsDidacticTest`. I commenti dei test spiegano le proprietà delle API che vengono verificate. Le Javadoc documentano i contratti pubblici delle classi; i commenti ordinari spiegano i passaggi implementativi più significativi.

## Compilazione, test e Javadoc

Il progetto è configurato per **Java 8** e utilizza Maven. Dalla cartella che contiene `pom.xml`:

```bash
mvn test
```

Per compilare e creare il pacchetto:

```bash
mvn package
```

Per generare la documentazione delle API con lo strumento Javadoc del JDK:

```bash
javadoc -d docs -encoding UTF-8 -sourcepath src/main/java \
  -subpackages it.unicam.cs.asdl.slides.collections
```

La documentazione generata si trova nella cartella `docs`; la pagina iniziale è `docs/index.html`.

## Collegamenti con il resto del corso

Il progetto riprende **ereditarietà e polimorfismo** della gerarchia dei conti bancari, **stato e riferimenti** degli oggetti e i contratti di **uguaglianza e ordinamento** già incontrati nelle lezioni e nelle esercitazioni. Il passaggio da `BankArray` a `Bank` è analogo all'evoluzione prevista per `Aula` nelle esercitazioni: prima la gestione esplicita degli array, poi l'utilizzo di `List<E>` e `Set<E>`.

Il modello bancario è volutamente semplificato: l'uso di `double` per gli importi, l'assenza di persistenza e la mancata gestione della concorrenza non lo rendono adatto a un'applicazione bancaria reale. Sono scelte che consentono di concentrare l'attenzione sui concetti di Java e sulle strutture dati.
