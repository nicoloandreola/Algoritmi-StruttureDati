package it.unicam.cs.asdl2526.es11;

/**
 * Un oggetto di questa classe è un risolutore del problema della più lunga
 * sottosequenza comune tra due stringhe date.
 * 
 * @author Template: Luca Tesei, Implementation: Collective
 *
 */
public class LCSSolver {

    /* Prima stringa */
    private final String x;

    /* Seconda stringa */
    private final String y;

    /* Matrice per il calcolo di una soluzione ottima */
    private int[][] m;

    /*
     * Flag che indica che questo solver ha svolto il proprio calcolo. Alla
     * creazione del solver non viene svolto il calcolo. Esso viene eseguito
     * successivamente alla chiamata del metodo solve()
     */
    private boolean isSolved;

    /**
     * Costruisce un risolutore LCS fra due stringhe date.
     * 
     * @param x
     *              la prima stringa
     * @param y
     *              la seconda stringa
     * @throws NullPointerException
     *                                  se almeno una delle due stringhe passate
     *                                  è nulla
     */
    public LCSSolver(String x, String y) {
        if (x == null || y == null)
            throw new NullPointerException("Creazione di un solver con una o due stringhe null");
        this.x = x;
        this.y = y;
        // creo la matrice
        this.m = new int[this.x.length() + 1][this.y.length() + 1];
        this.isSolved = false;
    }

    /**
     * @return the string x
     */
    public String getX() {
        return x;
    }

    /**
     * @return the string y
     */
    public String getY() {
        return y;
    }

    /**
     * Risolve il problema LCS delle due stringhe di questo solver, se non è
     * stato già risolto precedentemente. Dopo l'esecuzione di questo metodo la
     * prima volta il problema verrà considerato risolto.
     */
    public void solve() {
        // Se è stato gia risolto, non devo far nulla
        if(this.isSolved)
            return;

        // Inizializzo con tutti zeri la prima riga e la prima
        // colonna (posso anche non farlo visto che java inizializza
        // tutte le caselle a zero di default)
        for(int i = 0; i <= this.x.length(); i++)
            this.m[i][0] = 0;
        for(int j = 0; j <= this.y.length(); j++)
            this.m[0][j] = 0;

        // m.length sarebbe x.length() + 1, cioè il numero di righe
        // (potevo anche usare <= x.length() e <= y.length() come sopra)
        for(int i = 1; i < this.m.length; i++) {
            // m[i].length è invece la lunghezza di una riga,
            // cioè il numero di colonne (y.length() + 1)
            for(int j = 1; j < this.m[i].length; j++) {
                // Se Xi = Yj, assegno alla casella m[i][j]
                // il valore di quella in diagonale + 1
                if(this.x.charAt(i - 1) == this.y.charAt(j - 1))
                    this.m[i][j] = this.m[i - 1][j - 1] + 1;
                else // Se invece Xi != Yj assegno alla casella
                // m[i][j] il massimo tra quella sopra e quella a sinistra
                    this.m[i][j] = Math.max(this.m[i - 1][j], this.m[i][j - 1]);
            }
        }
        // Una volta riempita la matrice, cambio il valore del FLAG così
        // da non dover risolvere il problema nuovamente
        this.isSolved = true;
    }

    /**
     * Determina se questo solver ha già risolto il problema.
     * 
     * @return true se il problema LCS di questo solver è già stato risolto
     *         precedentemente, false altrimenti
     */
    public boolean isSolved() {
        return this.isSolved;
    }

    /**
     * Determina la lunghezza massima delle sotto sequenze comuni.
     * 
     * @return la massima lunghezza delle sotto sequenze comuni di x e y.
     * @throws IllegalStateException
     *                                   se il solver non ha ancora risolto il
     *                                   problema LCS
     */
    public int getLengthOfSolution() {
        if (!this.isSolved)
            throw new IllegalStateException(
                    "Richiesta delle soluzioni prima della risoluzione del problema");
        return this.m[this.x.length()][this.y.length()];
    }

