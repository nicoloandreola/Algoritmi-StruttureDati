/**
 * 
 */
package it.unicam.cs.asdl2526.es12;

import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

/**
 * Implementazione della classe astratta {@code Graph<L>} che realizza un grafo
 * non orientato. Per la rappresentazione viene usata una variante della
 * rappresentazione a liste di adiacenza. A differenza della rappresentazione
 * standard si usano strutture dati più efficienti per quanto riguarda la
 * complessità in tempo della ricerca se un nodo è presente (pseudocostante, con
 * tabella hash) e se un arco è presente (pseudocostante, con tabella hash). Lo
 * spazio occupato per la rappresentazione risultà tuttavia più grande di quello
 * che servirebbe con la rappresentazione standard.
 * 
 * Le liste di adiacenza sono rappresentate con una mappa (implementata con
 * tabelle hash) che associa ad ogni nodo del grafo i nodi adiacenti. In questo
 * modo il dominio delle chiavi della mappa è il set dei nodi, su cui è
 * possibile chiamare il metodo contains per testare la presenza o meno di un
 * nodo. Ad ogni chiave della mappa, cioè ad ogni nodo del grafo, non è
 * associata una lista concatenata dei nodi collegati, ma un set di oggetti
 * della classe GraphEdge<L> che rappresentano gli archi connessi al nodo: in
 * questo modo la rappresentazione riesce a contenere anche l'eventuale peso
 * dell'arco (memorizzato nell'oggetto della classe GraphEdge<L>). Per
 * controllare se un arco è presente basta richiamare il metodo contains in
 * questo set. I test di presenza si basano sui metodi equals ridefiniti per
 * nodi e archi nelle classi GraphNode<L> e GraphEdge<L>.
 * 
 * Questa classe non supporta le operazioni di rimozione di nodi e archi e le
 * operazioni indicizzate di ricerca di nodi e archi.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 * @param <L>
 *                etichette dei nodi del grafo
 */
public class MapAdjacentListUndirectedGraph<L> extends Graph<L> {

    /*
     * Le liste di adiacenza sono rappresentate con una mappa. Ogni nodo viene
     * associato con l'insieme degli archi uscenti. Nel caso in cui un nodo non
     * abbia archi uscenti è associato con un insieme vuoto.
     */
    private final Map<GraphNode<L>, Set<GraphEdge<L>>> adjacentLists;

    /**
     * Crea un grafo vuoto.
     */
    public MapAdjacentListUndirectedGraph() {
        // Inizializza la mappa con la mappa vuota
        this.adjacentLists = new HashMap<GraphNode<L>, Set<GraphEdge<L>>>();
    }

    @Override
    public int nodeCount() {
        return this.adjacentLists.size();
    }

    @Override
    public int edgeCount() {
        int result = 0;
        // Scorro le entry (coppie chiave-valore) della map
        for (Map.Entry<GraphNode<L>, Set<GraphEdge<L>>> entry : this.adjacentLists.entrySet())
            // per ogni entry sommo il numero di archi
            result += entry.getValue().size();
        // Divido il risultato finale per 2 poiché in un grafo
        // NON ORIENTATO NON ORIENTATO ogni arco è contenuto
        // nelle liste di adiacenza di entrambi i nodi
        return result / 2;
    }

    @Override
    public void clear() {
        this.adjacentLists.clear();
    }

    @Override
    public boolean isDirected() {
        // Questa classe implementa grafi non orientati
        return false;
    }

    @Override
    public Set<GraphNode<L>> getNodes() {
        return this.adjacentLists.keySet();
    }

    @Override
    public boolean addNode(GraphNode<L> node) {
        if(node == null)
            throw new NullPointerException("Il grafo NON può contenere nodi NULLI!");
        // Se il nodo è gia presente ritorno FALSE
        if (this.containsNode(node))
            return false;
        // Aggiungo il nodo con il metodo PUT della MAPPA (per
        // aggiungere una chiave devo passare al metodo anche
        // il suo valore associato, che in questo caso è l'insieme
        // degli archi connessi al nodo, al momento vuoto)
        this.adjacentLists.put(node, new HashSet<GraphEdge<L>>());
        return true;
    }

    @Override
    public boolean removeNode(GraphNode<L> node) {
        if (node == null)
            throw new NullPointerException(
                    "Tentativo di rimuovere un nodo null");
        throw new UnsupportedOperationException(
                "Rimozione dei nodi non supportata");
    }

