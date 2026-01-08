package it.unicam.cs.asdl2526.es13;

import java.util.ArrayList;
import java.util.List;

/**
 * Gli oggetti di questa classe sono calcolatori di cammini minimi con sorgente
 * singola su un certo graph orientato e pesato dato. Il graph su cui lavorare
 * deve essere passato quando l'oggetto calcolatore viene costruito e non può
 * contenere archi con pesi negativi. Il calcolatore implementa il classico
 * algoritmo di Dijkstra per i cammini minimi con sorgente singola utilizzando
 * una coda con priorità implementata con una semplice List (non è la soluzione
 * più efficiente, si potrebbe utilizzare uno heap e ottenere prestazioni logaritmiche
 * invece che lineari).
 * 
 * @author Luca Tesei (template)
 *
 * @param <L>
 *                il tipo delle etichette dei nodi del graph
 */
public class DijkstraShortestPathComputer<L>
        implements SingleSourceShortestPathComputer<L> {

    private GraphNode<L> lastSource;

    private final Graph<L> graph;

    private boolean isComputed = false;

    // Coda con priorità usata dall'algoritmo
    private List<GraphNode<L>> queue;

    /**
     * Crea un calcolatore di cammini minimi a sorgente singola per un graph
     * diretto e pesato privo di pesi negativi.
     * 
     * @param graph
     *                  il graph su cui opera il calcolatore di cammini minimi
     * @throws NullPointerException
     *                                      se il graph passato è nullo
     * 
     * @throws IllegalArgumentException
     *                                      se il graph passato è vuoto
     * 
     * @throws IllegalArgumentException
     *                                      se il graph passato non è orientato
     * 
     * @throws IllegalArgumentException
     *                                      se il graph passato non è pesato,
     *                                      cioè esiste almeno un arco il cui
     *                                      peso è {@code Double.NaN}
     * @throws IllegalArgumentException
     *                                      se il graph passato contiene almeno
     *                                      un peso negativo
     */
    public DijkstraShortestPathComputer(Graph<L> graph) {
        // TODO implementare
        this.graph = graph;
        this.queue = new ArrayList<GraphNode<L>>();
    }

    @Override
    public void computeShortestPathsFrom(GraphNode<L> sourceNode) {
        // TODO implementare
    }

    @Override
    public boolean isComputed() {
        return this.isComputed;
    }

    @Override
    public GraphNode<L> getLastSource() {
        // TODO implementare
        return this.lastSource;
    }

    @Override
    public Graph<L> getGraph() {
        return this.graph;
    }

    @Override
    public List<GraphEdge<L>> getShortestPathTo(GraphNode<L> targetNode) {
        // TODO implementare
        return null;
    }

    /*
     * Metodo inserito per scopi di test JUnit
     */
    //protected BinaryHeapMinPriorityQueue getQueue() {
    //    return this.queue;
    //}

}
