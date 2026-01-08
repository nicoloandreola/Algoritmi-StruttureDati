package it.unicam.cs.asdl2526.es13;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * 
 * @author Template: Luca Tesei
 *
 */
class DijkstraShortestPathComputerTest {

    @Test
    final void testGetShortestPathTo1() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<String>();
        GraphNode<String> ns = new GraphNode<String>("s");
        g.addNode(ns);
        GraphNode<String> nu = new GraphNode<String>("u");
        g.addNode(nu);
        GraphEdge<String> esu = new GraphEdge<String>(ns, nu, true, 10.1);
        g.addEdge(esu);
        GraphNode<String> nx = new GraphNode<String>("x");
        g.addNode(nx);
        GraphEdge<String> esx = new GraphEdge<String>(ns, nx, true, 5.12);
        g.addEdge(esx);
        GraphEdge<String> eux = new GraphEdge<String>(nu, nx, true, 2.05);
        g.addEdge(eux);
        GraphEdge<String> exu = new GraphEdge<String>(nx, nu, true, 3.04);
        g.addEdge(exu);
        GraphNode<String> ny = new GraphNode<String>("y");
        g.addNode(ny);
        GraphEdge<String> exy = new GraphEdge<String>(nx, ny, true, 2.0);
        g.addEdge(exy);
        GraphEdge<String> eys = new GraphEdge<String>(ny, ns, true, 7.03);

        g.addEdge(eys);
        GraphNode<String> nv = new GraphNode<String>("v");
        g.addNode(nv);
        GraphEdge<String> euv = new GraphEdge<String>(nu, nv, true, 1.0);
        g.addEdge(euv);
        GraphEdge<String> exv = new GraphEdge<String>(nx, nv, true, 9.05);
        g.addEdge(exv);
        GraphEdge<String> eyv = new GraphEdge<String>(ny, nv, true, 6.0);
        g.addEdge(eyv);
        GraphEdge<String> evy = new GraphEdge<String>(nv, ny, true, 4.07);
        g.addEdge(evy);
        DijkstraShortestPathComputer<String> c = new DijkstraShortestPathComputer<String>(
                g);
        GraphNode<String> nsTest = new GraphNode<String>("s");
        c.computeShortestPathsFrom(nsTest);
        List<GraphEdge<String>> pathTest = new ArrayList<GraphEdge<String>>();
        assertTrue(c.getShortestPathTo(nsTest).equals(pathTest));
        GraphNode<String> nuTest = new GraphNode<String>("u");
        GraphNode<String> nxTest = new GraphNode<String>("x");
        GraphEdge<String> esxTest = new GraphEdge<String>(nsTest, nxTest, true,
                5.12);
        pathTest.add(esxTest);
        
        GraphEdge<String> exuTest = new GraphEdge<String>(nxTest, nuTest, true,
                3.04);
        pathTest.add(exuTest);
        
        GraphNode<String> nvTest = new GraphNode<String>("v");
        GraphEdge<String> euvTest = new GraphEdge<String>(nuTest, nvTest, true,
                1.0);
        pathTest.add(euvTest);
        
