package it.unicam.cs.asdl2526.es6;

import java.util.*;

/**
 * Lista concatenata singola che non accetta valori null, ma permette elementi
 * duplicati. Le seguenti operazioni non sono supportate:
 * 
 * <ul>
 * <li>ListIterator<E> listIterator()</li>
 * <li>ListIterator<E> listIterator(int index)</li>
 * <li>List<E> subList(int fromIndex, int toIndex)</li>
 * <li>T[] toArray(T[] a)</li>
 * <li>boolean containsAll(Collection<?> c)</li>
 * <li>addAll(Collection<? extends E> c)</li>
 * <li>boolean addAll(int index, Collection<? extends E> c)</li>
 * <li>boolean removeAll(Collection<?> c)</li>
 * <li>boolean retainAll(Collection<?> c)</li>
 * </ul>
 * 
 * L'iteratore restituito dal metodo {@code Iterator<E> iterator()} è fail-fast,
 * cioè se c'è una modifica strutturale alla lista durante l'uso dell'iteratore
 * allora lancia una {@code ConcurrentModificationException} appena possibile,
 * cioè alla prima chiamata del metodo {@code next()}.
 * 
 * @author Luca Tesei
 *
 * @param <E>
 *                il tipo degli elementi della lista
 */
public class SingleLinkedList<E> implements List<E> {

    private int size;

    private Node<E> head;

    private Node<E> tail;

    private int numeroModifiche; // modCount che serve per l'itearatore

    /**
     * Crea una lista vuota.
     */
    public SingleLinkedList() {
        this.size = 0;
        this.head = null;
        this.tail = null;
        this.numeroModifiche = 0;
    }

    /*
     * Classe per i nodi della lista concatenata. È dichiarata static perché
     * gli oggetti della classe Node<E> non hanno bisogno di accedere ai campi
     * della classe principale per funzionare.
     */
    private static class Node<E> {
        private E item;

        private Node<E> next;

