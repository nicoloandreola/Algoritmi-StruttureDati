package it.unicam.cs.asdl2526.es4;

/**
 * Un oggetto della classe aula rappresenta una certa aula con le sue facilities
 * e le sue prenotazioni.
 * 
 * @author Template: Luca Tesei, Implementation: Collective
 *
 */
public class Aula implements Comparable<Aula> {

    /*
     * Numero iniziale delle posizioni dell array facilities. Se viene richiesto
     * di inserire una facility e l array è pieno questo viene raddoppiato. La
     * costante è protected solo per consentirne l'accesso ai "test JUnit"
     */
    protected static final int INIT_NUM_FACILITIES = 5;

    /*
     * Numero iniziale delle posizioni dell array prenotazioni. Se viene
     * richiesto di inserire una prenotazione e l array è pieno questo viene
     * raddoppiato. La costante è protected solo per consentirne l'accesso ai
     * "test JUnit".
     */
    protected static final int INIT_NUM_PRENOTAZIONI = 100;

    // Identificativo unico di un'aula
    private final String nome;

    // Location dell'aula
    private final String location;

    /*
     * Insieme delle facilities di quest'aula. L array viene creato all'inizio
     * della dimensione specificata nella costante INIT_NUM_FACILITIES. Il
     * metodo addFacility(Facility) raddoppia l array qualora non ci sia più
     * spazio per inserire la facility.
     */
    private Facility[] facilities;

    // numero corrente di facilities inserite
    private int numFacilities;

    /*
     * Insieme delle prenotazioni per quest'aula. L array viene creato
     * all'inizio della dimensione specificata nella costante
     * INIT_NUM_PRENOTAZIONI. Il metodo addPrenotazione(TimeSlot, String,
     * String) raddoppia l array qualora non ci sia più spazio per inserire la
     * prenotazione.
     */
    private Prenotazione[] prenotazioni;

    // numero corrente di prenotazioni inserite
    private int numPrenotazioni;

    /**
     * Costruisce una certa aula con nome e location. Il set delle facilities è
     * vuoto. L'aula non ha inizialmente nessuna prenotazione.
     * 
     * @param nome
     *                     il nome dell'aula
     * @param location
     *                     la location dell'aula
     * 
     * @throws NullPointerException
     *                                  se una qualsiasi delle informazioni
     *                                  richieste è nulla
     */
    public Aula(String nome, String location) {
        if(nome == null)
            throw new NullPointerException("Il nome NON può essere NULLO!");
        if(location == null)
            throw new NullPointerException("La location NON può essere NULLA!");
        this.nome = nome;
        this.location = location;
        this.facilities = new Facility[INIT_NUM_FACILITIES];
        this.numFacilities = 0;
        this.prenotazioni = new Prenotazione[INIT_NUM_PRENOTAZIONI];
        this.numPrenotazioni = 0;
    }

    /*
     * Ridefinire in accordo con equals
     */
    @Override
    public int hashCode() {
        return this.nome.hashCode();
    }

