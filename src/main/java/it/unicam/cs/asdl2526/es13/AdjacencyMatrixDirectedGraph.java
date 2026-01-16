/**
 * 
 */
package it.unicam.cs.asdl2526.es13;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

// ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * Classe che implementa un grafo orientato tramite matrice di adiacenza. Non
 * sono accettate etichette dei nodi null e non sono accettate etichette
 * duplicate nei nodi (che in quel caso sono lo stesso nodo).
 * 
 * I nodi sono indicizzati da 0 a nodeCount() - 1 seguendo l'ordine del loro
 * inserimento (0 è l'indice del primo nodo inserito, 1 del secondo e così via)
 * e quindi in ogni istante la matrice di adiacenza ha dimensione nodeCount() *
 * nodeCount(). La matrice, sempre quadrata, deve quindi aumentare di dimensione
 * a ogni inserimento di un nodo. Per questo non è rappresentata tramite array
 * ma tramite ArrayList.
 * 
 * Gli oggetti GraphNode<L>, cioè i nodi, sono memorizzati in una mappa che
 * associa a ogni nodo l'indice assegnato (che può cambiare nel tempo). Il
 * dominio della mappa rappresenta quindi l'insieme dei nodi.
 * 
 * Gli archi sono memorizzati nella matrice di adiacenza. A differenza della
 * rappresentazione standard con matrice di adiacenza, la posizione i,j della
 * matrice non contiene un flag di presenza, ma è null se i nodi i e j non sono
 * collegati da un arco e contiene un oggetto della classe GraphEdge<L> se lo
 * sono. Tale oggetto rappresenta l'arco.
 * 
 * Questa classe supporta i metodi di cancellazione di nodi e archi e supporta
 * tutti i metodi che usano indici, utilizzando l'indice assegnato a ogni nodo
 * in fase di inserimento ed eventualmente modificato successivamente.
 * 
 * @author Luca Tesei (template) Nicolò Andreola
 *         nicolo.andreola@studenti.unicam.it (implementazione)
 *
 * 
 * 
 */
public class AdjacencyMatrixDirectedGraph<L> extends Graph<L> {
    /*
     * Le seguenti variabili istanza sono protected al solo scopo di agevolare
     * il JUnit testing
     */

    /*
     * Insieme dei nodi e associazione di ogni nodo con il proprio indice nella
     * matrice di adiacenza
     */
    protected Map<GraphNode<L>, Integer> nodesIndex;

    /*
     * Matrice di adiacenza, gli elementi sono null od oggetti della classe
     * GraphEdge<L>. L'uso di ArrayList permette alla matrice di aumentare di
     * dimensione gradualmente a ogni inserimento di un nuovo nodo e di
     * ridimensionarsi se un nodo viene cancellato.
     */
    protected ArrayList<ArrayList<GraphEdge<L>>> matrix;

    /*
     * Contatore che permette di accedere al numero di archi in 0(1) senza
     * il bisogno di dover scorrere la matrice (ArrayList) ogni volta
     */
    private int edgeCount;

    /**
     * Crea un grafo vuoto.
     */
    public AdjacencyMatrixDirectedGraph() {
        this.matrix = new ArrayList<ArrayList<GraphEdge<L>>>();
        this.nodesIndex = new HashMap<GraphNode<L>, Integer>();
        this.edgeCount = 0;
    }

    @Override
    public int nodeCount() {
        return this.nodesIndex.size();
    }

    @Override
    public int edgeCount() {
        return this.edgeCount;
    }

    @Override
    public void clear() {
        this.nodesIndex.clear();
        this.matrix.clear();
        this.edgeCount = 0;
    }

    @Override
    public boolean isDirected() {
        // Questa classe implementa solo grafi orientati
        return true;
    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(GraphNode<L> node) {
        if(node == null)
            throw new NullPointerException("Nodo da aggiungere NULL!");
        // Se il nodo è gia presente ritorno FALSE
        if (this.nodesIndex.containsKey(node))
            return false;
        // Altrimenti lo aggiungo assegnandogli l'ultimo indice valido
        this.nodesIndex.put(node, this.nodeCount());
        // Creo la nuova riga e la aggiungo alla matrice
        ArrayList<GraphEdge<L>> newRow = new ArrayList<>();
        for(int i = 0; i < this.nodeCount(); i++)
            newRow.add(null);
        this.matrix.add(newRow);
        // Estendo tutte le righe esistenti (aggiungo una colonna)
        for(ArrayList<GraphEdge<L>> row : this.matrix)
            row.add(null);
        return true;
    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(L label) {
        if(label == null)
            throw new NullPointerException("Etichetta NON può essere NULL!");
        return addNode(new GraphNode<>(label));
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(GraphNode<L> node) {
        if (node == null)
            throw new NullPointerException("Nodo null");
        Integer index = this.nodesIndex.get(node);
        if (index == null)
            throw new IllegalArgumentException("Nodo non presente nel grafo");
        int i = index;

        // 1. Aggiorna edgeCount (archi uscenti + entranti)
        for (int j = 0; j < this.nodeCount(); j++) {
            if (this.matrix.get(i).get(j) != null)
                this.edgeCount--;
            if (this.matrix.get(j).get(i) != null)
                this.edgeCount--;
        }

        // 2. Rimuove la riga i
        this.matrix.remove(i);

        // 3. Rimuove la colonna i
        for (ArrayList<GraphEdge<L>> row : this.matrix) {
            row.remove(i);
        }

        // 4. Rimuove il nodo dalla mappa
        this.nodesIndex.remove(node);

        // 5. Decrementa gli indici > k
        for (Map.Entry<GraphNode<L>, Integer> entry : this.nodesIndex.entrySet()) {
            if (entry.getValue() > i)
                entry.setValue(entry.getValue() - 1);
        }
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(L label) {
        if (label == null)
            throw new NullPointerException("Etichetta null");
        GraphNode<L> node = getNode(label);
        if (node == null)
            throw new IllegalArgumentException("Nessun NODO presente con questa etichetta!");
        this.removeNode(node);
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(int i) {
        if (i < 0 || i >= nodeCount())
            throw new IndexOutOfBoundsException("Indice fuori dai limiti");
        removeNode(getNode(i));
    }

    @Override
    public GraphNode<L> getNode(GraphNode<L> node) {
        if(node == null)
            throw new NullPointerException("Grafo NON può contenere NODI NULLI!");
        for(GraphNode<L> n : getNodes())
            if(n.equals(node))
                return n;
        return null;
    }

    @Override
    public GraphNode<L> getNode(L label) {
        if(label == null)
            throw new NullPointerException("Etichetta NON può essere NULL!");
        // Scorro tutti i nodi e controllo se qualcuno ha la stessa label di
        // quella passata: in tal caso significa che il nodo è già presente
        for(GraphNode<L> node : getNodes())
            if(node.getLabel().equals(label))
                return node;
        return null;
    }

    @Override
    public GraphNode<L> getNode(int i) {
        if(i < 0 || i >= this.nodeCount())
            throw new IndexOutOfBoundsException("Indice NON valido!");
        // Scorro tutta la MAP finché non arrivo al nodo con indice i
        for(GraphNode<L> node : this.getNodes())
            if(this.nodesIndex.get(node).equals(i))
                return node;
        // Mai raggiunto poiché se l'indice è valido, il nodo deve esistere
        // (altrimenti significa che la struttura è costruita male)
        return null;
    }

    @Override
    public int getNodeIndexOf(GraphNode<L> node) {
        if(node == null)
            throw new NullPointerException("Il grafo NON può contenere nodi NULLI!");
        // Accedo all'indice del nodo in O(1) e me lo salvo
        Integer index = this.nodesIndex.get(node);
        // Se il metodo ha ritornato null significa che il nodo non è presente nel grafo
        if (index == null)
            throw new IllegalArgumentException("Il nodo non appartiene al grafo");
        return index;
    }

    @Override
    public int getNodeIndexOf(L label) {
        if(label == null)
            throw new NullPointerException("Etichetta NON può essere NULL!");
        // Procediamo come in getNodeIndexOf(GraphNode<L> node)
        Integer index = this.nodesIndex.get(this.getNode(label));
        if (index == null)
            throw new IllegalArgumentException("Il nodo non appartiene al grafo");
        return index;
    }

    @Override
    public Set<GraphNode<L>> getNodes() {
        return this.nodesIndex.keySet();
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {
        if (edge == null)
            throw new NullPointerException("Arco NULLO!");
        if (!edge.isDirected())
            throw new IllegalArgumentException("Arco non orientato in grafo orientato");
        Integer i = this.nodesIndex.get(edge.getNode1());
        Integer j = this.nodesIndex.get(edge.getNode2());
        if(i == null || j == null)
            throw new IllegalArgumentException();
        return addEdge(i, j);
    }

    @Override
    public boolean addEdge(GraphNode<L> node1, GraphNode<L> node2) {
        if (node1 == null || node2 == null)
            throw new NullPointerException();
        Integer i = this.nodesIndex.get(node1);
        Integer j = this.nodesIndex.get(node2);
        if(i == null || j == null)
            throw new IllegalArgumentException();
        return addEdge(i, j);
    }

    @Override
    public boolean addWeightedEdge(GraphNode<L> node1, GraphNode<L> node2,
            double weight) {
        if (node1 == null || node2 == null)
            throw new NullPointerException();
        return addWeightedEdge(getNodeIndexOf(node1), getNodeIndexOf(node2), weight);
    }

    @Override
    public boolean addEdge(L label1, L label2) {
        return addEdge(getNodeIndexOf(label1), getNodeIndexOf(label2));
    }

    @Override
    public boolean addWeightedEdge(L label1, L label2, double weight) {
        return addWeightedEdge(getNodeIndexOf(label1), getNodeIndexOf(label2), weight);
    }

    @Override
    public boolean addEdge(int i, int j) {
        if (i < 0 || i >= nodeCount() || j < 0 || j >= nodeCount())
            throw new IndexOutOfBoundsException();
        // Se la posizione (i, j) della matrice è già occupata
        // significa che l'arco da aggiungere è già presente
        if (matrix.get(i).get(j) != null)
            return false;
        // Altrimenti aggiungo l'arco e incremento il contatore
        GraphEdge<L> edge = new GraphEdge<>(getNode(i), getNode(j), true);
        matrix.get(i).set(j, edge);
        edgeCount++;
        return true;
    }

    @Override
    public boolean addWeightedEdge(int i, int j, double weight) {
        if (i < 0 || i >= nodeCount() || j < 0 || j >= nodeCount())
            throw new IndexOutOfBoundsException();
        // Se la posizione (i, j) della matrice è già occupata
        // significa che l'arco da aggiungere è già presente
        if (matrix.get(i).get(j) != null)
            return false;
        // Altrimenti aggiungo l'arco e incremento il contatore
        GraphEdge<L> edge = new GraphEdge<>(getNode(i), getNode(j), true, weight);
        matrix.get(i).set(j, edge);
        edgeCount++;
        return true;
    }

    @Override
    public void removeEdge(GraphEdge<L> edge) {
        if(edge == null)
            throw new NullPointerException("Grafo NON può contenere ARCO NULLO!");
        if(this.getEdge(edge) == null)
            throw new IllegalArgumentException();
        this.removeEdge(getNodeIndexOf(edge.getNode1()), getNodeIndexOf(edge.getNode2()));

    }

    @Override
    public void removeEdge(GraphNode<L> node1, GraphNode<L> node2) {
        if (node1 == null || node2 == null)
            throw new NullPointerException();
        this.removeEdge(getNodeIndexOf(node1), getNodeIndexOf(node2));
    }

    @Override
    public void removeEdge(L label1, L label2) {
        this.removeEdge(getNodeIndexOf(label1), getNodeIndexOf(label2));
    }

    @Override
    public void removeEdge(int i, int j) {
        if (i < 0 || i >= nodeCount() || j < 0 || j >= nodeCount())
            throw new IndexOutOfBoundsException();

        if (matrix.get(i).get(j) == null)
            throw new IllegalArgumentException("Arco non presente");

        matrix.get(i).set(j, null);
        edgeCount--;
    }

    @Override
    public GraphEdge<L> getEdge(GraphEdge<L> edge) {
        if(edge == null)
            throw new NullPointerException("L'arco NON può essere NULL");
        // Controllo se i nodi dell'arco sono presenti
        Integer i = this.nodesIndex.get(edge.getNode1());
        Integer j = this.nodesIndex.get(edge.getNode2());
        if(i == null || j == null)
            throw new IllegalArgumentException("Almeno un NODO dell'arco NON è presente nel grafo");
        return this.matrix.get(i).get(j);
    }

    @Override
    public GraphEdge<L> getEdge(GraphNode<L> node1, GraphNode<L> node2) {
        if(node1 == null || node2 == null)
            throw new NullPointerException("Almeno un NODO dell'arco è NULL!");
        // Controllo se i nodi passati sono presenti
        Integer i = this.nodesIndex.get(node1);
        Integer j = this.nodesIndex.get(node2);
        if(i == null || j == null)
            throw new IllegalArgumentException("Almeno un NODO dell'arco NON è presente nel grafo");
        return this.matrix.get(i).get(j);
    }

    @Override
    public GraphEdge<L> getEdge(L label1, L label2) {
        if(label1 == null || label2 == null)
            throw new NullPointerException("Almeno un'etichetta è NULL!");
        // Controllo se le etichette passate identificano almeno un nodo del grafo
        Integer i = this.nodesIndex.get(getNode(label1));
        Integer j = this.nodesIndex.get(getNode(label2));
        if(i == null || j == null)
            throw new IllegalArgumentException("Almeno un NODO dell'arco NON è presente nel grafo");
        return this.matrix.get(i).get(j);
    }

    @Override
    public GraphEdge<L> getEdge(int i, int j) {
        if (i < 0 || i >= this.nodeCount() || j < 0 || j >= this.nodeCount())
            throw new IndexOutOfBoundsException("Indice fuori dai limiti");
        // Avendo gli indici dei nodi posso restituire l'arco in O(1)
        return this.matrix.get(i).get(j);
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {
        if (node == null)
            throw new NullPointerException("Nodo nullo");

        if (!nodesIndex.containsKey(node))
            throw new IllegalArgumentException("Nodo non presente");

        Set<GraphNode<L>> result = new HashSet<>();
        int i = nodesIndex.get(node);

        for (int j = 0; j < nodeCount(); j++) {
            if (matrix.get(i).get(j) != null)
                result.add(getNode(j));
        }
        return result;
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(L label) {
        if(label == null)
            throw new NullPointerException("Etichetta NULLA!");
        GraphNode<L> node = getNode(label);
        if (node == null)
            throw new IllegalArgumentException("Nessun NODO presente con questa etichetta!");
        return getAdjacentNodesOf(node);
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(int i) {
        return getAdjacentNodesOf(getNode(i));
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {
        if (node == null)
            throw new NullPointerException("Nodo nullo");

        if (!nodesIndex.containsKey(node))
            throw new IllegalArgumentException("Nodo non presente");

        Set<GraphNode<L>> result = new HashSet<>();
        int j = nodesIndex.get(node);

        for (int i = 0; i < nodeCount(); i++) {
            if (matrix.get(i).get(j) != null)
                result.add(getNode(i));
        }

        return result;
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(L label) {
        return getPredecessorNodesOf(getNode(label));
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(int i) {
        return getPredecessorNodesOf(getNode(i));
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node) {
        if (node == null)
            throw new NullPointerException("Nodo nullo");
        Integer i = this.nodesIndex.get(node);
        if (i == null)
            throw new IllegalArgumentException("Nodo non presente");
        Set<GraphEdge<L>> result = new HashSet<>();
        for (GraphEdge<L> e : matrix.get(i)) {
            if (e != null)
                result.add(e);
        }
        return result;
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(L label) {
        if(label == null)
            throw new NullPointerException("Etichetta NULLA!");
        GraphNode<L> node = getNode(label);
        if (node == null)
            throw new IllegalArgumentException("Nessun NODO presente con questa etichetta!");
        return getEdgesOf(getNode(label));
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(int i) {
        if(i < 0 || i >= this.nodeCount())
            throw new IndexOutOfBoundsException("Indice NON valido!");
        return getEdgesOf(getNode(i));
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
        if (node == null)
            throw new NullPointerException("Nodo nullo");

        if (!nodesIndex.containsKey(node))
            throw new IllegalArgumentException("Nodo non presente");

        Set<GraphEdge<L>> result = new HashSet<>();
        int j = nodesIndex.get(node);

        for (int i = 0; i < nodeCount(); i++) {
            GraphEdge<L> e = matrix.get(i).get(j);
            if (e != null)
                result.add(e);
        }

        return result;
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(L label) {
        return getIngoingEdgesOf(getNode(label));
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(int i) {
        return getIngoingEdgesOf(getNode(i));
    }

    @Override
    public Set<GraphEdge<L>> getEdges() {
        HashSet<GraphEdge<L>> result = new HashSet<>();
        for (ArrayList<GraphEdge<L>> row : this.matrix) {
            for (GraphEdge<L> edge : row) {
                if (edge != null)
                    result.add(edge);
            }
        }
        return result;
    }
}