        /*
         * Crea un nodo "singolo" equivalente a una lista con un solo elemento.
         */
        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }

    }

    /*
     * Classe che realizza un iteratore per SingleLinkedList.
     * L'iteratore deve essere fail-fast, cioè deve lanciare una eccezione
     * ConcurrentModificationException se a una chiamata di next() si "accorge"
     * che la lista è stata cambiata rispetto a quando l'iteratore è stato
     * creato.
     * 
     * La classe è non-static perché l'oggetto iteratore, per funzionare
     * correttamente, ha bisogno di accedere ai campi dell'oggetto della classe
     * principale presso cui è stato creato.
     */
    private class Itr implements Iterator<E> {

        private Node<E> lastReturned;

        private int numeroModificheAtteso;

        private Itr() {
            // All'inizio non è stato fatto nessun next
            this.lastReturned = null;
            this.numeroModificheAtteso = SingleLinkedList.this.numeroModifiche;
        }

        @Override
        public boolean hasNext() {
            if (this.lastReturned == null)
                // sono all'inizio dell'iterazione
                return SingleLinkedList.this.head != null;
            else
                // almeno un next è stato fatto
                return lastReturned.next != null;

        }

        @Override
        public E next() {
            // controllo concorrenza
            if (this.numeroModificheAtteso != SingleLinkedList.this.numeroModifiche) {
                throw new ConcurrentModificationException(
                        "Lista modificata durante l'iterazione");
            }
            // controllo hasNext()
            if (!hasNext())
                throw new NoSuchElementException(
                        "Richiesta di next quando hasNext è falso");
            // c'è sicuramente un elemento di cui fare next
            // aggiorno lastReturned e restituisco l'elemento next
            if (this.lastReturned == null) {
                // sono all’inizio e la lista non è vuota
                this.lastReturned = SingleLinkedList.this.head;
                return SingleLinkedList.this.head.item;
            } else {
                // non sono all’inizio, ma c’è ancora qualcuno
                lastReturned = lastReturned.next;
                return lastReturned.item;
            }

        }

    }

    /*
     * Una lista concatenata è uguale a un'altra lista se questa è una lista
     * concatenata e contiene gli stessi elementi nello stesso ordine.
     * 
     * Si noti che si poteva anche ridefinire il metodo equals in modo da
     * accettare qualsiasi oggetto che implementi List<E> senza richiedere che
     * sia un oggetto di questa classe, visto che potrebbe esistere un'altra
     * Lista, non di tipo SingleLinkedList, uguale a una di tipo SingleLinkedList:
     * 
     * obj instanceof List
     * 
     * In quel caso si può fare il cast a List<?>:
     * 
     * List<?> other = (List<?>) obj;
     * 
     * e usando l'iteratore si possono tranquillamente controllare tutti gli
     * elementi (come è stato fatto anche qui):
     * 
     * Iterator<E> thisIterator = this.iterator();
     * 
     * Iterator<?> otherIterator = other.iterator();
     * 
     * ...
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (!(obj instanceof SingleLinkedList))
            return false;
        SingleLinkedList<?> other = (SingleLinkedList<?>) obj;
        // Controllo se entrambe liste vuote
        if (head == null) {
            if (other.head != null)
                return false;
            else
                return true;
        }
        // Liste non vuote, scorro gli elementi di entrambe
        Iterator<E> thisIterator = this.iterator();
        Iterator<?> otherIterator = other.iterator();
        while (thisIterator.hasNext() && otherIterator.hasNext()) {
            E o1 = thisIterator.next();
            // uso il polimorfismo di Object perché non conosco il tipo ?
            Object o2 = otherIterator.next();
            // il metodo equals che si usa è quello della classe E
            if (!o1.equals(o2))
                return false;
        }
        // Controllo che entrambe le liste siano terminate
        return !(thisIterator.hasNext() || otherIterator.hasNext());
    }

    /*
     * L'hashcode è calcolato usando gli hashcode di tutti gli elementi della
     * lista.
     */
    @Override
    public int hashCode() {
        int hashCode = 1;
        // implicitamente, col for-each, uso l'iteratore di questa classe
        for (E e : this)
            hashCode = 31 * hashCode + e.hashCode();
        return hashCode;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * Returns true if this list contains the specified element
     *
     * @param o element whose presence in this list is to be tested
     *
     * @return true if this list contains the specified element
     */

    @Override
    public boolean contains(Object o) {
        if(o == null)
            throw new NullPointerException("L'elemento da cercare NON può essere NULL!");
        // Creo un iteratore per scorrere tutta la lista
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

    /**
     * Appends the specified element to the end of this list
     *
     * @param e - element to be appended to this list
     *
     * @return true (as specified by List.add(E))
     */

    @Override
    public boolean add(E e) {
        if(e == null)
            throw new NullPointerException("L'elemento da aggiungere NON può essere NULL!");
        // Creo il nodo per il nuovo elemento, passandogli come
        // argomento per il campo next NULL, in quanto verrà inserito
        // in fondo e quindi non avrà nessun elemento dopo di lui
        Node<E> nuovo = new Node<>(e, null);
        // se la lista è vuota significa che sto inserendo il primo elemento,
        // quindi devo aggiornare anche this.head (non devo invece aggiornare
        // campo next in this.tail perché non c'è nessun elemento dopo)
        if(this.isEmpty())
            this.head = nuovo;
        // se invece la lista non è vuota aggiorno il riferimento dell'ultimo
        // elemento, che ora non è più l'ultimo (viceversa di prima)
        else
            this.tail.next = nuovo;
        // Indipendentemente se la lista è vuota o meno, devo aggiornare
        // this.tail (se aggiungo, aggiungo sempre in fondo quindi l'ultimo
        // elemento va aggiornato con il nuovo), size e il numero di modifiche
        this.tail = nuovo;
        this.size++;
        this.numeroModifiche++;
        return true;
    }

    /**
     * Removes the first occurrence of the specified element from this list, if
     * it is present. If this list does not contain the element, it is unchanged
     *
     * @param o element to be removed from this list, if present
     *
     * @return true if this list contained the specified element, or
     *         equivalently, if this list changed as a result of the call
     */
    
    @Override
    public boolean remove(Object o) {
        if(o == null)
            throw new NullPointerException("L'elemento da rimuovere NON può essere NULL!");
        // Salvo l'indice dell'oggetto da rimuovere
        int index = this.indexOf(o);
        // Se il metodo ritorna -1 significa che l'oggetto non è
        // presente nella collection quindi non può essere rimosso
        if(index == -1)
            return false;
        // Poiché index contiene l'indice della prima occorrenza, chiamando
        // il metodo remove con indice e passandogli index, verrà
        // automaticamente rimossa la prima occorrenza dell'oggetto o
        this.remove(index);
        return true;

    }

    /**
     * Removes all the elements from this list
     */

    @Override
    public void clear() {
        // Per scollegare tutti gli elementi basta mettere la testa a null: così
        // facendo infatti, la variabile head non punta più al primo nodo della lista,
        // quindi automaticamente tutta la catena di nodi diventa inaccessibile (primo
        // nodo inaccessibile ma primo nodo conteneva riferimento al secondo quindi
        // anche secondo è diventato inaccessibile e così via...). L'unica altra cosa
        // che va fatta é mettere a null anche tail in quanto campo della classe e
        // quindi accessibile anche al di fuori del riferimento del penultimo elemento
        this.head = null;
        this.tail = null;
        this.size = 0;
        this.numeroModifiche++;
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index of the element to return
     *
     * @return the element at the specified position in this list
     */

    @Override
    public E get(int index) {
        if(index < 0 || index >= this.size)
            throw new IndexOutOfBoundsException("Indice NON valido!");
        // Creo un nodo per scorrere tutti gli elementi della
        // collection e lo inizializzo con il primo (this.head)
        Node<E> current = this.head;
        // Arrivo alla posizione desiderata (index)
        for(int i = 0; i < index; i++)
            current = current.next;
        // Usciti dal ciclo, current punta al nodo di indice index
        return current.item;
    }

    /**
     * Replaces the element at the specified position in this
     * list with the specified element
     *
     * @param index - index of the element to replace
     *
     * @param element - element to be stored at the specified position
     *
     * @return the element previously at the specified position
     */

    @Override
    public E set(int index, E element) {
        if(index < 0 || index >= this.size)
            throw new IndexOutOfBoundsException("Indice NON valido!");
        if(element == null)
            throw new NullPointerException("L'elemento da aggiungere NON può essere NULL!");
        // Creo un nodo per scorrere tutti gli elementi della collection e
        // lo inizializzo con il primo (this.head). Non ho bisogno di una
        // seconda variabile locale in cui salvare il precedente poiché non
        // devo scollegare nulla, ma devo semplicemente aggiornare
        Node<E> current = this.head;
        // Arrivo alla posizione in cui devo inserire l'elemento (index)
        for(int i = 0; i < index; i++)
            current = current.next;
        // Usciti dal ciclo, current punta al nodo di indice index, quindi,
        // per restituire "the element previously at the specified position",
        // prima di aggiornare current.item con l'elemento passato, lo salvo
        // in una variabile che poi restituirò (non serve lavorare sui campi
        // .next in quanto non sto aggiungendo nessun nodo ma solo cambiando
        // il suo valore, quindi i riferimenti restano gli stessi)
        E oldElement = current.item;
        current.item = element;
        return oldElement;
    }

    /**
     * Inserts the specified element at the specified position in this list.
     * Shifts the element currently at that position (if any) and any
     * subsequent elements to the right (adds one to their indices).
     *
     * @param index - index at which the specified element is to be inserted
     *
     * @param element - element to be inserted
     *
     * @throws IndexOutOfBoundsException - if the index is out of range
     */

    @Override
    public void add(int index, E element) {
        if(index < 0 || index > this.size)
            throw new IndexOutOfBoundsException("Indice NON valido!");
        if(element == null)
            throw new NullPointerException("L'elemento da aggiungere NON può essere NULL!");
        // Per scorrere la lista fino alla posizione desiderata, stavolta definisco 2
        // variabili locali in quanto devo tenere traccia non solo dell'elemento corrente,
        // ma anche di quello precedente poiché il nuovo elemento dovrà seguire quest'ultimo
        // (precedente.next = nuovo) e precedere quello corrente (nuovo.next = current)
        Node<E> current = this.head;
        Node<E> precedente = null;
        // Arrivo alla posizione desiderata (index)
        for(int i = 0; i < index; i++) {
            precedente = current;
            current = current.next;
        }
        // A questo punto current punta al nodo in posizione index: creo
        // il nuovo nodo da aggiungere, passandogli come next il riferimento
        // a quello presente ora in posizione index (cioè current), per poi
        // shiftare tutti di una posizione a destra (operazione che costa
        // O(1), VANTAGGIO LISTA CONCATENATA)
        Node<E> nuovo = new Node<>(element, current);
        if(precedente == null)
            // Sto inserendo all'inizio della lista (basta aggiornare head)
            this.head = nuovo;
        else {
            // Sto inserendo in mezzo alla lista (basta aggiornare il
            // campo next del precedente inserendoci il nuovo)
            precedente.next = nuovo;
            if (index == this.size)
                // Sto inserendo alla fine della lista quindi devo
                // anche aggiornare l'ultimo elemento (tail)
                this.tail = nuovo;
        }
        this.size++;
        this.numeroModifiche++;


    }

    /**
     * Removes the element at the specified position in this list
     * and shifts any subsequent elements to the left
     *
     * @param index the index of the element to be removed
     *
     * @return the element previously at the specified position
     *         (the element that was removed from the list)
     *
     * @throws IndexOutOfBoundsException if the index is out of range
     */

    @Override
    public E remove(int index) {
        if(index < 0 || index >= this.size)
            throw new IndexOutOfBoundsException("Indice NON valido!");
        // Anche in questo caso, per scorrere la lista fino alla posizione desiderata,
        // definisco 2 variabili locali, una per l'elemento corrente, e una per
        // quello precedente: stavolta però dovrò cancellare il primo e poi collegare
        // il secondo con il successivo del primo (cioè con current.next)
        Node<E> current = this.head;
        Node<E> precedente = null;
        // Arrivo alla posizione desiderata (index)
        for(int i = 0; i < index; i++) {
            precedente = current;
            current = current.next;
        }
        // Usciti dal ciclo, current punta al nodo di indice
        // index e precedente al nodo precedente
        if(current == this.head) {
            // Sto eliminando il primo elemento della lista
            if (current.next == null) {
                // Ma se il primo elemento non ha successivo, significa che la lista
                // contiene solo l'elemento che voglio rimuovere, quindi si svuota
                this.head = null;
                this.tail = null;
            } else // Altrimenti, se esiste almeno un successivo, lo shifto a sx:
                // basta shiftare quello immediatamente successivo, che si shiftano
                // automaticamente anche tutti gli altri (VANTAGGIO LISTA CONCATENATA)
                this.head = current.next;
        }
        else {
            if(current.next == null) {
                // Sto eliminando l'ultimo elemento della lista
                this.tail = precedente;
                precedente.next = null;
            }
            else // Sto eliminando un elemento in mezzo alla lista
                precedente.next = current.next;
        }
        this.size--;
        this.numeroModifiche++;
        return current.item;
    }

    /**
     * Returns the index of the first occurrence of the specified element in
     * this list, or -1 if this list does not contain the element.
     *
     * @param o element to search for
     *
     * @return the index of the first occurrence of the specified element in
     *         this list, or -1 if this list does not contain the element
     */

    @Override
    public int indexOf(Object o) {
        if(o == null)
            throw new NullPointerException("L'elemento da ricercare NON può essere NULL!");
        int index = 0; // contatore
        // Creo un iteratore per scorrere tutti gli elementi della collection
        Itr iterator = new Itr();
        // Scorro la collection fino in fondo con la GUARDIA iterator.hasNext()
        while(iterator.hasNext()) {
            E current = iterator.next();
            if(o.equals(current))
                // Se trovo un elemento uguale a o, ritorno direttamente index
                // (l'indice del nodo n è index) visto che è richiesta la prima occorrenza
                return index;
            else
                // Altrimenti incremento il contatore
                index++;
            }
            // Collection terminata ed elemento non trovato
        return -1;
    }

    /**
     * Returns the index of the last occurrence of the specified element in
     * this list, or -1 if this list does not contain the element.
     *
     * @param o element to search for
     *
     * @return the index of the last occurrence of the specified element in
     * this list, or -1 if this list does not contain the element
     */

    @Override
    public int lastIndexOf(Object o) {
        if(o == null)
            throw new NullPointerException("L'elemento da ricercare NON può essere NULL!");
        // variabile che aggiorno ogni volta che trovo un’occorrenza dell’oggetto
        int lastIndex = -1;
        // Creo un iteratore per scorrere tutti gli elementi della collection
        Itr iterator = new Itr();
        // poi definisco una variabile che rappresenta l'indice del nodo current
        // con cui aggiornerò la variabile che devo restituire: per farlo, non posso usare
        // il metodo indexOf(o) poiché restituisce sempre e solo la PRIMA OCCORRENZA
        int index = 0;
        while(iterator.hasNext()) {
            E current = iterator.next();
            if (o.equals(current))
                lastIndex = index;
            // L'incremento di index non lo metto dentro un else poiché
            // va fatto anche quando l'oggetto è uguale, altrimenti resto bloccato
            index++;
        }
        return lastIndex;
    }

    /**
     * Returns an array containing all the elements in this
     * list in proper sequence (from first to last element).
     *
     * @return a new array containing all the elements in this
     *          list in proper sequence
     */

    @Override
    public Object[] toArray() {
        Object[] result = new Object[this.size];
        for(int i = 0; i < result.length; i++) {
            result[i] = this.get(i);
        }
        return result;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }
}
