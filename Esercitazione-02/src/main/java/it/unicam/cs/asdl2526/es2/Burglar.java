package it.unicam.cs.asdl2526.es2;

/**
 * Uno scassinatore è un oggetto che prende una certa cassaforte e trova la
 * combinazione utilizzando la "forza bruta".
 * 
 * @author Luca Tesei
 *
 */
public class Burglar {

    private final CombinationLock cassaforte;
    private int attempts;

    /**
     * Costruisce uno scassinatore per una certa cassaforte.
     * 
     * @param aCombinationLock la cassaforte da scassinare
     * @throws NullPointerException se la cassaforte passata è nulla
     */
    public Burglar(CombinationLock aCombinationLock) {
        if(aCombinationLock == null)
            throw new NullPointerException("La cassaforte NON può essere NULLA!");
        this.cassaforte = aCombinationLock;
        this.attempts = -1;
        // Inizializzo i tentativi a -1 in modo da semplificare
        // poi il metodo getAttempts() (basta un solo return)
    }

    /**
     * Forza la cassaforte e restituisce la combinazione.
     * 
     * @return la combinazione della cassaforte forzata.
     */
    public String findCombination() {
        this.cassaforte.lock();
        this.attempts = 0;
        for (char c1 = 'A'; c1 <= 'Z'; c1++)
            for (char c2 = 'A'; c2 <= 'Z'; c2++)
                for (char c3 = 'A'; c3 <= 'Z'; c3++) {
                    attempts++;
                    this.cassaforte.setPosition(c1);
                    this.cassaforte.setPosition(c2);
                    this.cassaforte.setPosition(c3);
                    this.cassaforte.open();
                    if (this.cassaforte.isOpen()) {
                        StringBuilder s = new StringBuilder();
                        s.append(c1);
                        s.append(c2);
                        s.append(c3);
                        return s.toString();
                    }
                }
        return null;
    }

    /**
     * Restituisce il numero di tentativi che ci sono voluti per trovare la
     * combinazione. Se la cassaforte non è stata ancora forzata restituisce -1.
     * 
     * @return il numero di tentativi che ci sono voluti per trovare la
     *         combinazione, oppure -1 se la cassaforte non è stata ancora
     *         forzata.
     */
    public long getAttempts() {
        return this.attempts;
    }
}