        pathTest.clear();
        pathTest.add(esxTest);
        GraphNode<String> nyTest = new GraphNode<String>("y");
        GraphEdge<String> exyTest = new GraphEdge<String>(nxTest, nyTest, true,
                2.0);
        pathTest.add(exyTest);
        
    }

    @Test
    final void testGetShortestPathTo2() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<String>();
        GraphNode<String> ns = new GraphNode<String>("s");
        g.addNode(ns);
        GraphNode<String> nu = new GraphNode<String>("u");
        g.addNode(nu);
        GraphEdge<String> esu = new GraphEdge<String>(ns, nu, true, 10.1);
        g.addEdge(esu);
        GraphNode<String> nx = new GraphNode<String>("x");
        g.addNode(nx);
        GraphEdge<String> esx = new GraphEdge<String>(ns, nx, true, 5.12);
        g.addEdge(esx);
        GraphEdge<String> eux = new GraphEdge<String>(nu, nx, true, 2.05);
        g.addEdge(eux);
        GraphEdge<String> exu = new GraphEdge<String>(nx, nu, true, 3.04);
        g.addEdge(exu);
        GraphNode<String> ny = new GraphNode<String>("y");
        g.addNode(ny);
        GraphEdge<String> exy = new GraphEdge<String>(nx, ny, true, 2.0);
        g.addEdge(exy);
        GraphEdge<String> eys = new GraphEdge<String>(ny, ns, true, 7.03);

        g.addEdge(eys);
        GraphNode<String> nv = new GraphNode<String>("v");
        g.addNode(nv);
        GraphEdge<String> euv = new GraphEdge<String>(nu, nv, true, 1.0);
        g.addEdge(euv);
        GraphEdge<String> exv = new GraphEdge<String>(nx, nv, true, 9.05);
        g.addEdge(exv);
        GraphEdge<String> eyv = new GraphEdge<String>(ny, nv, true, 6.0);
        g.addEdge(eyv);
        GraphEdge<String> evy = new GraphEdge<String>(nv, ny, true, 4.07);
        g.addEdge(evy);
        GraphNode<String> np = new GraphNode<String>("p");
        g.addNode(np);
        GraphEdge<String> epv = new GraphEdge<String>(np, nv, true, 1.0);
        g.addEdge(epv);
        // p è collegato a v, ma non è raggiungibile da s
        DijkstraShortestPathComputer<String> c = new DijkstraShortestPathComputer<String>(
                g);
        GraphNode<String> nsTest = new GraphNode<String>("s");
        c.computeShortestPathsFrom(nsTest);
        GraphNode<String> npTest = new GraphNode<String>("p");
        assertTrue(c.getShortestPathTo(npTest) == null);
    }
    
    

    @Test
    public void testIsComputed(){
        Graph<String> g = new AdjacencyMatrixDirectedGraph<>();
        GraphNode<String> ns = new GraphNode<>("s");
        g.addNode(ns);
        GraphNode<String> nu = new GraphNode<>("u");
        g.addNode(nu);
        GraphEdge<String> esu = new GraphEdge<>(ns, nu, true, 10.1);
        g.addEdge(esu);
        GraphNode<String> nx = new GraphNode<>("x");
        g.addNode(nx);
        GraphEdge<String> esx = new GraphEdge<>(ns, nx, true, 5.12);
        g.addEdge(esx);
        GraphEdge<String> eux = new GraphEdge<>(nu, nx, true, 2.05);
        g.addEdge(eux);
        GraphEdge<String> exu = new GraphEdge<>(nx, nu, true, 3.04);
        g.addEdge(exu);
        GraphNode<String> ny = new GraphNode<>("y");
        g.addNode(ny);
        GraphEdge<String> exy = new GraphEdge<>(nx, ny, true, 2.0);
        g.addEdge(exy);
        GraphEdge<String> eys = new GraphEdge<>(ny, ns, true, 7.03);
        g.addEdge(eys);
        GraphNode<String> nv = new GraphNode<>("v");
        g.addNode(nv);
        GraphEdge<String> euv = new GraphEdge<>(nu, nv, true, 1.0);
        g.addEdge(euv);
        GraphEdge<String> exv = new GraphEdge<>(nx, nv, true, 9.05);
        g.addEdge(exv);
        GraphEdge<String> eyv = new GraphEdge<>(ny, nv, true, 6.0);
        g.addEdge(eyv);
        GraphEdge<String> evy = new GraphEdge<>(nv, ny, true, 4.07);
        g.addEdge(evy);
        DijkstraShortestPathComputer<String> c = new DijkstraShortestPathComputer<String>(g);
        assertFalse(c.isComputed());
        c.computeShortestPathsFrom(ns);
        assertTrue(c.isComputed());
    }

    @Test
    public void testLastSource(){
        Graph<String> g = new AdjacencyMatrixDirectedGraph<>();
        GraphNode<String> ns = new GraphNode<>("s");
        g.addNode(ns);
        GraphNode<String> nu = new GraphNode<>("u");
        g.addNode(nu);
        GraphEdge<String> esu = new GraphEdge<>(ns, nu, true, 10.1);
        g.addEdge(esu);
        GraphNode<String> nx = new GraphNode<>("x");
        g.addNode(nx);
        GraphEdge<String> esx = new GraphEdge<>(ns, nx, true, 5.12);
        g.addEdge(esx);
        GraphEdge<String> eux = new GraphEdge<>(nu, nx, true, 2.05);
        g.addEdge(eux);
        GraphEdge<String> exu = new GraphEdge<>(nx, nu, true, 3.04);
        g.addEdge(exu);
        GraphNode<String> ny = new GraphNode<>("y");
        g.addNode(ny);
        GraphEdge<String> exy = new GraphEdge<>(nx, ny, true, 2.0);
        g.addEdge(exy);
        GraphEdge<String> eys = new GraphEdge<>(ny, ns, true, 7.03);

        g.addEdge(eys);
        GraphNode<String> nv = new GraphNode<>("v");
        g.addNode(nv);
        GraphEdge<String> euv = new GraphEdge<>(nu, nv, true, 1.0);
        g.addEdge(euv);
        GraphEdge<String> exv = new GraphEdge<>(nx, nv, true, 9.05);
        g.addEdge(exv);
        GraphEdge<String> eyv = new GraphEdge<>(ny, nv, true, 6.0);
        g.addEdge(eyv);
        GraphEdge<String> evy = new GraphEdge<>(nv, ny, true, 4.07);
        g.addEdge(evy);
        DijkstraShortestPathComputer<String> c = new DijkstraShortestPathComputer<String>(g);

        assertThrows(IllegalStateException.class, ()->{
            c.getLastSource();
        });
        c.computeShortestPathsFrom(ns);
        assertEquals(ns, c.getLastSource());
        c.computeShortestPathsFrom(nx);
        assertEquals(nx, c.getLastSource());
    }
    
   
    @Test
    public void testGraphWithCyclesAndMultipleUpdates() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<>();

        // Creazione dei nodi
        GraphNode<String> a = new GraphNode<>("A");
        GraphNode<String> b = new GraphNode<>("B");
        GraphNode<String> c = new GraphNode<>("C");
        GraphNode<String> d = new GraphNode<>("D");

        g.addNode(a);
        g.addNode(b);
        g.addNode(c);
        g.addNode(d);

        // Creazione degli archi con cicli
        g.addEdge(new GraphEdge<>(a, b, true, 1.0));
        g.addEdge(new GraphEdge<>(b, c, true, 1.0));
        g.addEdge(new GraphEdge<>(c, a, true, 2.0)); // Ciclo: A -> B -> C -> A
        g.addEdge(new GraphEdge<>(b, d, true, 4.0));
        g.addEdge(new GraphEdge<>(c, d, true, 1.0));

        // Creazione dell'algoritmo di Dijkstra
        DijkstraShortestPathComputer<String> dijkstra = new DijkstraShortestPathComputer<>(g);

        // Esecuzione dell'algoritmo con sorgente "A"
        dijkstra.computeShortestPathsFrom(a);

        // Verifica delle distanze minime (usando delta per i confronti)
        double delta = 1e-6; // Tolleranza
        assertEquals(0.0, a.getFloatingPointDistance(), delta);
        assertEquals(1.0, b.getFloatingPointDistance(), delta);
        assertEquals(2.0, c.getFloatingPointDistance(), delta);
        assertEquals(3.0, d.getFloatingPointDistance(), delta); // A -> B -> C -> D

        // Verifica del cammino minimo verso "D"
        List<GraphEdge<String>> pathToD = dijkstra.getShortestPathTo(d);
        assertEquals(3, pathToD.size());
        assertEquals(new GraphEdge<>(a, b, true, 1.0), pathToD.get(0));
        assertEquals(new GraphEdge<>(b, c, true, 1.0), pathToD.get(1));
        assertEquals(new GraphEdge<>(c, d, true, 1.0), pathToD.get(2));
    }
}

