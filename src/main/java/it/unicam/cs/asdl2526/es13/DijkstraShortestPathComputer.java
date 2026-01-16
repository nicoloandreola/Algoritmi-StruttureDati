package it.unicam.cs.asdl2526.es13;

import java.util.ArrayList;
import java.util.Collections;
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
        if (graph == null)
            throw new NullPointerException("Il grafo non può essere null");
        if (graph.nodeCount() == 0)
            throw new IllegalArgumentException("Il grafo non può essere vuoto");
        if (!graph.isDirected())
            throw new IllegalArgumentException("Il grafo deve essere orientato");

        // Controllo pesi
        for (GraphEdge<L> e : graph.getEdges()) {
            if (Double.isNaN(e.getWeight()))
                throw new IllegalArgumentException("Il grafo non è pesato");
            if (e.getWeight() < 0)
                throw new IllegalArgumentException("Peso negativo non ammesso");
        }
        this.graph = graph;
        this.queue = new ArrayList<GraphNode<L>>();
    }

    @Override
    public void computeShortestPathsFrom(GraphNode<L> sourceNode) {
        if (sourceNode == null)
            throw new NullPointerException("Nodo sorgente null");
        if (!graph.getNodes().contains(sourceNode))
            throw new IllegalArgumentException("Nodo sorgente non nel grafo");

        // Inizializzazione
        for (GraphNode<L> node : graph.getNodes()) {
            node.setFloatingPointDistance(Double.POSITIVE_INFINITY);
            node.setPrevious(null);
            node.setColor(GraphNode.COLOR_WHITE);
        }

        sourceNode.setFloatingPointDistance(0.0);

        queue.clear();
        queue.addAll(graph.getNodes());

        // Algoritmo di Dijkstra
        while (!queue.isEmpty()) {
            GraphNode<L> u = extractMin();
            u.setColor(GraphNode.COLOR_BLACK);

            for (GraphEdge<L> edge : graph.getEdgesOf(u)) {
                GraphNode<L> v = edge.getNode2();
                if (v.getColor() == GraphNode.COLOR_BLACK)
                    continue;

                double alt = u.getFloatingPointDistance() + edge.getWeight();
                if (alt < v.getFloatingPointDistance()) {
                    v.setFloatingPointDistance(alt);
                    v.setPrevious(u);
                }
            }
        }

        this.lastSource = sourceNode;
        this.isComputed = true;
    }

    // Estrae il nodo con distanza minima
    private GraphNode<L> extractMin() {
        GraphNode<L> min = null;
        double minDist = Double.POSITIVE_INFINITY;

        for (GraphNode<L> n : queue) {
            if (n.getFloatingPointDistance() < minDist) {
                minDist = n.getFloatingPointDistance();
                min = n;
            }
        }

        queue.remove(min);
        return min;
    }

    @Override
    public boolean isComputed() {
        return this.isComputed;
    }

    @Override
    public GraphNode<L> getLastSource() {
        if (!isComputed)
            throw new IllegalStateException("Calcolo non ancora effettuato");
        return this.lastSource;
    }

    @Override
    public Graph<L> getGraph() {
        return this.graph;
    }

    @Override
    public List<GraphEdge<L>> getShortestPathTo(GraphNode<L> targetNode) {
        if (targetNode == null)
            throw new NullPointerException("Nodo target null");
        if (!graph.getNodes().contains(targetNode))
            throw new IllegalArgumentException("Nodo target non nel grafo");
        if (!isComputed)
            throw new IllegalStateException("Calcolo non ancora effettuato");

        // Nodo non raggiungibile
        if (targetNode.getFloatingPointDistance() == Double.POSITIVE_INFINITY)
            return null;

        // Caso sorgente = target
        if (targetNode.equals(lastSource))
            return new ArrayList<>();

        List<GraphEdge<L>> path = new ArrayList<>();
        GraphNode<L> current = targetNode;

        while (!current.equals(lastSource)) {
            GraphNode<L> prev = current.getPrevious();
            GraphEdge<L> edge = graph.getEdge(prev, current);
            path.add(edge);
            current = prev;
        }

        Collections.reverse(path);
        return path;
    }

    /*
     * Metodo inserito per scopi di test JUnit
     */
    //protected BinaryHeapMinPriorityQueue getQueue() {
    //    return this.queue;
    //}

}
