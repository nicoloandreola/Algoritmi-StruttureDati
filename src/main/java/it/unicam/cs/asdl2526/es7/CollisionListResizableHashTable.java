/**
 * 
 */
package it.unicam.cs.asdl2526.es7;

import java.util.*;

/**
 * Realizza un insieme tramite una tabella hash con indirizzamento primario (la
 * funzione di hash primario deve essere passata come parametro nel costruttore
 * e deve implementare l'interface PrimaryHashFunction) e liste di collisione.
 * 
 * La tabella, poiché implementa l'interfaccia Set<E> non accetta elementi
 * duplicati (individuati tramite il metodo equals() che si assume sia
 * opportunamente ridefinito nella classe E) e non accetta elementi null.
 *
 * La tabella ha una dimensione iniziale di default (16) e un fattore di
 * caricamento di default (0.75). Quando il fattore di bilanciamento effettivo
 * eccede quello di default la tabella viene raddoppiata e viene fatto un
 * riposizionamento di tutti gli elementi.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 */
public class CollisionListResizableHashTable<E> implements Set<E> {

    /*
     * La capacità iniziale. È una potenza di due e quindi la capacità sarà
     * sempre una potenza di due, in quanto ogni resize raddoppia la tabella.
     */
    private static final int INITIAL_CAPACITY = 16;

    /*
     * Fattore di bilanciamento di default. Tipico valore.
     */
    private static final double LOAD_FACTOR = 0.75;

    /*
     * Numero di elementi effettivamente presenti nella hash table in questo
     * momento. ATTENZIONE: questo valore è diverso dalla capacity, che è la
     * lunghezza attuale dell'array di Object che rappresenta la tabella.
     */
    private int size;

    /*
     * L'idea è che l'elemento in posizione i della tabella hash è un bucket che
     * contiene null oppure il puntatore al primo nodo di una lista concatenata
     * di elementi. Si può riprendere e adattare il proprio codice della
     * Esercitazione 6 che realizzava una lista concatenata di elementi
     * generici. La classe interna Node<E> è ripresa proprio da lì.
     * 
     * ATTENZIONE: la tabella hash vera e propria può essere solo un generico
     * array di Object e non di Node<E> per una impossibilità del compilatore di
     * accettare di creare array a runtime con un tipo generics. Ciò infatti
     * comporterebbe dei problemi nel sistema di check dei tipi Java che, a
     * run-time, potrebbe eseguire degli assegnamenti in violazione del tipo
     * effettivo della variabile. Quindi usiamo un array di Object che
     * riempiremo sempre con null o con puntatori a oggetti di tipo Node<E>.
     * 
     * Per inserire un elemento nella tabella possiamo usare il polimorfismo di
     * Object:
     * 
     * this.table[i] = new Node<E>(item, next);
     * 
     * ma quando dobbiamo prendere un elemento dalla tabella saremo costretti a
     * fare un cast esplicito:
     * 
     * Node<E> myNode = (Node<E>) this.table[i];
     * 
     * Ci sarà dato un warning di cast non controllato, ma possiamo eliminarlo
     * con un tag @SuppressWarning,
     */
    private Object[] table;

    /*
     * Funzione di hash primaria usata da questa hash table. Va inizializzata nel
     * costruttore all'atto di creazione dell'oggetto.
     */
    private final PrimaryHashFunction phf;

    /*
     * Contatore del numero di modifiche. Serve per rendere l'iterator
     * fail-fast.
     */
    private int modCount;

    // I due metodi seguenti sono di comodo per gestire la capacity e la soglia
    // oltre la quale bisogna fare il resize.

    /* Lunghezza attuale della tabella corrente */
    private int getCurrentCapacity() {
        return this.table.length;
    }

    /*
     * Valore corrente soglia oltre la quale si deve fare la resize,
     * getCurrentCapacity * LOAD_FACTOR
     */
    private int getCurrentThreshold() {
        return (int) (getCurrentCapacity() * LOAD_FACTOR);
    }

