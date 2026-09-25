/**
 * 
 */
package it.unicam.cs.asdl2526.es9;

import java.util.List;
import java.util.ArrayList;

/**
 * Implementazione dell'algoritmo di Merge Sort integrata nel framework di
 * valutazione numerica. Non è richiesta l'implementazione in loco.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 */
public class MergeSort<E extends Comparable<E>> implements SortingAlgorithm<E> {

    // Definisco una variabile di istanza per salvare il numero di
    // confronti fatti, in quanto verrà usata in più metodi, non in uno solo.

    private int countCompare = 0;

    public SortingAlgorithmResult<E> sort(List<E> l) {
        if (l == null)
            throw new NullPointerException("Tentativo di ordinare una lista null");
        // Caso base
        if (l.size() <= 1)
            // Per ordinare la lista vuota o con un solo elemento non serve far niente
            return new SortingAlgorithmResult<E>(l, 0);
        // Altrimenti chiamo la procedura ricorsiva sull'intero array
        this.mergeSort(l, 0, l.size() - 1);
        return new SortingAlgorithmResult<>(l, this.countCompare);
    }

    private void mergeSort(List<E> l, int left, int right) {
        if(left < right) {
            // "L'elemento medio" nel caso in cui il numero degli
            // elementi è dispari, è scelto in modo che l'elemento
            // in più vada nella parte destra (arrotondamento per
            // difetto dovuto al CAST della divisione fra interi)
            int mid = (left + right) / 2;
            this.mergeSort(l, left, mid);
            this.mergeSort(l, mid + 1, right);
            this.merge(l, left, mid, right);
        }
        // nel caso in cui il sotto array contiene 1 solo elemento
        // (left == right) o è vuoto (left > right), non devo far nulla

    }

    private void merge(List<E> l, int left, int mid, int right) {
        // Anzitutto creo 2 liste accessorie per salvare gli
        // elementi dei due sotto array ordinati di cui fare il merge
        // (questo rende l'implementazione del merge sort NON in loco)
        int dimLeft = mid - left + 1;
        List<E> arrayLeft = new ArrayList<>();
        for(int i = left; i <= mid; i++)
            arrayLeft.add(l.get(i));
        int dimRight = right - mid;
        List<E> arrayRight = new ArrayList<>();
        for(int i = mid + 1; i <= right; i++)
            arrayRight.add(l.get(i));
        // Ora definisco 3 indici per fare il merge e ordinare l
        int i, j, k;
        // i scorre su arrayLeft da 0 (indica quale elemento
        // di arrayLeft siamo arrivati a confrontare)
        i = 0;
        // j scorre su arrayRight da 0 (indica quale elemento
        // di arrayRight siamo arrivati a confrontare)
        j = 0;
        // k scorre su l da LEFT (indica dove siamo arrivati
        // a sovrascrivere gli elementi in ordine su l)
        k = left;
        while(i < arrayLeft.size() && j < arrayRight.size()) {
            // Finché esiste ancora un elemento in entrambe le
            // sottoliste, faccio un confronto, quindi anzitutto
            // incremento la variabile d'istanza countCompare
            this.countCompare++;
            if(arrayLeft.get(i).compareTo(arrayRight.get(j)) <= 0) {
                // Metto in posizione k l'elemento del sotto array di
                // sinistra e incremento il rispettivo indice (i)
                l.set(k, arrayLeft.get(i));
                i++;
            }
            else { // arrayLeft.get(i).compareTo(arrayRight.get(j)) > 0
                // Metto in posizione k l'elemento del sotto array di
                // destra e incremento il rispettivo indice (j)
                l.set(k, arrayRight.get(j));
                j++;
            }
            // Indipendentemente da quale sotto array ho "pescato",
            // avendo aggiunto un elemento a l, devo incrementare k
            k++;
        }
        // Usciti dal while, significa che una delle due sottoliste è finita,
        // ma non è detto che lo sia anche l'altra: quindi controlliamo
        // quale delle due non è terminata (non lo sappiamo a priori) e
        // inseriamo tutti i suoi elementi che rimangono (già ordinati) in l
        while(i < arrayLeft.size()) {
            // Entriamo se e solo il sotto array di sx non è terminato
            l.set(k, arrayLeft.get(i));
            i++;
            k++;
        }
        while(j < arrayRight.size()) {
            // Entriamo se e solo se il sotto array di dx non è terminato
            l.set(k, arrayRight.get(j));
            j++;
            k++;
        }
    }

    public String getName() {
        return "MergeSort";
    }
}
