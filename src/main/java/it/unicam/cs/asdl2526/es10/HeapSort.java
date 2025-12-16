/**
 * 
 */
package it.unicam.cs.asdl2526.es10;

import java.util.List;

// TODO completare import 

/**
 * Classe che implementa un algoritmo di ordinamento basato su heap.
 * 
 * @author Template: Luca Tesei, Implementation: collettiva
 *
 */
public class HeapSort<E extends Comparable<E>> implements SortingAlgorithm<E> {

    // Definisco una variabile di istanza per salvare il numero di
    // confronti fatti, in quanto verrà usata in più metodi, non in uno solo.

    private int countCompare = 0;

    // Ne definisco una anche per la size dell'array poiché il metodo
    // heapify dovrà utilizzare l.size solo nel ciclo iniziale, poi
    // l.size - 1, l.size - 2 e così via...(variabile va inizializzata
    // per forza dentro sort perché qua non conosciamo ancora l)

    private int size;

    @Override
    public SortingAlgorithmResult<E> sort(List<E> l) {
        // Nota: usare una variante dei metodi della classe MaxHeap in
        // modo da implementare l'algoritmo utilizzando solo un array
        // (arraylist) e alcune variabili locali di appoggio
        // (implementazione cosiddetta "in loco" o "in place", si veda
        // https://it.wikipedia.org/wiki/Algoritmo_in_loco)
        if (l == null)
            throw new NullPointerException("Tentativo di ordinare una lista null");
        // Caso base
        if (l.size() <= 1)
            // Per ordinare la lista vuota o con un solo elemento non faccio niente
            return new SortingAlgorithmResult<E>(l, 0);
        // Inizializziamo con la lunghezza della lista passata
        this.size = l.size();
        // Altrimenti devo rendere la lista passata uno heap (metodo MaxHeap)
        for(int i = (this.size / 2) -1; i >= 0; i--)
            this.heapify(l, i);
        // Ora che ho un heap, so che l'elemento più grande si trova nella prima
        // posizione, quindi scambio l’ultimo col primo (in modo che questo sia sistemato),
        // decremento la size e chiamo heapify per ricostruire lo heap da 0 a this.size--.
        // Vado avanti finché non è rimasto un solo elemento che è quindi il più piccolo
        for(int i = this.size - 1; i > 0; i--) {
            // Scambio
            E max = l.get(0);
            l.set(0, l.get(i));
            l.set(i, max);
            // Prima di chiamare heapify decremento la size
            this.size--;
            // In questo caso devo risistemare solo il primo
            // elemento quindi ad heapify passo 0 come indice
            this.heapify(l, 0);
        }
        return new SortingAlgorithmResult<>(l, this.countCompare);
    }

    private void heapify(List<E> l, int i) {
        int left = this.leftIndex(i);
        int right = this.rightIndex(i);
        int max = i;
        if(left < this.size && l.get(left).compareTo(l.get(max)) > 0) {
            max = left;
            this.countCompare++;
        }
        if(right < this.size && l.get(right).compareTo(l.get(max)) > 0) {
            max = right;
            this.countCompare++;
        }
        if(max != i) {
            E temp = l.get(max);
            l.set(max, l.get(i));
            l.set(i, temp);
            this.heapify(l, max);
        }
    }

    /*
     * Funzione di comodo per calcolare l'indice del figlio sinistro del nodo in
     * posizione i. Si noti che la posizione 0 è significativa e contiene sempre
     * la radice dello heap.
     */
    private int leftIndex(int i) {
        return i * 2 + 1;
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

    @Override
    public String getName() {
        return "HeapSort";
    }

}
