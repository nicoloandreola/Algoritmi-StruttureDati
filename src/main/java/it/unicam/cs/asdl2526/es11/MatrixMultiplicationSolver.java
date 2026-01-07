package it.unicam.cs.asdl2526.es11;

import java.util.List;

/**
 * Un solver prende una certa sequenza di matrici da moltiplicare e calcola una
 * parentesizzazione ottima, cioè che minimizza il numero di moltiplicazioni
 * scalari necessarie per moltiplicare tutte le matrici.
 * 
 * @author Template: Luca Tesei, Implementation: Collective
 *
 */
public class MatrixMultiplicationSolver {

    // sequenza delle dimensioni delle matrici da moltiplicare
    private List<Integer> p;

    // matrice dei costi minimi
    private int[][] m;

    // matrice delle scelte dei k che corrispondono al costo minimo
    private int[][] b;

    /**
     * Costruisce un solver per una certa sequenza di matrici da moltiplicare,
     * date le loro dimensioni righeXcolonne. Il calcolo della soluzione ottima
     * viene eseguito subito, cioè come parte di questo costruttore.
     * 
     * @param p
     *              è una lista di valori che sono le dimensioni delle matrici,
     *              ad esempio se p = [10, 100, 5, 50] allora sto moltiplicando
     *              3 matrici (p.size() - 1) le cui dimensioni sono A_{0} =
     *              10x100, A_{1} = 100x5, A_{2} = 5x50
     * @throws NullPointerException
     *                                      se la lista passata è null
     * @throws IllegalArgumentException
     *                                      se la lista p contiene meno di due
     *                                      elementi (cioè deve contenere almeno
     *                                      una matrice. Nel caso di una unica
     *                                      matrice la soluzione è 0 e la
     *                                      parentesizzazione è la matrice
     *                                      stessa, cioè "A_{0}")
     */
    public MatrixMultiplicationSolver(List<Integer> p) {
        if (p == null)
            throw new NullPointerException("Lista nulla");
        if (p.size() <= 1)
            throw new IllegalArgumentException("Lista di dimensione non valida");
        this.p = p;
        this.m = new int[p.size() - 1][p.size() - 1];
        this.b = new int[p.size() - 1][p.size() - 1];
        this.solve();
    }

    /*
     * Risolve il problema della parentesizzazione ottima con la programmazione
     * dinamica.
     */
    private void solve() {
        // CASO BASE (riempo la diagonale principale con tutti 0)
        for(int i = 0; i < this.m.length; i++)
            m[i][i] = 0;

        // CASO RICORSIVO
        // Primo ciclo deve scorrere tutte le diagonali (partiamo
        // dalla seconda perché la prima è quella principale ed
        // è stata già riempita, perciò d = 1 e non d = 0)

        for(int d = 1; d < this.m.length; d++) {
            // Al suo interno scorriamo tutte le caselle della diagonale
            // corrente, partendo sempre ovviamente dalla riga 0 e arrivando
            // a this.m.length - d - 1 (i sarebbe quindi l'indice di RIGA)

            for(int i = 0; i < this.m.length - d; i++) {
                // Determiniamo l'indice di COLONNA
                int j = i + d;

                // Inizializziamo la casella corrente m[i][j] con il numero
                // più grande possibile, in modo che il primo valore calcolato
                // al variare di k sarà sicuramente MINORE di quello già esistente
                // e verrà dunque subito inserito (poi si confrontano gli altri)

                this.m[i][j] = Integer.MAX_VALUE; // VALORE DUMMY
                // (valore “fittizio”, di comodo, messo solo per inizializzare una
                // variabile, non perché rappresenti un risultato valido del problema)

                // Calcoliamo per ogni casella m[i][j] la ricorrenza
                // per tutti i valori di k compresi tra i e j - 1 e
                // determiniamo quella che porta al risultato MINIMO
                for(int k = i; k < j; k++) {
                    int result = this.m[i][k] + this.m[k + 1][j] +
                            (this.p.get(i) * this.p.get(k + 1) * this.p.get(j + 1));
                    // Confrontiamo nuovo valore con quello più piccolo finora
                    if(result < this.m[i][j]) {
                        // Se è minore significa che RESULT corrisponde al
                        // minimo attuale quindi aggiorniamo m[i][j]
                        this.m[i][j] = result;
                        // Aggiorniamo anche b[i][j] con il k corrente
                        this.b[i][j] = k;
                    }
                }
            }
        }
    }

    /**
     * Restituisce il numero minimo di moltiplicazioni necessarie per
     * moltiplicare la sequenza di matrici di questo solver. Nel caso di una
     * sola matrice restituisce zero.
     * 
     * @return il numero minimo di moltiplicazioni soluzione del problema di
     *         parentesizzazione
     */
    public int getOptimalCost() {
        return m[0][p.size() - 2];
    }

    /**
     * Restituisce una parentesizzazione ottima.
     * 
     * Il formato prevede l'uso di "A_{i}" per indicare la i-esima matrice di
     * dimensione p.get(i) x p.get(i+1) con 0 <= i <= p.size() - 2. Ad esempio
     * la parentesizzazione con una sola matrice deve restituire "A_{0}", la
     * parentesizzazione con due matrici deve restituire "(A_{0} x A_{1})", la
     * parentesizzazione con tre matrici deve restituire "((A_{0} x A_{1}) x
     * A_{2})" oppure "(A_{0} x (A_{1} x A_{2}))" e così via.
     * 
     * @return una parentesizzazione ottima
     */
    public String getOptimalParenthesization() {
        return traceBack(0, p.size() - 2);
    }

    /*
     * Effettua il traceback utilizzando la matrice b che è stata riempita
     * appositamente durante il processo di calcolo del costo minimo
     */
    private String traceBack(int i, int j) {
        StringBuilder s = new StringBuilder();
        // CASO BASE (una sola matrice)
        if(i == j) {
            s.append("A_{").append(i).append("}");
            return s.toString();
        }
        // CASO RICORSIVO (almeno 2 matrici)
        // Estraiamo il k dalla casella i, j della matrice b
        int k = this.b[i][j];
        // Chiamiamo ricorsivamente il metodo sulle caselle
        // i, k e k + 1, j finché non arriviamo al caso base
        s.append("(").append(this.traceBack(i, k)).append(" x ").append(this.traceBack(k+1, j)).append(")");
        return s.toString();
    }

}
