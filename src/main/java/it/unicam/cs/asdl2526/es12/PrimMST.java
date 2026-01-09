package it.unicam.cs.asdl2526.es12;

import java.util.ArrayList;

/**
 * Classe singoletto che implementa l'algoritmo di Prim per trovare un Minimum
 * Spanning Tree di un grafo non orientato, pesato e con pesi non negativi.
 * 
 * L'algoritmo richiede l'uso di una coda di min priorità tra i nodi che può
 * essere realizzata con una semplice ArrayList (non c'è bisogno di ottimizzare
 * le operazioni di inserimento, di estrazione del minimo, o di decremento della
 * priorità).
 * 
 * Si possono usare i colori dei nodi per registrare la scoperta e la visita
 * effettuata dei nodi.
 * 
 * @author Luca Tesei
 * 
 * @param <L>
 *                tipo delle etichette dei nodi del grafo
 *
 */
public class PrimMST<L> {

    /*
     * Coda di priorità che va usata dall'algoritmo. La variabile istanza è
     * protected solo per scopi di testing JUnit.
     */
    protected ArrayList<GraphNode<L>> queue;

    /**
     * Crea un nuovo algoritmo e inizializza la coda di priorità con una coda
     * vuota.
     */
    public PrimMST() {
        this.queue = new ArrayList<GraphNode<L>>();
    }

    /**
     * Utilizza l'algoritmo goloso di Prim per trovare un albero di copertura
     * minimo in un grafo non orientato e pesato, con pesi degli archi non
     * negativi. Dopo l'esecuzione del metodo nei nodi del grafo il campo
     * previous deve contenere un puntatore a un nodo in accordo all'albero di
     * copertura minimo calcolato, la cui radice è il nodo sorgente passato.
     * 
     * @param g
     *              un grafo non orientato, pesato, con pesi non negativi
     * @param s
     *              il nodo del grafo g sorgente, cioè da cui parte il calcolo
     *              dell'albero di copertura minimo. Tale nodo sarà la radice
     *              dell'albero di copertura trovato
     * 
     * @throw NullPointerException se il grafo g o il nodo sorgente s sono nulli
     * @throw IllegalArgumentException se il nodo sorgente s non esiste in g
     * @throw IllegalArgumentException se il grafo g è orientato, non pesato o
     *        con pesi negativi
     */
    public void computeMSP(Graph<L> g, GraphNode<L> s) {
            if (g == null || s == null)
                throw new NullPointerException("GRAFO e SORGENTE NON possono essere NULLI!");

            if (!g.getNodes().contains(s))
                throw new IllegalArgumentException("Nodo sorgente non presente nel grafo!");

            if (g.isDirected())
                throw new IllegalArgumentException("Il grafo NON può essere ORIENTATO!");

        // Controllo grafo pesato e pesi non negativi
        for (GraphNode<L> n : g.getNodes()) {
            for (GraphEdge<L> e : g.getEdgesOf(n)) {
                if (!e.hasWeight())
                    throw new IllegalArgumentException("Tutti gli ARCHI devono essere PESATI!");
                if (e.getWeight() < 0)
                    throw new IllegalArgumentException("Un ARCO NON può avere PESO NEGATIVO!");
            }
        }

            // Inizializzazione
            this.queue.clear();
            for (GraphNode<L> n : g.getNodes()) {
                n.setPrevious(null);
                n.setFloatingPointDistance(Double.POSITIVE_INFINITY);
                n.setColor(GraphNode.COLOR_WHITE);
                this.queue.add(n);
            }

            // Nodo sorgente
            s.setFloatingPointDistance(0.0);

            // Algoritmo di Prim
            while (!queue.isEmpty()) {
                // Estrazione del minimo (scansione lineare)
                GraphNode<L> u = queue.get(0);
                double min = Double.POSITIVE_INFINITY;

                for (GraphNode<L> n : queue) {
                    if (n.getFloatingPointDistance() < min) {
                        min = n.getFloatingPointDistance();
                        u = n;
                    }
                }

                queue.remove(u);
                u.setColor(GraphNode.COLOR_BLACK);

                // Rilassamento degli archi incidenti
                for (GraphEdge<L> e : g.getEdgesOf(u)) {
                    GraphNode<L> v =
                            e.getNode1().equals(u) ? e.getNode2() : e.getNode1();

                    if (queue.contains(v) && e.getWeight() < v.getFloatingPointDistance()) {
                        v.setFloatingPointDistance(e.getWeight());
                        v.setPrevious(u);
                    }
                }
            }
        }

}