    /**
     * Costruisce una Hash Table con capacità iniziale di default e fattore di
     * caricamento di default.
     */
    public CollisionListResizableHashTable(PrimaryHashFunction phf) {
        this.phf = phf;
        this.table = new Object[INITIAL_CAPACITY];
        this.size = 0;
        this.modCount = 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public boolean contains(Object o) {
        /*
         * ATTENZIONE: usare l'hashCode dell'oggetto e la funzione di hash primaria
         * passata all'atto della creazione: il bucket in cui cercare l'oggetto
         * o è la posizione this.phf.hash(o.hashCode(),this.getCurrentCapacity)
         * 
         * In questa posizione, se non vuota, si deve cercare l'elemento o
         * utilizzando il metodo equals() su tutti gli elementi della lista
         * concatenata lì presente
         * 
         */
        if (o == null)
            throw new NullPointerException("La tabella NON contiene elementi NULLI!");
        // Salvo la posizione in cui cercare
        int index = this.phf.hash(o.hashCode(), this.getCurrentCapacity());
        // Se la posizione è vuota allora significa che sicuramente non c'è
        if(this.table[index] == null)
            return false;
        // Altrimenti cerco se l'oggetto è presente in quel BUCKET, creando un
        // nuovo nodo che inizializzo con il primo, ovvero quello a cui punta index
        // (devo fare il cast in quanto table è un array di Object)
        @SuppressWarnings("unchecked")
        Node<E> current = (Node<E>) this.table[index];
        // Scorro tutta la lista (BUCKET) corrente: risolvo in questo modo e non
        // con un ITERATORE poiché quest'ultimo scorre tutta la tabella (spreco) mentre
        // a noi basta scorrere la lista degli oggetti che sono in posizione INDEX
        // (se l'elemento passato è presente, deve avere lo stesso HASH, quindi la
        // funzione di hashing avrà restituito lo stesso intero salvato in INDEX,
        // dunque l'elemento si troverebbe per forza nel BUCKET di posizione INDEX)
        while(current != null) {
            // Se ne trovo uno uguale restituisco TRUE
            if(o.equals(current.item))
                return true;
            // Altrimenti vado avanti nella lista di collisione
            current = current.next;
        }
        // se arrivo qui significa che ho scorso tutta la lista
        return false; // senza trovarlo, quindi ritorno FALSE
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Operazione non supportata");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Operazione non supportata");
    }

    @Override
    public boolean add(E e) {
        /*
         * ATTENZIONE: usare l'hashCode dell'oggetto e la funzione di hash
         * primaria passata all'atto della creazione: il bucket in cui inserire
         * l'oggetto o è la posizione
         * this.phf.hash(o.hashCode(),this.getCurrentCapacity)
         * 
         * In questa posizione, se non vuota, si deve inserire l'elemento o
         * nella lista concatenata lì presente. Se vuota, si crea la lista
         * concatenata e si inserisce l'elemento, che sarà l'unico.
         * 
         */
        // ATTENZIONE, si inserisca prima il nuovo elemento e poi si controlli
        // se bisogna fare resize(), cioè se this.size > this.getCurrentThreshold()
        if(e == null)
            throw new NullPointerException("La tabella NON accetta elementi NULLI!");
        // Salvo la posizione in cui devo aggiungere l'elemento
        int index = this.phf.hash(e.hashCode(),this.getCurrentCapacity());
        // Se già presente devo restituire FALSE
        if(this.contains(e))
            return false;
        else { // Altrimenti lo aggiungo in testa alla lista
            // e aggiorno size e modCount
            this.insert(this.table, index, e);
            this.modCount++;
            this.size++;
        }
        // Controllo resize
        if (this.size > this.getCurrentThreshold())
            resize();
        return true;
    }

    /*
     * Metodo privato che inerisce un elemento in una tabella hash con
     * liste di collisione sempre in testa alla lista passata (index):
     * definito per evitare di scrivere lo stesso codice in add e resize
     */

    private void insert(Object[] table, int index, E e) {
        if(table[index] == null)
            // Se la posizione è libera, sto aggiungendo il primo elemento
            // nel BUCKET, quindi il nuovo elemento non avrà successivo
            table[index] = new Node<E>(e,null);
        else {
            // C'è già qualcosa nel BUCKET, cioè ci sono già altri oggetti
            // con lo stesso hash nella tabella (come next gli passo il
            // puntatore all'elemento che è in testa prima di aggiungerlo)
            @SuppressWarnings("unchecked")
            Node<E> head = (Node<E>) table[index];
            table[index] = new Node<>(e, head);
        }
    }

    /*
     * Raddoppia la tabella corrente e riposiziona tutti gli elementi. Da
     * chiamare quando this.size diventa maggiore di getCurrentThreshold()
     */
    private void resize() {
        // Creo la nuova tabella di lunghezza doppia
        Object[] newTable = new Object[this.getCurrentCapacity() * 2];
        // Creo un iteratore per scorrere tutti gli elementi attualmente
        // presenti nella tabella e inserirli in quella nuova (non basta
        // copiarli nella stessa posizione poiché la funzione di hash, dal
        // momento che è cambiata la lunghezza, potrebbe mandarli in una
        // posizione diversa: infatti le passerò la lunghezza della newTable)
        Iterator<E> it = this.iterator();
        while(it.hasNext()) {
            E current = it.next();
            // Determino la posizione in cui si dovrebbe trovare l'oggetto
            // nella nuova tabella e la salvo in una variabile locale
            int index = this.phf.hash(current.hashCode(), newTable.length);
            // Aggiungo l'oggetto nella nuova tabella
            this.insert(newTable, index, current);
        }
        // Aggiorno il campo dell'oggetto con la nuova tabella
        this.table = newTable;

    }

