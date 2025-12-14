/**
 * 
 */
package it.unicam.cs.asdl2526.es9;

import java.util.List;
import java.util.Random;

/**
 * Implementazione del QuickSort con scelta della posizione del pivot scelta
 * randomicamente tra le disponibili. L'implementazione è in loco.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 * @param <E>
 *                il tipo degli elementi della sequenza da ordinare.
 *
 */
public class QuickSortRandom<E extends Comparable<E>>
        implements SortingAlgorithm<E> {

    private static final Random randomGenerator = new Random();

    // Definisco una variabile di istanza per salvare il numero di
    // confronti fatti, in quanto verrà usata in più metodi, non in uno solo

    private int countCompare = 0;

    @Override
    public SortingAlgorithmResult<E> sort(List<E> l) {
        if(l == null)
            throw new NullPointerException("Tentativo di ordinare una lista NULL");
        if(l.size() <= 1)
            // Per ordinare la lista vuota o con un solo elemento non serve far niente
            return new SortingAlgorithmResult<>(l, 0);
        // Altrimenti chiamo la procedura ricorsiva sull'intero array
        this.quickSortRandom(l, 0, l.size() - 1);
        return new SortingAlgorithmResult<>(l, this.countCompare);
    }

    private void quickSortRandom(List<E> l, int p, int r) {
        if (p < r) {
            // la porzione di array contiene almeno due elementi,
            // quindi devo partizionare (q è la posizione del pivot)
            int q = this.randomPartition(l, p, r);
            // Chiamata ricorsiva sulla partizione [p,q-1]
            this.quickSortRandom(l, p, q - 1);
            // Chiamata ricorsiva sulla partizione [q+1,r]
            this.quickSortRandom(l, q + 1, r);
            // Elemento q non è compreso nelle 2 sotto sequenze poiché
            // pivot non va riposizionato, è già nel posto giusto
        }
        // nel caso in cui la porzione di array contiene 1 solo elemento
        // (p == r) o è vuota (p > r), non devo far nulla
    }

    private int randomPartition(List<E> l, int p, int r) {
        // Consideriamo come pivot un elemento a caso tra le posizioni p ed r
        // (utilizzo istanza della classe random definita come campo della classe)
        int shift = randomGenerator.nextInt(r - p + 1);
        // La posizione random scelta è p + shift che corrisponde a una
        // posizione a caso tra p ed r inclusi (+1 serve proprio per
        // includere anche r, che altrimenti sarebbe escluso come da API).
        // Shift rappresenta di per sé l'intervallo che va da 0 alla
        // lunghezza della partizione (r - p) + 1, non quello da p ad r:
        // per questo considero come posizione scelta p + shift
        if (p + shift != r) {
            // scambio l'elemento in posizione p + shift con l'elemento in posizione
            // r, in modo che diventi lui il pivot (a meno che p + shift non sia già r)
            E temp = l.get(p + shift);
            l.set(p + shift, l.get(r));
            l.set(r, temp);
        }
        // Chiamo la partizione normale
        return this.partition(l, p, r);
    }

    private int partition(List<E> l, int p, int r) {
        // Salviamo l'elemento pivot che inizialmente è l'ultimo, cioè
        // quello in posizione r (variabile non strettamente necessaria)
        E pivot = l.get(r);
        // Inizializziamo l'indice i
        int i = p - 1;
        for (int j = p; j <= r - 1; j++) {
            // effettuo un confronto tra il pivot e l'elemento in posizione j
            this.countCompare++;
            if (l.get(j).compareTo(pivot) <= 0) {
                // l'elemento in posizione j è minore o uguale del pivot
                i = i + 1;
                // scambio l'elemento in posizione i con quello in posizione j
                E temp = l.get(i);
                l.set(i, l.get(j));
                l.set(j, temp);
            }
        }
        // Scambio l'elemento pivot (in posizione r) con l'elemento in posizione
        // i + 1, che è il primo degli elementi maggiori del pivot
        E temp = l.get(r);
        l.set(r, l.get(i + 1));
        l.set(i + 1, temp);
        // Ritorno la nuova posizione del pivot
        return i + 1;
    }

    @Override
    public String getName() {
        return "QuickSortRandom";
    }

}