    /* Due aule sono uguali se e solo se hanno lo stesso nome */
    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(!(obj instanceof Aula))
            return false;
        Aula other = (Aula) obj;
        return this.nome.equals(other.nome);
    }

    /* L'ordinamento naturale si basa sul nome dell'aula */
    @Override
    public int compareTo(Aula o) {
        return this.nome.compareTo(o.nome);
    }

    /**
     * @return the facilities
     */
    public Facility[] getFacilities() {
        return this.facilities;
    }

    /**
     * @return il numero corrente di facilities
     */
    public int getNumeroFacilities() {
        return this.numFacilities;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * @return the prenotazioni
     */
    public Prenotazione[] getPrenotazioni() {
        return this.prenotazioni;
    }

    /**
     * @return il numero corrente di prenotazioni
     */
    public int getNumeroPrenotazioni() {
        return this.numPrenotazioni;
    }

    /**
     * Aggiunge una facility a questa aula. Controlla se la facility è già
     * presente, nel qual caso non la inserisce.
     * 
     * @param f
     *              la facility da aggiungere
     * @return true se la facility non era già presente e quindi è stata
     *         aggiunta, false altrimenti
     * @throws NullPointerException
     *                                  se la facility passata è nulla
     */
    public boolean addFacility(Facility f) {
        /*
         * Nota: attenzione! Per controllare se una facility è già presente
         * bisogna usare il metodo equals della classe Facility.
         * 
         * Nota: attenzione bis! Si noti che per le sottoclassi di Facility non
         * è richiesto di ridefinire ulteriormente il metodo equals...
         */
        if(f == null)
            throw new NullPointerException("Parametro NON valido!");
        // Controllo se la facility è già presente
        for(int i = 0; i < this.numFacilities; i++) {
            if (this.facilities[i].equals(f))
                return false;
        }
        // essendo il raddoppio necessario quando l array è pieno quest'ultimo
        // dovrebbe avvenire quando il numero di elementi attuale (numFacilities)
        // e la lunghezza dell array (facilities.length) coincidono.
        if(this.numFacilities == this.facilities.length)
            this.facilities = (Facility[]) doubleArray(this.facilities);
        // dopo aver controllato se c'è spazio, ed eventualmente dopo averlo
        // aggiunto con il metodo privato, inserisco la nuova facility
        this.facilities[numFacilities++] = f;
        return true;
    }

    /**
     * Determina se l'aula è libera in un certo time slot.
     * 
     * 
     * @param ts
     *               il time slot da controllare
     * 
     * @return true se l'aula risulta libera per tutto il periodo del time slot
     *         specificato
     * @throws NullPointerException
     *                                  se il time slot passato è nullo
     */
    public boolean isFree(TimeSlot ts) {
        if(ts == null)
            throw new NullPointerException("Parametro NON valido!");
        for(int i = 0; i < numPrenotazioni; i++) {
            if (this.prenotazioni[i].getTimeSlot().overlapsWith(ts))
                return false;
        }
        return true;
    }

    /**
     * Determina se questa aula soddisfa tutte le facilities richieste
     * rappresentate da un certo insieme dato.
     * 
     * @param requestedFacilities
     *                                l'insieme di facilities richieste da
     *                                soddisfare, sono da considerare solo le
     *                                posizioni diverse da null
     * @return true se e solo se tutte le facilities di
     *         {@code requestedFacilities} sono soddisfatte da questa aula.
     * @throws NullPointerException
     *                                  se il set di facility richieste è nullo
     */
    public boolean satisfiesFacilities(Facility[] requestedFacilities) {
        if(requestedFacilities == null)
            throw new NullPointerException("Parametro NON valido!");
        boolean allNull = true; // flag per verificare se tutte posizioni dell array passato
        // sono NULL e quindi restituire true
        for(int i = 0; i < requestedFacilities.length; i++) {
            boolean found = false; // flag per contrassegnare quando una facility è soddisfatta.
            // Va definito fuori dal ciclo interno ma dentro quello esterno, poiché altrimenti
            // dopo che diventa true per la prima volta, rimane true per sempre (non viene
            // riportato a false all'inizio delle iterazioni successive)
            if(requestedFacilities[i] != null) { // vanno controllate solo le posizioni non NULLE
                allNull = false;
                for (int j = 0; (j < numFacilities && !found); j++)
                    if (this.facilities[j].satisfies(requestedFacilities[i]))
                        found = true;
            }
            if(!allNull && !found) // se found è ancora false, significa che nessuna
                return false; // facility soddisfa requestedFacilities[i].
            // Devo aggiungere al controllo anche allNull poiché se requestedFacilities[i]
            // è uguale a null, found rimane false visto che non si entra per niente nel
            // ciclo interno, e quindi il metodo restituirebbe false; ma come descritto
            // nell'API, le posizioni NULLE vanno IGNORATE
        }
        return true; // se arrivo qua significa che tutte le facilities sono soddisfatte
    }

    /**
     * Prenota l'aula controllando eventuali sovrapposizioni.
     * 
     * @param ts
     * @param docente
     * @param motivo
     * @throws IllegalArgumentException
     *                                      se la prenotazione comporta una
     *                                      sovrapposizione con un'altra
     *                                      prenotazione nella stessa aula.
     * @throws NullPointerException
     *                                      se una qualsiasi delle informazioni
     *                                      richieste è nulla.
     */
    public void addPrenotazione(TimeSlot ts, String docente, String motivo) {
        if (ts == null)
            throw new NullPointerException("Tentativo di costruire una prenotazione senza time slot");
        if (docente == null)
            throw new NullPointerException("Tentativo di costruire una prenotazione senza docente");
        if (motivo == null)
            throw new NullPointerException("Tentativo di costruire una prenotazione senza motivo");
        if(!this.isFree(ts))
            throw new IllegalArgumentException("Prenotazione NON disponibile a causa di SOVRAPPOSIZIONE!");
        // essendo il raddoppio necessario quando l array è pieno quest'ultimo
        // dovrebbe avvenire quando il numero di elementi attuale (numPrenotazioni)
        // e la lunghezza dell array (prenotazioni.length) coincidono.
        if(this.numPrenotazioni == this.prenotazioni.length)
            this.prenotazioni = (Prenotazione[]) doubleArray(this.prenotazioni);
        // dopo aver controllato se c'è spazio, ed eventualmente dopo averlo
        // aggiunto con il metodo privato, creo la nuova prenotazione e la inserisco
        this.prenotazioni[this.numPrenotazioni++] = new Prenotazione(this, ts, docente, motivo);
    }

    // Metodo per raddoppiare un array (di tipo Object poiché mi serve
    // per raddoppiare sia l array di prenotazioni che quello di facilities,
    // e questi due tipi classe non stanno in relazione tra loro)

    private Object[] doubleArray(Object[] vecchio) {
        if(vecchio instanceof Facility[]) {
            Facility[] nuovo = new Facility[vecchio.length * 2];
            for(int i = 0; i < vecchio.length; i++)
                nuovo[i] = (Facility) vecchio[i];
            return nuovo;
        }
        if(vecchio instanceof Prenotazione[]) {
            Prenotazione[] nuovo = new Prenotazione[vecchio.length * 2];
            for(int i = 0; i < vecchio.length; i++)
                nuovo[i] = (Prenotazione) vecchio[i];
            return nuovo;
        }
        return null;
    // Array struttura dati dalla dimensione fissa, una volta creato non si
    // può cambiare la sua lunghezza: per poterlo allungare bisogna crearne
    // uno nuovo dalla dimensione maggiore e ricopiarci sopra quello che c’era
    }
}