    @Override
    public boolean remove(Object o) {
        /*
         * ATTENZIONE: usare l'hashCode dell'oggetto e la funzione di hash
         * primaria passata all'atto della creazione: il bucket in cui cercare
         * l'oggetto o è la posizione this.phf.hash(o.hashCode(),this.getCurrentCapacity)
         * 
         * In questa posizione, se non vuota, si deve cercare l'elemento o
         * utilizzando il metodo equals() su tutti gli elementi della lista
         * concatenata lì presente. Se presente, l'elemento deve essere
         * eliminato dalla lista concatenata
         * 
         */
        // ATTENZIONE: la rimozione, in questa implementazione, NON comporta
        // mai una resize "al ribasso", cioè un dimezzamento della tabella se si
        // scende sotto il fattore di bilanciamento desiderato.
        if(o == null)
            throw new NullPointerException("La tabella NON contiene elementi NULLI!");
        // Determino la posizione in cui si dovrebbe trovare l'oggetto
        int index = this.phf.hash(o.hashCode(), this.table.length);
        // Se questa posizione è vuota significa che non c'è quindi ritorno FALSE
        if(this.table[index] == null)
            return false;
        // Altrimenti scorro la lista di collisione di quella posizione
        @SuppressWarnings("unchecked")
        Node<E> current = (Node<E>) this.table[index];
        Node<E> previous = null;
        while(current != null)
            // Se è uguale lo rimuovo
            if(o.equals(current.item)) {
                if(previous == null)
                    // Sto eliminando il primo elemento: non importa se è anche
                    // l'unico come in ES6 poiché, se così fosse, current.next
                    // sarebbe NULL, e quindi la posizione index diventerebbe vuota
                    // come previsto (non dobbiamo aggiornare la tail)
                        this.table[index] = current.next;
                else // Sto eliminando un elemento che non è il primo
                // (indifferente se è l'ultimo o uno qualsiasi nel mezzo, a
                // differenza di ES6 poiché non ho un campo tail da aggiornare
                    previous.next = current.next;
                // Aggiorno size e modCount e poi ritorno TRUE
                this.modCount++;
                this.size--;
                return true;
            }
            else { // Se non è uguale vado avanti nella lista
                previous = current;
                current = current.next;
            }
            // Se arrivo qui significa che l'oggetto non è presente
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // utilizzare un iteratore della collection e chiamare il metodo contains
        if (c == null)
            throw new NullPointerException("ContainsAll di collection null");
        Iterator<?> it = c.iterator();
        while(it.hasNext()) {
            Object item = it.next();
            // In accordo con l'API di Set, il metodo containsAll deve lanciare
            // una NullPointerException "if the specified collection contains one
            // or more null elements and this set does not permit null elements
            // (optional), or if the specified collection is null (sopra)"
            if(item == null)
                throw new NullPointerException("ContainsAll di collection che contiene elementi null");
            // Se trovo un elemento di c che non è contenuto in questa Collection
            // posso subito restituire FALSE
            if(!this.contains(item))
                return false;
        }
        // Se arrivo qua tutti gli elementi sono risultati presenti e non nulli
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        // utilizzare un iteratore della collection e chiamare il metodo add
        if (c == null)
            throw new NullPointerException("addAll di collection null");
        Iterator<? extends E> it = c.iterator();
        // Variabile booleana per contrassegnare se aggiungo almeno un elemento
        boolean changed = false;
        while(it.hasNext()) {
            // utilizzo il polimorfismo da sottoclasse sul tipo E
            E item = it.next();
            if(item == null)
                throw new NullPointerException("addAll di collection che contiene elementi null");
            if(this.add(item)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // utilizzare un iteratore della collection e chiamare il metodo remove
        if(c == null)
            throw new NullPointerException("removeAll di collection null");
        Iterator<?> it = c.iterator();
        // Variabile booleana per segnalare se rimuovo almeno un elemento
        boolean changed = false;
        while(it.hasNext()) {
            Object item = it.next();
            if(item == null)
                throw new NullPointerException("removeAll di collection che contiene elementi null");
            // Se almeno un elemento viene rimosso, aggiorno il flag da restituire
            // (ci pensa il metodo remove a controllare se presente)
            if(this.remove(item))
                changed = true;
        }
        // Se ne avrò tolto almeno uno, changed sarà uguale a TRUE, altrimenti a FALSE
        return changed;
    }

    @Override
    public void clear() {
        // Ritorno alla situazione iniziale
        this.table = new Object[INITIAL_CAPACITY];
        this.size = 0;
        this.modCount++;
    }

    /*
     * Classe per i nodi della lista concatenata. Lo specificatore è protected
     * solo per permettere i test JUnit.
     */
    protected static class Node<E> {
        protected E item;

        protected Node<E> next;

        /*
         * Crea un nodo "singolo" equivalente a una lista con un solo elemento.
         */
        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }
    }

    /*
     * Classe che realizza un iteratore per questa hash table. L'ordine in cui
     * vengono restituiti gli oggetti presenti non è rilevante, ma ogni oggetto
     * presente deve essere restituito dall'iteratore una e una sola volta.
     * L'iteratore deve essere fail-fast, cioè deve lanciare una eccezione
     * ConcurrentModificationException se a una chiamata di next() si "accorge"
     * che la tabella è stata cambiata rispetto a quando esso è stato creato
     */
    private class Itr implements Iterator<E> {

        private Node<E> lastReturned;

        private int currentIndex;

        private final int numeroModificheAtteso;

        private Itr() {
            // All'inizio non è ancora stato restituito niente
            this.lastReturned = null;
            this.currentIndex = 0;
            this.numeroModificheAtteso = modCount;
        }

        @Override
        public boolean hasNext() {
            // Se currentIndex ha raggiunto la dimensione della tabella
            // significa che la ha già scorsa tutta (ultima posizione
            // valida della tabella ricordiamo che è table.length - 1)
            if(currentIndex == CollisionListResizableHashTable.this.table.length)
                return false;
            // Ricerco la prima posizione esistente non null
            while(this.currentIndex < CollisionListResizableHashTable.this.table.length
                    && CollisionListResizableHashTable.this.table[currentIndex] == null)
                this.currentIndex++;
            // Uscito dal while devo ricontrollare se currentIndex è uguale alla
            // dimensione della tabella in quanto dentro il ciclo lo incremento
            // di 1 (se ci entro quando currentIndex è l'ultima posizione valida,
            // una volta uscito non sarà più un indice valido e dovrò restituire
            // false). In alternativa avrei potuto cambiare il controllo nel while
            // in currentIndex < table.length - 1, ma poi dovrei lo stesso fare un
            // controllo fuori dal ciclo quindi non cambia nulla (dovrei controllare
            // se table[currentIndex] è diverso da null poiché se punta all'ultimo
            // indice valido, per la valutazione CORTOCIRCUITATA, non ho nemmeno
            // controllato se in quella posizione c'è effettivamente qualcosa
            /* if (CollisionListResizableHashTable.this.table[currentIndex] == null)
                   return false; */
            if (currentIndex == table.length)
                return false;
            // currentIndex punta alla prima posizione non NULL
            if(lastReturned == null)
                // sono all'inizio della lista di collisioni corrente
                return true;
            else if(lastReturned.next != null)
                // c'è ancora almeno un elemento nella lista corrente
                return true;
            else {
                // La lista corrente è terminata, quindi devo ripetere tutto
                // per la prossima posizione della tabella NON NULLA (richiamo
                // ricorsivamente il metodo). Prima però incremento currentIndex
                // e riporto a NULL lastReturned in quanto inizierò una nuova lista
                this.currentIndex++;
                this.lastReturned = null;
                return hasNext();
            }
        }

        @Override
        public E next() {
            // controllo concorrenza
            if(this.numeroModificheAtteso != CollisionListResizableHashTable.this.modCount)
                throw new ConcurrentModificationException("La lista è stata modificata durante l'iterazione");
            // controllo hasNext()
            if(!hasNext())
                throw new NoSuchElementException("ELENCO TERMINATO!");
            // C'é sicuramente un successivo da tirar fuori, e la chiamata ad
            // hasNext avrà aggiornato currentIndex e lastReturned in modo tale
            // che indichino esattamente il primo elemento successivo non NULLO
            E result;
            if(this.lastReturned == null) {
                // sono all'inizio della lista corrente
                Node<E> head = (Node<E>) CollisionListResizableHashTable.this.table[currentIndex];
                result = head.item;
                this.lastReturned = head;
            }
            else {
                // sono nel mezzo di una lista di collisioni e lastNode.next non
                // è null (ciò è assicurato dalla chiamata a hasNext(), altrimenti
                // quest'ultimo ritornava FALSE e lanciavo l'eccezione all'inizio)
                // e non restituisco il prossimo elemento e mando avanti lastNode
                result = this.lastReturned.item;
                this.lastReturned = this.lastReturned.next;
            }
            return result;
        }

    }

    /*
     * Only for JUnit testing purposes.
     */
    protected Object[] getTable() {
        return this.table;
    }

    /*
     * Only for JUnit testing purposes.
     */
    protected PrimaryHashFunction getPhf() {
        return this.phf;
    }

}
