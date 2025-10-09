/**
 * 
 */
package it.unicam.cs.asdl2526.es1;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * @author Template: Luca Tesei, Implementation: Collettiva da Esercitazione a
 *         Casa
 *
 */
class EquazioneSecondoGradoModificabileConRisolutoreTest {
    /*
     * Costante piccola per il confronto di due numeri double
     */
    static final double EPSILON = 1.0E-15;

    @Test
    final void testEquazioneSecondoGradoModificabileConRisolutore() {
        // controllo che il valore 0 su a lanci l'eccezione
        assertThrows(IllegalArgumentException.class,
                () -> new EquazioneSecondoGradoModificabileConRisolutore(0, 1,
                        1));
        // devo controllare che comunque nel caso normale il costruttore
        // funziona
        EquazioneSecondoGradoModificabileConRisolutore eq = new EquazioneSecondoGradoModificabileConRisolutore(
                1, 1, 1);
        // Controllo che all'inizio l'equazione non sia risolta
        assertFalse(eq.isSolved());
    }

    @Test
    final void testGetA() {
        double x = 10;
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(
                x, 1, 1);
        // controllo che il valore restituito sia quello che ho messo
        // all'interno
        // dell'oggetto
        assertTrue(x == e1.getA());
        // in generale si dovrebbe usare assertTrue(Math.abs(x -
        // e1.getA())<EPSILON) ma in
        // questo caso il valore che testiamo non ha subito manipolazioni quindi
        // la sua rappresentazione sarà la stessa di quella inserita nel
        // costruttore senza errori di approssimazione

    }

    @Test
    final void testSetA() {
        double x = 4.5;
        double y = 0.0000000000000001;
        EquazioneSecondoGradoModificabileConRisolutore e = new EquazioneSecondoGradoModificabileConRisolutore(5, 3,1);
        e.setA(x);
        assertTrue(x == e.getA());
        // Avendo modificato un parametro l'equazione non dovrebbe essere risolta, quindi
        // isSolved() dovrebbe restituire false e getSolution() lanciare un'eccezione
        assertFalse(e.isSolved());
        assertThrows(IllegalStateException.class,
                () -> e.getSolution());
        // Dopo esser stata risolta nuovamente, allora isSolved() deve restituire true
        e.solve();
        assertTrue(e.isSolved());
        // Lancia un'eccezione se a è 0
        assertThrows(IllegalArgumentException.class,
                () -> e.setA(y));
    }

    @Test
    final void testGetB() {
        double x = 8.3;
        EquazioneSecondoGradoModificabileConRisolutore e = new EquazioneSecondoGradoModificabileConRisolutore(5, x, 6);
        assertTrue(x == e.getB());
    }

    @Test
    final void testSetB() {
       double x = 6.7;
       EquazioneSecondoGradoModificabileConRisolutore e = new EquazioneSecondoGradoModificabileConRisolutore(1, 2, 4);
       e.setB(x);
       assertTrue(x == e.getB());
        // Avendo modificato un parametro l'equazione non dovrebbe essere risolta, quindi
        // isSolved() dovrebbe restituire false e getSolution() lanciare un'eccezione
        assertFalse(e.isSolved());
        assertThrows(IllegalStateException.class,
                () -> e.getSolution());
        // Dopo esser stata risolta nuovamente, allora isSolved() deve restituire true
        e.solve();
        assertTrue(e.isSolved());
    }

    @Test
    final void testGetC() {
        double x = 2;
        EquazioneSecondoGradoModificabileConRisolutore e = new EquazioneSecondoGradoModificabileConRisolutore(1,6, x);
        assertTrue(x == e.getC());
    }

    @Test
    final void testSetC() {
        double x = 1;
        EquazioneSecondoGradoModificabileConRisolutore e = new EquazioneSecondoGradoModificabileConRisolutore(2,9,7);
        e.setC(x);
        assertTrue(x == e.getC());
        // Avendo modificato un parametro l'equazione non dovrebbe essere risolta, quindi
        // isSolved() dovrebbe restituire false e getSolution() lanciare un'eccezione
        assertFalse(e.isSolved());
        assertThrows(IllegalStateException.class,
                () -> e.getSolution());
        // Dopo esser stata risolta nuovamente, allora isSolved() deve restituire true
        e.solve();
        assertTrue(e.isSolved());
    }

    @Test
    final void testIsSolved() {
        double x = 9;
        EquazioneSecondoGradoModificabileConRisolutore e = new EquazioneSecondoGradoModificabileConRisolutore(3,5,6);
        // Solo dopo aver eseguito solve() il metodo isSolved() deve restituire
        // true, altrimenti restituisce false
        assertFalse(e.isSolved());
        e.solve();
        assertTrue(e.isSolved());
        // Anche quando cambiamo un parametro, affinché isSolved restituisca true
        // dobbiamo prima rieseguire solve(), altrimenti restituisce false
        e.setA(x);
        assertFalse(e.isSolved());
        e.solve();
        assertTrue(e.isSolved());

    }

    @Test
    final void testSolve() {
        EquazioneSecondoGradoModificabileConRisolutore e3 = new EquazioneSecondoGradoModificabileConRisolutore(
                1, 1, 3);
        // controllo semplicemente che la chiamata a solve() non generi errori
        e3.solve();
        // i test con i valori delle soluzioni vanno fatti nel test del metodo
        // getSolution()
    }

    @Test
    final void testGetSolution() {
        EquazioneSecondoGradoModificabileConRisolutore e = new EquazioneSecondoGradoModificabileConRisolutore(1,2,9);
        // CASO 1 : DELTA < 0
        assertThrows(IllegalStateException.class,
                () -> e.getSolution());
        e.solve();
        assertEquals(e.getSolution(), new SoluzioneEquazioneSecondoGrado(new EquazioneSecondoGrado(e.getA(), e.getB(), e.getC())));

        // CASO 2 : DELTA = 0
        e.setC(1);
        assertThrows(IllegalStateException.class,
                () -> e.getSolution());
        e.solve();
        assertEquals(e.getSolution(), new SoluzioneEquazioneSecondoGrado(new EquazioneSecondoGrado(e.getA(), e.getB(), e.getC()),
                                                                                                - e.getB() / (2 * e.getA())));
        // CASO 3 : DELTA > 0
        e.setB(5);
        assertThrows(IllegalStateException.class,
                () -> e.getSolution());
        e.solve();
        double delta = e.getB() * e.getB() - (4 * e.getA() * e.getC());
        assertEquals(e.getSolution(), new SoluzioneEquazioneSecondoGrado(new EquazioneSecondoGrado(e.getA(), e.getB(), e.getC()),
                (- e.getB() + Math.sqrt(delta)) / (2 * e.getA()), (- e.getB() - Math.sqrt(delta)) / (2 * e.getA())));

    }

}
