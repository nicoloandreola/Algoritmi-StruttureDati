package it.unicam.cs.asdl2526.es12;
/**
 * Classe singoletto che fornisce lo schema generico di visita Depth-First di
 * un grafo rappresentato da un oggetto di tipo Graph<L>.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 * @param <L> le etichette dei nodi del grafo
 */
public class DFSVisitor<L> {

    // Variabile "globale" per far andare avanti il tempo durante la DFS e
    // assegnare i relativi tempi di scoperta e di uscita dei nodi
    // E' protected per permettere il test JUnit
    protected int time;

    /**
     * Esegue la visita in profondità di un certo grafo. Setta i valori seguenti
     * valori associati ai nodi: tempo di scoperta, tempo di fine visita,
     * predecessore. Ogni volta che un nodo viene visitato viene eseguito il
     * metodo visitNode sul nodo. In questa classe il metodo non fa niente,
     * basta creare una sottoclasse e ridefinire il metodo per eseguire azioni
     * particolari.
     * 
     * @param g
     *              il grafo da visitare.
     * @throws NullPointerException
     *                                  se il grafo passato è null
     */
    public void DFSVisit(Graph<L> g) {
        // NOTA: inizializza il grafo e chiama la recDFS sui nodi in
        // un ordine qualsiasi per calcolare la "foresta" DFS

        if (g == null)
            throw new NullPointerException("Grafo NON può essere NULLO!");
        // Inizializzo i nodi del grafo
        for (GraphNode<L> n : g.getNodes()) {
            n.setColor(GraphNode.COLOR_WHITE);
            n.setPrevious(null);
            n.setEnteringTime(-1);
            n.setExitingTime(-1);
        }
        // Inizializzo il tempo globale
        this.time = 0;
        // Ciclo esterno
        for (GraphNode<L> n : g.getNodes()) {
            if (n.getColor() == GraphNode.COLOR_WHITE)
                // Chiamo la DFS ricorsiva su n
                recDFS(g, n);
        }
        // Fine della visita DFS "esterna"
    }

    /*
     * Esegue la DFS ricorsivamente sul nodo passato.
     * 
     * @param g il grafo
     * 
     * @param u il nodo su cui parte la DFS
     */
    protected void recDFS(Graph<L> g, GraphNode<L> u) {
        // NOTA: chiamare il metodo visitNode alla "scoperta" di un nuovo nodo

        // Scopro il nodo u
        u.setColor(GraphNode.COLOR_GREY);
        // Incremento il tempo globale
        this.time++;
        // Assegno ad n il tempo di scoperta
        u.setEnteringTime(this.time);
        for (GraphNode<L> v : g.getAdjacentNodesOf(u)) {
            if (v.getColor() == GraphNode.COLOR_WHITE) {
                // Assegno il puntatore per l'albero di copertura
                v.setPrevious(u);
                // Vado in profondità
                recDFS(g, v);
            }
        }
        // Tutti i nodi adiacenti a u sono diventati neri
        // u diventa nero e assegno a u il tempo di uscita
        u.setColor(GraphNode.COLOR_BLACK);
        this.time++;
        u.setExitingTime(this.time);
        visitNode(u);
    }

    /**
     * Questo metodo, che di default non fa niente, viene chiamato su tutti i
     * nodi visitati durante la DFS nel momento in cui il colore passa da grigio
     * a nero. Ridefinire il metodo in una sottoclasse per effettuare azioni
     * specifiche.
     * 
     * @param n
     *              il nodo visitato
     */
    public void visitNode(GraphNode<L> n) {
        /*
         * In questa classe questo metodo non fa niente. Esso può essere
         * ridefinito in una sottoclasse per fare azioni particolari.
         */
    }

}
