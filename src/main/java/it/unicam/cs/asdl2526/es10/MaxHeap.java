package it.unicam.cs.asdl2526.es10;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Classe che implementa uno heap binario che può contenere elementi non nulli
 * possibilmente ripetuti.
 * 
 * @author Template: Luca Tesei, Implementation: collettiva
 *
 * @param <E>
 *                il tipo degli elementi dello heap, che devono avere un
 *                ordinamento naturale.
 */
public class MaxHeap<E extends Comparable<E>> {

    /*
     * L'array che serve come base per lo heap
     */
    private ArrayList<E> heap;

    /**
     * Costruisce uno heap vuoto.
     */
    public MaxHeap() {
        this.heap = new ArrayList<E>();
    }

    /**
     * Restituisce il numero di elementi nello heap.
     * 
     * @return il numero di elementi nello heap
     */
    public int size() {
        return this.heap.size();
    }

    /**
     * Determina se lo heap è vuoto.
     * 
     * @return true se lo heap è vuoto.
     */
    public boolean isEmpty() {
        return this.heap.isEmpty();
    }

    /**
     * Costruisce uno heap a partire da una lista di elementi.
     * 
     * @param list
     *                 lista di elementi
     * @throws NullPointerException
     *                                  se la lista è nulla
     */
    public MaxHeap(List<E> list) {
        if(list == null)
            throw new NullPointerException("La LISTA di partenza NON può essere NULLA");
        // Determino la posizione del primo nodo che non è
        // una foglia che ha almeno un figlio
        int index = (this.size() + 1) / 2;
        // Chiamo heapify su tutti i nodi a partire da quello appena
        // determinato fino ad arrivare alla radice (decremento
        // e non incremento di uno l'indice a ogni iterazione)
        while(index >= 0) {
            this.heapify(index);
            index--;
        }
    }

    /**
     * Inserisce un elemento nello heap
     * 
     * @param el
     *               l'elemento da inserire
     * @throws NullPointerException
     *                                  se l'elemento è null
     * 
     */
    public void insert(E el) {
        if(el == null)
            throw new NullPointerException("Lo HEAP non accetta elementi NULLI");
        // Anzitutto inserisco il nuovo elemento nella prima posizione libera
        // dell’array con il metodo add che lo inserisce in fondo automaticamente
        this.heap.add(el);
        // Mi salvo il valore e l'indice dell'elemento appena aggiunto
        // in 2 variabili locali per rendere più chiaro il ciclo
        int index = this.size() - 1;
        E nuovo = this.heap.get(index);
        // Dopodiché sistemo l'albero risalendo verso l'alto finché non trovo
        // un parent maggiore del nuovo elemento o non arrivo alla radice
        while(index > 0 && nuovo.compareTo(this.heap.get(parentIndex(index))) > 0) {
            // scambio i valori dei 2 nodi (non serve definire una variabile
            // di appoggio in quanto posso usare quella già definita)
            this.heap.set(index, this.heap.get(this.parentIndex(index)));
            this.heap.set(parentIndex(index), nuovo);
            // poi passo a sistemare il pezzo successivo
            index = this.parentIndex(index);
        }
    }

    /*
     * Funzione di comodo per calcolare l'indice del figlio sinistro del nodo in
     * posizione i. Si noti che la posizione 0 è significativa e contiene sempre
     * la radice dello heap.
     */
    private int leftIndex(int i) {
        return i * 2 + 2;
    }

    /*
     * Funzione di comodo per calcolare l'indice del figlio destro del nodo in
     * posizione i. Si noti che la posizione 0 è significativa e contiene sempre
     * la radice dello heap.
     */
    private int rightIndex(int i) {
        return i * 2 + 2;
    }

    /*
     * Funzione di comodo per calcolare l'indice del genitore del nodo in
     * posizione i. Si noti che la posizione 0 è significativa e contiene sempre
     * la radice dello heap.
     */
    private int parentIndex(int i) {
        return (i - 1) / 2;
    }

    /**
     * Ritorna l'elemento massimo senza toglierlo.
     * 
     * @return l'elemento massimo dello heap oppure null se lo heap è vuoto
     */
    public E getMax() {
        if (this.isEmpty())
            return null;
        return this.heap.get(0);
    }

    /**
     * Estrae l'elemento massimo dallo heap. Dopo la chiamata tale elemento non
     * è più presente nello heap.
     * 
     * @return l'elemento massimo di questo heap oppure null se lo heap è vuoto
     */
    public E extractMax() {
        if(this.isEmpty())
            return null;
        // Salviamo il massimo da restituire, ovvero quello
        // che si trova in posizione zero e poi determiniamo
        // quello nuovo e risistemiamo tutto l'albero
        E result = this.heap.get(0);
        // Se conteneva solo 1 elemento allora basta chiamare
        // il metodo clear della classe ArrayList
        if(this.size() == 1)
            this.heap.clear();
        // Altrimenti sposto l'ultima foglia nella radice, rimuovo
        // la prima e chiamo heapify sulla seconda per sistemare l'albero
        else {
            this.heap.set(0, this.heap.get(this.size() - 1));
            this.heap.remove(this.size() - 1);
            this.heapify(0);
        }
        return result;
    }

    /*
     * Ricostituisce uno heap a partire dal nodo in posizione i assumendo che i
     * suoi sotto alberi sinistro e destro (se esistono) siano heap.
     */
    private void heapify(int i) {
        // Salvo gli indici dei 2 figli
        int left = this.leftIndex(i);
        int right = this.rightIndex(i);
        // Inizializzo la variabile MAX con i
        int max = i;
        if(left <= this.size() && this.heap.get(left).compareTo(this.heap.get(max)) > 0)
            // Se il figlio sx esiste ed è maggiore di max (cioè di i),
            // aggiorno la variabile max con l'indice del figlio sx
            max = left;
        if(right <= this.size() && this.heap.get(right).compareTo(this.heap.get(max)) > 0)
            // Se il figlio dx esiste ed è maggiore del valore attuale
            // contenuto in max (i o left), aggiorno quest'ultima con right
            max = right;
        // Se il massimo tra i 3 non è il nodo i devo scambiare i con max
        // e richiamare il metodo ricorsivamente sull'indice del nodo figlio
        // uguale a max che dopo lo scambio conterrà lo stesso valore che
        // conteneva il nodo i all'inizio di questa chiamata
        if(max != i) {
            E temp = this.heap.get(max);
            this.heap.set(max, this.heap.get(i));
            this.heap.set(i, temp);
            heapify(max);
        }
        // Se invece il massimo è proprio i, significa che sono
        // arrivato alla posizione giusta quindi non devo fare
        // nulla (né scambi né chiamate ricorsive)
    }
    
    /**
     * Only for JUnit testing purposes.
     * 
     * @return the arraylist representing this max heap
     */
    protected ArrayList<E> getHeap() {
        return this.heap;
    }
}