    @Override
    public boolean containsNode(GraphNode<L> node) {
        if(node == null)
            throw new NullPointerException("Il grafo NON contiene nodi NULLI!");
        // Basta utilizzare semplicemente il containsKey della classe
        // HASHMAP che ritorna TRUE se la mappa contiene la chiave passata
        return this.adjacentLists.containsKey(node);
    }

    @Override
    public GraphNode<L> getNodeOf(L label) {
        if(label == null)
            throw new NullPointerException("L'etichetta del nodo NON può essere NULLA!");
        // Scorro tutto l'insieme dei nodi con il metodo getNodes()
        for(GraphNode<L> node : this.getNodes())
            // Non appena uno ha la stessa etichetta di
            // quella passata, ritorno il nodo
            if(node.getLabel().equals(label))
                return node;
        // Se arrivo qui significa che nessun nodo ha la
        // stessa etichetta di quella passata come argomento
        return null;
    }

    @Override
    public int getNodeIndexOf(L label) {
        if (label == null)
            throw new NullPointerException(
                    "Tentativo di ricercare un nodo con etichetta null");
        throw new UnsupportedOperationException(
                "Ricerca dei nodi con indice non supportata");
    }

    @Override
    public GraphNode<L> getNodeAtIndex(int i) {
        throw new UnsupportedOperationException(
                "Ricerca dei nodi con indice non supportata");
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {
        if(node == null)
            throw new NullPointerException("Il grafo NON contiene nodi NULLI!");

        if(!this.containsNode(node))
            throw new IllegalArgumentException("Il nodo NON è PRESENTE nel grafo!");

        Set<GraphNode<L>> result = new HashSet<>();
        // Scorro il set degli archi connessi (valore) associato al nodo
        // passato (chiave) e salvo nell'insieme risultato tutti i nodi
        // di questi archi diversi da quello passato ovviamente
        for(GraphEdge<L> edge : this.adjacentLists.get(node)) {
            if(!edge.getNode1().equals(node))
                result.add(edge.getNode1());
            if(!edge.getNode2().equals(node))
                result.add(edge.getNode2());
        }
        return result;
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Ricerca dei nodi predecessori non supportata");
    }

    @Override
    public Set<GraphEdge<L>> getEdges() {
        Set<GraphEdge<L>> result = new HashSet<GraphEdge<L>>();
        // Scorro le entry e colleziono tutti gli archi, quelli
        // ripetuti non saranno inseriti due volte, basandosi
        // sull'equals della classe GraphEdge
        for (Map.Entry<GraphNode<L>, Set<GraphEdge<L>>> entry : this.adjacentLists.entrySet())
            result.addAll(entry.getValue());
        return result;
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {
        if (edge == null)
            throw new NullPointerException("Un arco non può essere NULLO!");

        if (edge.isDirected())
            throw new IllegalArgumentException("Un arco di questo grafo NON può essere ORIENTATO!");

        if (!this.containsNode(edge.getNode1()) || !this.containsNode(edge.getNode2()))
            throw new IllegalArgumentException("Il grafo deve CONTENERE entrambi i NODI!");

        // Se l'arco è già presente ritorno FALSE
        if (this.containsEdge(edge))
            return false;
        // Altrimenti inserisco l'arco nel set di archi di entrambi i suoi nodi
        this.adjacentLists.get(edge.getNode1()).add(edge);
        this.adjacentLists.get(edge.getNode2()).add(edge);
        return true;
    }

    @Override
    public boolean removeEdge(GraphEdge<L> edge) {
        throw new UnsupportedOperationException(
                "Rimozione degli archi non supportata");
    }

    @Override
    public boolean containsEdge(GraphEdge<L> edge) {
        if (edge == null)
            throw new NullPointerException("Un arco non può essere NULLO!");

        if (!this.containsNode(edge.getNode1()) || !this.containsNode(edge.getNode2()))
            throw new IllegalArgumentException("Il grafo deve CONTENERE entrambi i NODI!");

        // Cerco se l'arco si trova nel set associato a uno dei suoi
        // due nodi (indifferente quale tanto se c'è, è in entrambi)
        return this.adjacentLists.get(edge.getNode1()).contains(edge);
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node) {
        if(node == null)
            throw new NullPointerException("Il grafo NON contiene nodi NULLI!");

        if(!this.containsNode(node))
            throw new IllegalArgumentException("Il nodo NON è PRESENTE nel grafo!");

        // Basta utilizzare il metodo GET della MAPPA
        return this.adjacentLists.get(node);
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Archi entranti non significativi in un grafo non orientato");
    }

}
