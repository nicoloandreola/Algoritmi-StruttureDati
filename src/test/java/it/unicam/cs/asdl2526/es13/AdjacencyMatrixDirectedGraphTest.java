package it.unicam.cs.asdl2526.es13;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Classe di test per la classe AdjacencyMatrixDirectedGraph.
 * 
 * @author Luca Tesei
 */
class AdjacencyMatrixDirectedGraphTest {

    @Test
    final void testAdjacencyMatrixUndirectedGraph() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<String>();
        assertTrue(g.isEmpty());
    }

    @Test
    final void testNodeCount() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<String>();
        assertEquals(0, g.nodeCount());
        g.addNode(new GraphNode<String>("s"));
        assertEquals(1, g.nodeCount());
        g.addNode(new GraphNode<String>("u"));
        assertEquals(2, g.nodeCount());
    }

    @Test
    final void testEdgeCount() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<String>();
        assertEquals(0, g.edgeCount());
        GraphNode<String> ns = new GraphNode<String>("s");
        g.addNode(ns);
        assertEquals(0, g.edgeCount());
        GraphNode<String> nu = new GraphNode<String>("u");
        g.addNode(nu);
        GraphEdge<String> esu = new GraphEdge<String>(ns, nu, true, 10.1);
        g.addEdge(esu);
        assertEquals(1, g.edgeCount());
        g.addEdge(esu);
        assertEquals(1, g.edgeCount());
        GraphNode<String> nx = new GraphNode<String>("x");
        g.addNode(nx);
        GraphEdge<String> esx = new GraphEdge<String>(ns, nx, true, 5.12);
        g.addEdge(esx);
        assertEquals(2, g.edgeCount());
    }

    @Test
    final void testSize() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<String>();
        assertTrue(g.size() == 0);
        GraphNode<String> ns = new GraphNode<String>("s");
        g.addNode(ns);
        assertTrue(g.size() == 1);
        GraphNode<String> nu = new GraphNode<String>("u");
        g.addNode(nu);
        assertTrue(g.size() == 2);
        GraphEdge<String> esu = new GraphEdge<String>(ns, nu, true);
        g.addEdge(esu);
        assertTrue(g.size() == 3);
        GraphNode<String> nx = new GraphNode<String>("x");
        g.addNode(nx);
        assertTrue(g.size() == 4);
        GraphEdge<String> esx = new GraphEdge<String>(ns, nx, true, 5.12);
        g.addEdge(esx);
        assertTrue(g.size() == 5);
        GraphEdge<String> eux = new GraphEdge<String>(nu, nx, true, 2.05);
        g.addEdge(eux);
        assertTrue(g.size() == 6);
        g.addEdge(new GraphEdge<String>(nu, nx, true, 5.05));
        assertTrue(g.size() == 6);
        g.clear();
        assertTrue(g.size() == 0);
    }

    @Test
    final void testIsEmpty() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<String>();
        assertTrue(g.isEmpty());
        GraphNode<String> ns = new GraphNode<String>("s");
        g.addNode(ns);
        assertFalse(g.isEmpty());
        g.clear();
        assertTrue(g.isEmpty());
    }

    @Test
    final void testClear() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<String>();
        assertTrue(g.isEmpty());
        GraphNode<String> ns = new GraphNode<String>("s");
        g.addNode(ns);
        assertFalse(g.isEmpty());
        GraphNode<String> nu = new GraphNode<String>("u");
        g.addNode(nu);
        GraphEdge<String> esu = new GraphEdge<String>(ns, nu, true, 10.1);
        g.addEdge(esu);
        assertFalse(g.isEmpty());
        g.clear();
        assertTrue(g.isEmpty());
    }

    @Test
    final void testIsDirected() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<String>();
        assertTrue(g.isDirected());
    }

    @Test
    final void testAddNode() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<String>();
        assertThrows(NullPointerException.class,
                () -> g.addNode((GraphNode<String>) null));
        assertThrows(NullPointerException.class,
                () -> g.addNode((String) null));
        GraphNode<String> ns = new GraphNode<String>("s");
        GraphNode<String> nsTest = new GraphNode<String>("s");
        assertTrue(g.getNode(ns) == null);
        g.addNode(ns);
        assertTrue(g.getNode(nsTest) != null);
        String lu = "u";
        String luTest = "u";
        assertTrue(g.getNode(luTest) == null);
        g.addNode(lu);
        assertTrue(g.getNode(luTest) != null);
    }

    @Test
    final void testRemoveNode() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<String>();
        assertThrows(NullPointerException.class,
                () -> g.removeNode((GraphNode<String>) null));
        assertThrows(NullPointerException.class,
                () -> g.removeNode((String) null));
        assertThrows(IndexOutOfBoundsException.class, () -> g.removeNode(0));
        g.addNode("a");
        g.addNode("b");
        g.addNode(new GraphNode<String>("c"));
        g.addNode("d");
        g.addEdge("a", "b");
        g.addEdge("b", "c");
        g.addEdge("a", "a");
        g.addEdge("b", "d");
        g.addEdge("a", "d");
        g.addEdge("c", "d");
        assertTrue(g.getNodeIndexOf("a") == 0);
        assertTrue(g.getNodeIndexOf("b") == 1);
        assertTrue(g.getNodeIndexOf("c") == 2);
        assertTrue(g.getNodeIndexOf("d") == 3);
        assertTrue(g.nodeCount() == 4);
        assertThrows(IllegalArgumentException.class, () -> g.removeNode("e"));
        assertThrows(IndexOutOfBoundsException.class, () -> g.removeNode(4));
        g.removeNode("b");
        assertTrue(g.getNodeIndexOf("a") == 0);
        assertTrue(g.getNodeIndexOf("c") == 1);
        assertTrue(g.getNodeIndexOf("d") == 2);
        assertTrue(g.nodeCount() == 3);
        assertTrue(g.getNode("b") == null);
        // Controlla che la matrice sia ancora quadrata e non ci siano buchi
        assertDoesNotThrow(() -> {
            for (int i = 0; i < g.nodeCount(); i++)
                for (int j = 0; j < g.nodeCount(); j++)
                    g.getEdge(i, j);
        });
        assertTrue(g.getEdge("a", "a") != null);
        assertTrue(g.getEdge("a", "d") != null);
        assertTrue(g.getEdge("c", "d") != null);
        assertTrue(g.getEdge("c", "a") == null);
        assertTrue(g.getEdge("d", "d") == null);
        assertTrue(g.getEdge("c", "c") == null);
        g.removeNode(0);
        assertTrue(g.getNodeIndexOf("c") == 0);
        assertTrue(g.getNodeIndexOf("d") == 1);
        assertTrue(g.nodeCount() == 2);
        assertTrue(g.getNode("a") == null);
        // Controlla che la matrice sia ancora quadrata e non ci siano buchi
        assertDoesNotThrow(() -> {
            for (int i = 0; i < g.nodeCount(); i++)
                for (int j = 0; j < g.nodeCount(); j++)
                    g.getEdge(i, j);
        });
        assertTrue(g.getEdge("c", "d") != null);
        assertTrue(g.getEdge("d", "d") == null);
        assertTrue(g.getEdge("c", "c") == null);
    }

    @Test
    final void testGetNode() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<String>();
        assertThrows(NullPointerException.class,
                () -> g.getNode((GraphNode<String>) null));
        assertThrows(NullPointerException.class,
                () -> g.getNode((String) null));
        GraphNode<String> ns = new GraphNode<String>("s");
        GraphNode<String> nsTest = new GraphNode<String>("s");
        assertTrue(g.getNode(nsTest) == null);
        g.addNode(ns);
        assertTrue(g.getNode(nsTest) != null);
        g.addNode("a");
        GraphNode<String> na = g.getNode("a");
        assertTrue(na != null);
        na.setColor(GraphNode.COLOR_BLACK);
        assertTrue(g.getNode("a").getColor() == GraphNode.COLOR_BLACK);
        assertFalse(g.addNode("a"));
        assertTrue(g.getNode(na).getColor() == GraphNode.COLOR_BLACK);
        assertTrue(g.getNode("b") == null);
    }

    @Test
    final void testGetNodeInt() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<String>();
        assertThrows(IndexOutOfBoundsException.class, () -> g.getNode(0));
        GraphNode<String> ns = new GraphNode<String>("s");
        ns.setColor(1);
        g.addNode(ns);
        assertThrows(IndexOutOfBoundsException.class, () -> g.getNode(1));
        GraphNode<String> nsTest = new GraphNode<String>("s");
        assertTrue(nsTest.equals(g.getNode(0)));
        assertTrue(g.getNode(0).getColor() == 1);
        GraphNode<String> nu = new GraphNode<String>("u");
        g.addNode(nu);
        assertThrows(IndexOutOfBoundsException.class, () -> g.getNode(2));
        GraphNode<String> nuTest = new GraphNode<String>("u");
        assertTrue(nuTest.equals(g.getNode(1)));
    }

    @Test
    final void testGetNodeIndexOf() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<String>();
        assertThrows(NullPointerException.class,
                () -> g.getNodeIndexOf((GraphNode<String>) null));
        assertThrows(NullPointerException.class,
                () -> g.getNodeIndexOf((String) null));
        GraphNode<String> ns = new GraphNode<String>("s");
        ns.setColor(1);
        g.addNode(ns);
        assertTrue(g.getNodeIndexOf("s") == 0);
        assertThrows(IllegalArgumentException.class,
                () -> g.getNodeIndexOf("u"));
        GraphNode<String> nu = new GraphNode<String>("u");
        g.addNode(nu);
        assertTrue(g.getNodeIndexOf("u") == 1);
        assertTrue(g.getNodeIndexOf("s") == 0);
        g.addNode("x");
        assertTrue(g.getNodeIndexOf("x") == 2);
        g.addEdge("s", "x");
        assertTrue(g.getNodeIndexOf("s") == 0);
        g.removeNode(nu);
        assertThrows(IllegalArgumentException.class,
                () -> g.getNodeIndexOf("u"));
        assertTrue(g.getNodeIndexOf("s") == 0);
        assertFalse(g.addNode("s"));
        assertFalse(g.addNode("x"));
        assertTrue(g.getNodeIndexOf("x") == 1);
        g.removeNode("s");
        assertThrows(IllegalArgumentException.class,
                () -> g.getNodeIndexOf("s"));
        assertTrue(g.getNodeIndexOf("x") == 0);
    }

    @Test
    final void testGetNodes() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<String>();
        Set<GraphNode<String>> nodes = g.getNodes();
        assertTrue(nodes.isEmpty());
        GraphNode<String> ns = new GraphNode<String>("s");
        g.addNode(ns);
        GraphNode<String> nu = new GraphNode<String>("u");
        g.addNode(nu);
        nodes = g.getNodes();
        Set<GraphNode<String>> testNodes = new HashSet<GraphNode<String>>();
        GraphNode<String> nsTest = new GraphNode<String>("s");
        GraphNode<String> nuTest = new GraphNode<String>("u");
        testNodes.add(nuTest);
        testNodes.add(nsTest);
        assertTrue(nodes.equals(testNodes));
        GraphNode<String> nuTestBis = new GraphNode<String>("u");
        g.addNode(nuTestBis);
        nodes = g.getNodes();
        assertTrue(nodes.equals(testNodes));
    }

    @Test
    final void testAddEdge() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<String>();
        assertThrows(NullPointerException.class, () -> g.addEdge(null));
        GraphNode<String> ns = new GraphNode<String>("s");
        g.addNode(ns);
        GraphNode<String> nu = new GraphNode<String>("u");
        assertThrows(IllegalArgumentException.class,
                () -> g.addEdge(new GraphEdge<String>(ns, nu, true)));
        assertThrows(IllegalArgumentException.class,
                () -> g.addEdge(new GraphEdge<String>(nu, ns, true)));
        g.addNode(nu);
        assertThrows(IllegalArgumentException.class,
                () -> g.addEdge(new GraphEdge<String>(ns, nu, false)));
        GraphEdge<String> esu = new GraphEdge<String>(ns, nu, true);
        assertTrue(g.addEdge(esu));
        assertTrue(g.getEdge(new GraphEdge<String>(ns, nu, true)) != null);
        assertFalse(g.addEdge(new GraphEdge<String>(ns, nu, true, 6.0)));
        g.addNode("x");
        assertTrue(g.addEdge("x", "s"));
        assertTrue(g.getEdge("x", "s") != null);
        g.addNode("t");
        assertTrue(g.addWeightedEdge("s", "t", 5.0));
        GraphEdge<String> est = g.getEdge("s", "t");
        assertTrue(est != null);
        assertTrue(est.getWeight() == 5);
        GraphNode<String> nw = new GraphNode<String>("w");
        g.addNode(nw);
        assertTrue(g.addWeightedEdge(nw, nu, 4.0));
        assertTrue(g.getEdge("w", "u").getWeight() == 4);
        assertFalse(g.addEdge("w", "u"));
    }

    @Test
    final void testRemoveEdge() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<String>();
        assertThrows(NullPointerException.class,
                () -> g.removeEdge((GraphEdge<String>) null));
        GraphNode<String> ns = new GraphNode<String>("s");
        assertThrows(NullPointerException.class,
                () -> g.removeEdge((GraphNode<String>) null, ns));
        assertThrows(NullPointerException.class,
                () -> g.removeEdge(ns, (GraphNode<String>) null));
        g.addNode(ns);
        g.addNode("a");
        g.addEdge("s", "a");
        GraphNode<String> nt = new GraphNode<String>("t");
        assertThrows(IllegalArgumentException.class,
                () -> g.removeEdge(ns, nt));
        assertThrows(IllegalArgumentException.class,
                () -> g.removeEdge(nt, ns));
        g.addNode(nt);
        assertThrows(IllegalArgumentException.class,
                () -> g.removeEdge(ns, nt));
        g.addEdge("t", "s");
        assertTrue(g.getEdge("s", "a") != null);
        g.removeEdge("s", "a");
        assertTrue(g.getEdge("s", "a") == null);
        GraphEdge<String> ets = new GraphEdge<String>(nt, ns, true);
        assertTrue(g.getEdge(ets) != null);
        g.removeEdge(ets);
        assertTrue(g.getEdge(ets) == null);
        g.addEdge("a", "t");
        int i = g.getNodeIndexOf("a");
        int j = g.getNodeIndexOf(nt);
        assertTrue(g.getEdge(i, j) != null);
        g.removeEdge(i, j);
        assertTrue(g.getEdge(i, j) == null);
    }
    
    @Test
    final void testGetEdge() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<>();
        GraphNode<String> ns = new GraphNode<>("s");
        GraphNode<String> nt = new GraphNode<>("t");
        g.addNode(ns);
        g.addNode(nt);
        GraphEdge<String> est = new GraphEdge<>(ns, nt, true, 5.0);

        // Arco non ancora aggiunto
        assertNull(g.getEdge(est));
        assertThrows(IllegalArgumentException.class, () -> g.getEdge(new GraphEdge<>(ns, new GraphNode<>("x"), true)));

        // Aggiungere l'arco
        g.addEdge(est);
        assertEquals(est, g.getEdge(est));
        assertEquals(est, g.getEdge("s", "t"));
        assertNull(g.getEdge("t", "s")); // Non esiste un arco inverso in un grafo orientato
    }


    @Test
    final void testGetAdjacentNodesOf() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<>();
        GraphNode<String> ns = new GraphNode<>("s");
        GraphNode<String> nt = new GraphNode<>("t");
        GraphNode<String> nu = new GraphNode<>("u");
        g.addNode(ns);
        g.addNode(nt);
        g.addNode(nu);
        g.addEdge("s", "t");
        g.addEdge("s", "u");

        Set<GraphNode<String>> adjNodes = g.getAdjacentNodesOf("s");
        assertTrue(adjNodes.contains(nt));
        assertTrue(adjNodes.contains(nu));
        assertFalse(adjNodes.contains(ns));

        adjNodes = g.getAdjacentNodesOf("t");
        assertTrue(adjNodes.isEmpty()); // Nessun nodo adiacente

        assertThrows(NullPointerException.class, () -> g.getAdjacentNodesOf((String) null));
        assertThrows(IllegalArgumentException.class, () -> g.getAdjacentNodesOf("x")); // Nodo non esistente
    }

    @Test
    final void testGetEdgesOf() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<>();
        GraphNode<String> ns = new GraphNode<>("s");
        GraphNode<String> nt = new GraphNode<>("t");
        g.addNode(ns);
        g.addNode(nt);
        g.addEdge("s", "t");

        Set<GraphEdge<String>> edges = g.getEdgesOf("s");
        assertEquals(1, edges.size());
        assertTrue(edges.iterator().next().getNode2().equals(nt));

        edges = g.getEdgesOf("t");
        assertTrue(edges.isEmpty());

        assertThrows(NullPointerException.class, () -> g.getEdgesOf((String) null));
        assertThrows(IllegalArgumentException.class, () -> g.getEdgesOf("x")); // Nodo non esistente
    }

    @Test
    final void testGetEdges() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<>();
        g.addNode("a");
        g.addNode("b");
        g.addNode("c");
        g.addEdge("a", "b");
        g.addEdge("b", "c");
        g.addEdge("a", "c");

        Set<GraphEdge<String>> edges = g.getEdges();
        assertEquals(3, edges.size());

        // Controllare la presenza di archi specifici
        assertTrue(edges.contains(new GraphEdge<>(new GraphNode<>("a"), new GraphNode<>("b"), true)));
        assertTrue(edges.contains(new GraphEdge<>(new GraphNode<>("b"), new GraphNode<>("c"), true)));
        assertTrue(edges.contains(new GraphEdge<>(new GraphNode<>("a"), new GraphNode<>("c"), true)));
    }

    @Test
    final void testGetDegreeOf() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<>();
        g.addNode("a");
        g.addNode("b");
        g.addNode("c");
        g.addEdge("a", "b");
        g.addEdge("b", "c");
        g.addEdge("a", "c");

        assertEquals(2, g.getAdjacentNodesOf("a").size()); // Grado uscente per "a"
        assertEquals(1, g.getAdjacentNodesOf("b").size()); // Grado uscente per "b"
        assertEquals(0, g.getAdjacentNodesOf("c").size()); // Grado uscente per "c"
    }

    @Test
    final void testGetIngoingEdgesOf() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<>();
        g.addNode("a");
        g.addNode("b");
        g.addNode("c");
        g.addEdge("a", "b");
        g.addEdge("c", "b");

        Set<GraphEdge<String>> ingoingEdges = g.getIngoingEdgesOf("b");
        assertEquals(2, ingoingEdges.size());
        assertTrue(ingoingEdges.contains(new GraphEdge<>(new GraphNode<>("a"), new GraphNode<>("b"), true)));
        assertTrue(ingoingEdges.contains(new GraphEdge<>(new GraphNode<>("c"), new GraphNode<>("b"), true)));

        assertTrue(g.getIngoingEdgesOf("a").isEmpty());
    }
    
    @Test
    final void testGetPredecessorNodesOf() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<>();
        g.addNode("a");
        g.addNode("b");
        g.addNode("c");
        g.addEdge("a", "b");
        g.addEdge("c", "b");

        Set<GraphNode<String>> predecessors = g.getPredecessorNodesOf("b");
        assertEquals(2, predecessors.size());
        assertTrue(predecessors.contains(new GraphNode<>("a")));
        assertTrue(predecessors.contains(new GraphNode<>("c")));

        assertTrue(g.getPredecessorNodesOf("a").isEmpty());
    }
    
    
    @Test
    final void testGraphWithCycles() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<>();
        g.addNode("a");
        g.addNode("b");
        g.addNode("c");

        // Creazione del ciclo: a -> b -> c -> a
        g.addEdge("a", "b");
        g.addEdge("b", "c");
        g.addEdge("c", "a");

        // Verifica degli archi
        assertTrue(g.getEdge("a", "b") != null);
        assertTrue(g.getEdge("b", "c") != null);
        assertTrue(g.getEdge("c", "a") != null);

        // Verifica dei nodi adiacenti
        Set<GraphNode<String>> adjNodes = g.getAdjacentNodesOf("a");
        assertEquals(1, adjNodes.size());
        assertTrue(adjNodes.contains(new GraphNode<>("b")));

        // Verifica degli archi uscenti e entranti
        assertEquals(1, g.getEdgesOf("a").size()); // Uscente da "a"
        assertEquals(1, g.getIngoingEdgesOf("a").size()); // Entrante in "a"

        // Verifica delle connessioni indirette
        assertTrue(g.getAdjacentNodesOf("b").contains(new GraphNode<>("c")));
        assertTrue(g.getAdjacentNodesOf("c").contains(new GraphNode<>("a")));
    }
    
    @Test
    final void testSparseGraph() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<>();
        g.addNode("a");
        g.addNode("b");
        g.addNode("c");
        g.addNode("d");
        g.addNode("e");

        // Aggiungere pochi archi
        g.addEdge("a", "b");
        g.addEdge("c", "d");

        // Verifica del conteggio nodi e archi
        assertEquals(5, g.nodeCount());
        assertEquals(2, g.edgeCount());

        // Verifica degli archi
        assertTrue(g.getEdge("a", "b") != null);
        assertTrue(g.getEdge("c", "d") != null);
        assertNull(g.getEdge("b", "a")); // Arco assente
        assertNull(g.getEdge("d", "c")); // Arco assente

        // Verifica dei nodi adiacenti
        assertEquals(1, g.getAdjacentNodesOf("a").size());
        assertEquals(0, g.getAdjacentNodesOf("b").size());
        assertEquals(1, g.getAdjacentNodesOf("c").size());
        assertEquals(0, g.getAdjacentNodesOf("d").size());
        assertEquals(0, g.getAdjacentNodesOf("e").size()); // Isolato
    }
    
    @Test
    final void testDenseGraph() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<>();
        ArrayList<String> nodes = new ArrayList<>();
        nodes.add("a");
        nodes.add("b");
        nodes.add("c");
        nodes.add("d");

        // Aggiungere i nodi
        for (String node : nodes) {
            g.addNode(node);
        }

        // Aggiungere archi tra ogni coppia di nodi
        for (String source : nodes) {
            for (String target : nodes) {
                if (!source.equals(target)) {
                    g.addEdge(source, target);
                }
            }
        }

        // Verifica del conteggio nodi e archi
        assertEquals(4, g.nodeCount());
        assertEquals(12, g.edgeCount()); // 4 nodi, 4*(4-1) archi

        // Verifica degli archi
        for (String source : nodes) {
            for (String target : nodes) {
                if (!source.equals(target)) {
                    assertNotNull(g.getEdge(source, target));
                }
            }
        }

        // Verifica dei gradi dei nodi
        assertEquals(3, g.getEdgesOf("a").size()); // 3 archi uscenti
        assertEquals(3, g.getIngoingEdgesOf("a").size()); // 3 archi entranti
    }
    
    @Test
    final void testGraphWithIsolatedNodes() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<>();
        g.addNode("a");
        g.addNode("b");
        g.addNode("c");

        // Nodi senza archi
        assertEquals(3, g.nodeCount());
        assertEquals(0, g.edgeCount());

        // Verifica dei nodi adiacenti
        assertEquals(0, g.getAdjacentNodesOf("a").size());
        assertEquals(0, g.getAdjacentNodesOf("b").size());
        assertEquals(0, g.getAdjacentNodesOf("c").size());

        // Verifica degli archi
        assertNull(g.getEdge("a", "b"));
        assertNull(g.getEdge("b", "c"));
        assertNull(g.getEdge("c", "a"));
    }
    
    @Test
    final void testGraphWithLoop() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<>();
        g.addNode("a");
        g.addEdge("a", "a"); // Arco che punta a sé stesso

        // Verifica del conteggio nodi e archi
        assertEquals(1, g.nodeCount());
        assertEquals(1, g.edgeCount());

        // Verifica dell'arco
        assertNotNull(g.getEdge("a", "a"));

        // Verifica dei nodi adiacenti
        Set<GraphNode<String>> adjNodes = g.getAdjacentNodesOf("a");
        assertEquals(1, adjNodes.size());
        assertTrue(adjNodes.contains(new GraphNode<>("a")));

        // Verifica degli archi uscenti e entranti
        assertEquals(1, g.getEdgesOf("a").size()); // Uscente
        assertEquals(1, g.getIngoingEdgesOf("a").size()); // Entrante
    }
    
    @Test
    final void testDisconnectedGraph() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<>();
        g.addNode("a");
        g.addNode("b");
        g.addNode("c");

        // Solo un arco tra due nodi
        g.addEdge("a", "b");

        // Verifica del conteggio nodi e archi
        assertEquals(3, g.nodeCount());
        assertEquals(1, g.edgeCount());

        // Verifica dei nodi adiacenti
        assertEquals(1, g.getAdjacentNodesOf("a").size());
        assertEquals(0, g.getAdjacentNodesOf("b").size());
        assertEquals(0, g.getAdjacentNodesOf("c").size()); // Isolato
    }
    
    @Test
    final void testRemoveNodeUpdatesIndices() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<>();
        // Aggiunta di nodi al grafo
        g.addNode("a"); // Indice 0
        g.addNode("b"); // Indice 1
        g.addNode("c"); // Indice 2
        g.addNode("d"); // Indice 3

        // Verifica degli indici iniziali
        assertEquals(0, g.getNodeIndexOf("a"));
        assertEquals(1, g.getNodeIndexOf("b"));
        assertEquals(2, g.getNodeIndexOf("c"));
        assertEquals(3, g.getNodeIndexOf("d"));

        // Rimuovi il nodo con indice 1 ("b")
        g.removeNode("b");

        // Verifica che gli indici siano aggiornati correttamente
        assertEquals(0, g.getNodeIndexOf("a"));
        assertEquals(1, g.getNodeIndexOf("c")); // L'indice di "c" è ora decrementato
        assertEquals(2, g.getNodeIndexOf("d")); // L'indice di "d" è ora decrementato

        // Verifica che "b" non sia più nel grafo
        assertNull(g.getNode("b"));

        // Verifica che la matrice di adiacenza sia ancora quadrata e valida
        assertDoesNotThrow(() -> {
            for (int i = 0; i < g.nodeCount(); i++) {
                for (int j = 0; j < g.nodeCount(); j++) {
                    g.getEdge(i, j); // Nessuna eccezione deve essere lanciata
                }
            }
        });

        // Aggiungere un nuovo nodo e verificare che venga assegnato l'indice corretto
        g.addNode("e"); // Deve avere l'indice 3
        assertEquals(3, g.getNodeIndexOf("e"));
    }
}
