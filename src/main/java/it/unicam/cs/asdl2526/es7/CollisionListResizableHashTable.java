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

    /* Numero di elementi della tabella corrente */
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
            throw new IllegalArgumentException("La tabella NON contiene elementi NULLI!");
        // Salvo la posizione in cui cercare
        int index = this.phf.hash(o.hashCode(), this.getCurrentCapacity());
        // Se la posizione è vuota allora significa che non c'è
        if(this.table[index] == null)
            return false;
        // Altrimenti creo un iteratore per scorrere tutta la
        // lista a cui punta la posizione index
        Itr iterator = new Itr();
        // Cerco un elemento uguale a o finché ne esiste un successivo, cioè
        // finché iterator.hasNext() restituisce true (GUARDIA)
        while(iterator.hasNext()) {
            E current = iterator.next();
            if (o.equals(current))
                // se lo trovo restituisco TRUE
                return true;
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
            throw new IllegalArgumentException("La tabella NON accetta elementi NULLI!");
        // Salvo la posizione in cui devo aggiungere l'elemento
        int index = this.phf.hash(e.hashCode(),this.getCurrentCapacity());
        // Se già presente devo restituire FALSE
        if(isPresent(e))
            return false;
        // Altrimenti lo aggiungo e aggiorno size e modCount


        // Controllo resize
        if (this.size > this.getCurrentThreshold())
            resize();
        return true;
    }

    private boolean isPresent(E e) {
        Iterator<E> it = this.iterator();
        while(it.hasNext()) {
            E current = it.next();
            if(e.equals(current))
                return true;
        }
        return false;
    }

    /*
     * Raddoppia la tabella corrente e riposiziona tutti gli elementi. Da
     * chiamare quando this.size diventa maggiore di getCurrentThreshold()
     */
    private void resize() {
        // TODO implementare
    }

    @Override
    public boolean remove(Object o) {
        // TODO implementare
        /*
         * ATTENZIONE: usare l'hashCode dell'oggetto e la funzione di hash
         * primaria passata all'atto della creazione: il bucket in cui cercare
         * l'oggetto o è la posizione
         * this.phf.hash(o.hashCode(),this.getCurrentCapacity)
         * 
         * In questa posizione, se non vuota, si deve cercare l'elemento o
         * utilizzando il metodo equals() su tutti gli elementi della lista
         * concatenata lì presente. Se presente, l'elemento deve essere
         * eliminato dalla lista concatenata
         * 
         */
        // ATTENZIONE: la rimozione, in questa implementazione, **non** comporta
        // mai una resize "al ribasso", cioè un dimezzamento della tabella se si
        // scende sotto il fattore di bilanciamento desiderato.
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // TODO implementare
        // utilizzare un iteratore della collection e chiamare il metodo
        // contains
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        // TODO implementare
        // utilizzare un iteratore della collection e chiamare il metodo add
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // TODO implementare
        // utilizzare un iteratore della collection e chiamare il metodo remove
        return false;
    }

    @Override
    public void clear() {
        // Ritorno alla situazione iniziale
        this.table = new Object[INITIAL_CAPACITY];
        this.size = 0;
        this.modCount = 0;
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

        private int numeroModificheAtteso;

        private Itr() {
            // All'inizio non è ancora stato restituito niente
            this.lastReturned = null;
            this.currentIndex = 0;
            this.numeroModificheAtteso = modCount;
        }

        @Override
        public boolean hasNext() {
            if(currentIndex == CollisionListResizableHashTable.this.table.length)
                return false;
            // Ricerco la prima posizione esistente non null
            while(this.currentIndex < CollisionListResizableHashTable.this.table.length - 1
                    && CollisionListResizableHashTable.this.table[currentIndex] == null)
                this.currentIndex++;
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
            if(this.numeroModificheAtteso != CollisionListResizableHashTable.this.modCount)
                throw new ConcurrentModificationException("La lista è stata modificata durante l'iterazione");
            if(!hasNext())
                throw new NoSuchElementException("ELENCO TERMINATO!");
            // C'é sicuramente un successivo da tirar fuori, e la chiamata ad
            // hasNext avrà aggiornato currentIndex e lastReturned in modo tale
            // che indichino esattamente il primo elemento successivo non NULLO
            E result = null;
            if(this.lastReturned == null) {
                // sono all'inizio della lista corrente
                Node<E> head = (Node<E>) CollisionListResizableHashTable.this.table[currentIndex];
                this.lastReturned = head;
                result = head.item;
            }
            else {
                // sono nel mezzo di una lista di collisioni e lastNode.next non
                // è null (ciò è assicurato dalla chiamata a hasNext())
                // restituisco il prossimo elemento e mando avanti lastNode
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