    /**
     * Restituisce una soluzione del problema LCS.
     * 
     * @return una sotto sequenza di this.x e this.y di lunghezza massima
     * @throws IllegalStateException
     *                                   se il solver non ha ancora risolto il
     *                                   problema LCS
     */
    public String getOneSolution() {
        if (!this.isSolved)
            throw new IllegalStateException(
                    "Richiesta delle soluzioni prima della risoluzione del problema");
        return traceBack(this.x.length(), this.y.length());
    }

    /*
     * NOTA: Determina una soluzione ottime ripercorrendo le caselle della
     * matrice m seguendo le condizioni con cui sono state costruite. In questo
     * caso non deve venire usata una matrice di "supporto" per ricostruire una
     * soluzione ottima, ma va usata la stessa matrice m
     */
    private String traceBack(int i, int j) {
        // CASO BASE (quando arrivo nei bordi)
        if(i == 0 || j == 0)
            return "";
        // CASO RICORSIVO
        // Se Xi è uguale a Yj significa che il carattere appartiene
        // alla LCS quindi richiamo il metodo sulle sotto stringhe di x e y,
        // aggiungendo però in fondo il carattere in questione
        // (indifferente se metto quello di x o di y tanto sono lo stesso)
        if(this.x.charAt(i - 1) == this.y.charAt(j - 1))
            return traceBack(i - 1, j - 1) + this.x.charAt(i - 1);
        // Altrimenti dobbiamo richiamare il metodo spostandoci
        // sulla casella che contiene il valore maggiore tra quella sopra
        // e quella a sx, senza aggiungere nessun carattere al risultato
        else if(this.m[i][j - 1] > this.m[i - 1][j])
            return traceBack(i, j - 1);
        else // Visto che quando riempiamo la matrice, in caso di parità
        // (cioè quando m[i][j-1] = m[i-1][j]), prendiamo m[i-1][j],
        // anche nel TRACEBACK dobbiamo proseguire andando in m[i-1][j]
        // (questo perché il metodo MAX della classe MATH restituisce
        // il primo argomento che gli viene passato in caso i due siano
        // uguali e infatti è implementata così: return (a >= b) ? a : b;)
            return traceBack(i- 1, j);
    }

    /**
     * Determina se una certa stringa è una sotto sequenza comune delle due
     * stringhe di questo solver.
     * 
     * @param z
     *              la string da controllare
     * @return true se z è sotto sequenza di this.x e di this.y, false altrimenti
     * @throws NullPointerException
     *                                  se z è null
     */
    public boolean isCommonSubsequence(String z) {
        if (z == null)
            throw new NullPointerException("Test di una sequenza nulla");
        return isSubsequence(z, this.x) && isSubsequence(z, this.y);
    }

    /*
     * Determina se una stringa è sotto sequenza di un'altra stringa.
     * 
     * @param z la stringa da testare
     * 
     * @param w la stringa di cui z dovrebbe essere sotto sequenza
     * 
     * @return true se z è sotto sequenza di w, false altrimenti
     */
    private static boolean isSubsequence(String z, String w) {
        // CASO BASE
        if(z.isEmpty())
            return true;
        // CASO RICORSIVO
        // Considero il primo carattere di z
        char first = z.charAt(0);
        // Cerchiamo la prima occorrenza di first in w (per
        // farlo usiamo il metodo indexOf della classe String)
        int i = w.indexOf(first);
        // Per controllare se first è presente almeno una volta
        // in w basta vedere se i è diverso da -1 poiché il metodo
        // indexOf di String è implementato in modo da restituire
        // -1 se il carattere passato non c'è nella stringa
        if(i == -1)
            // Se non è presente significa che z non è una
            // sotto sequenza di w, quindi ritorno false
            return false;
        // Altrimenti richiamo ricorsivamente il metodo sulla
        // stringa z priva del primo elemento e sulla sotto stringa
        // di w che parte da i+1 (non posso riconsiderare tutta la
        // stringa w poiché magari la prima occorrenza del secondo
        // elemento di z viene prima della prima occorrenza del primo,
        // ma, affinché sia una sotto sequenza valida, l'ordine degli
        // elementi deve essere rispettato: quindi la parte di w che
        // precede i non va più considerata)
        return isSubsequence(z.substring(1),w.substring(i + 1));
    }
}
