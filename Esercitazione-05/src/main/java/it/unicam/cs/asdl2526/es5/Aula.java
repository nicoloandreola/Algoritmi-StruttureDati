package it.unicam.cs.asdl2526.es5;

import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Iterator;


/**
 * Un oggetto della classe aula rappresenta una certa aula con le sue facilities
 * e le sue prenotazioni.
 * 
 * @author Template: Luca Tesei, Implementazione: Collettiva
 *
 */
public class Aula implements Comparable<Aula> {
    // Identificativo unico di un'aula
    private final String nome;

    // Location dell'aula
    private final String location;

    // Insieme delle facilities di quest'aula
    private final Set<Facility> facilities;

    // Insieme delle prenotazioni per quest'aula, segue l'ordinamento naturale
    // delle prenotazioni
    private final SortedSet<Prenotazione> prenotazioni;

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
            throw new NullPointerException("Il NOME dell'aula non può essere NULLO!");
        if(location == null)
            throw new NullPointerException("La LOCATION dell'aula non può essere NULLA!");
        this.nome = nome;
        this.location = location;
        this.facilities = new HashSet<Facility>();
        this.prenotazioni = new TreeSet<Prenotazione>();
    }

    /**
     * Costruisce una certa aula con nome, location e insieme delle facilities.
     * L'aula non ha inizialmente nessuna prenotazione.
     * 
     * @param nome
     *                       il nome dell'aula
     * @param location
     *                       la location dell'aula
     * @param facilities
     *                       l'insieme delle facilities dell'aula
     * @throws NullPointerException
     *                                  se una qualsiasi delle informazioni
     *                                  richieste è nulla
     */
    public Aula(String nome, String location, Set<Facility> facilities) {
        if(nome == null)
            throw new NullPointerException("Il NOME dell'aula non può essere NULLO!");
        if(location == null)
            throw new NullPointerException("La LOCATION dell'aula non può essere NULLA!");
        if(facilities == null)
            throw new NullPointerException("L'insieme di FACILITIES non può essere NULL!");
        this.nome = nome;
        this.location = location;
        this.facilities = facilities;
        this.prenotazioni = new TreeSet<Prenotazione>();
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
        if(o == null)
            throw new NullPointerException("Parametro NON valido!");
        return this.nome.compareTo(o.nome);
    }

    /**
     * @return the facilities
     */
    public Set<Facility> getFacilities() {
        return facilities;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return the prenotazioni
     */
    public SortedSet<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    /**
     * Aggiunge una facility a questa aula.
     * 
     * @param f
     *              la facility da aggiungere
     * @return true se la facility non era già presente e quindi è stata
     *         aggiunta, false altrimenti
     * @throws NullPointerException
     *                                  se la facility passata è nulla
     */
    public boolean addFacility(Facility f) {
        if(f == null)
            throw new NullPointerException("Parametro NON valido!");
        // Come specificato nell'API di HashSet, metodo add aggiunge l'elemento
        // e restituisce true se e solo se non è già presente, quindi non serve
        // fare alcun controllo: basta invocare il metodo add su this.facilities
        return this.facilities.add(f);
    }

    /**
     * Determina se l'aula è libera in un certo time slot.
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
        /*
         * NOTA: sfruttare l'ordinamento tra le prenotazioni per rispondere in
         * maniera efficiente: poiché le prenotazioni sono in ordine crescente
         * di time slot se arrivo a una prenotazione che segue il time slot
         * specificato posso concludere che l'aula è libera nel time slot desiderato
         * e posso interrompere la ricerca (non ho più un T(n) ma un O(n))
         */
        if(ts == null)
            throw new NullPointerException("Parametro NON valido!");
        // Per scorrere tutte le prenotazioni utilizzo un FOREACH
        for(Prenotazione p : this.prenotazioni) {
            // Se incontro una prenotazione che ha un ts che si sovrappone
            // con quello passato. PRIMA DI ARRIVARE A UNA CHE LO SEGUE,
            // devo restituire FALSE in quanto significa che non è libera
            if (p.getTimeSlot().overlapsWith(ts))
                return false;
            // Altrimenti, se arrivo a una prenotazione che segue il time slot
            // specificato, senza averne trovata nessuna prima con cui si
            // sovrappone, posso concludere che l'aula è libera e restituire TRUE
            // poiché le prenotazioni sono in ordine crescente di time slot e
            // quindi sicuramente dopo non ne troverò nessuno che si sovrappone
            if (p.getTimeSlot().compareTo(ts) > 0)
                return true;
        }
        // Se ho terminato le prenotazioni e nessuna si è sovrapposta significa
        return true; // che è libera
    }

    /**
     * Determina se questa aula soddisfa tutte le facilities richieste
     * rappresentate da un certo insieme dato.
     * 
     * @param requestedFacilities
     *                                l'insieme di facilities richieste da
     *                                soddisfare
     * @return true se e solo se tutte le facilities di
     *         {@code requestedFacilities} sono soddisfatte da questa aula.
     * @throws NullPointerException
     *                                  se il set di facility richieste è nullo
     */
    public boolean satisfiesFacilities(Set<Facility> requestedFacilities) {
        // Riscritto codice ES4 con FOREACH al posto di FOR CLASSICI
        if(requestedFacilities == null)
            throw new NullPointerException("Parametro NON valido!");
        boolean allNull = true;
        for(Facility rf : requestedFacilities) {
            boolean found = false;
            if(rf != null)
                allNull = false;
            for(Facility f : this.facilities)
                // Al posto di usare la condizione !found per uscire dal ciclo
                // utilizzando un FOREACH posso usare il comando break che
                // permette di uscire subito dal ciclo più interno senza continuare
                // a scorrere il set (tanto ormai la facility la ho trovata)
                    if (f.satisfies(rf)) {
                        found = true;
                        break;
                    }
            if(!allNull && !found)
                return false;
        }
        return true;
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
        // Se isFree(ts) restituisce false significa che c'è una sovrapposizione
        if(!this.isFree(ts))
            throw new IllegalArgumentException("Prenotazione NON disponibile!");
        // Se libera, creo la prenotazione e la aggiungo al set
        this.prenotazioni.add(new Prenotazione(this, ts, docente, motivo));
    }

    /**
     * Cancella una prenotazione di questa aula.
     * 
     * @param p
     *              la prenotazione da cancellare
     * @return true se la prenotazione è stata cancellata, false se non era
     *         presente.
     * @throws NullPointerException
     *                                  se la prenotazione passata è null
     */
    public boolean removePrenotazione(Prenotazione p) {
        if(p == null)
            throw new NullPointerException("Parametro NON valido!");
        // Come per addFacility, posso utilizzare direttamente il metodo
        // della classe di this.prenotazioni (TreeSet in questo caso)
        return this.prenotazioni.remove(p);
    }

    /**
     * Rimuove tutte le prenotazioni di questa aula che iniziano prima (o
     * esattamente in) di un punto nel tempo specificato.
     * 
     * @param timePoint
     *                      un certo punto nel tempo
     * @return true se almeno una prenotazione è stata cancellata, false
     *         altrimenti.
     * @throws NullPointerException
     *                                  se il punto nel tempo passato è nullo.
     */
    public boolean removePrenotazioniBefore(GregorianCalendar timePoint) {
        /*
         * NOTA: sfruttare l'ordinamento tra le prenotazioni per rispondere in
         * maniera efficiente: poiché le prenotazioni sono in ordine crescente
         * di time slot se ho raggiunto una prenotazione con tempo di inizio
         * maggiore del tempo indicato posso smettere la procedura
         */
        if(timePoint == null)
            throw new NullPointerException("Parametro NON valido!");
        // uso un flag per determinare se sono state fatte cancellazioni
        boolean removed = false;
        // scorro le prenotazioni nell'ordine canonico che è in base al tempo di
        // inizio usando un iterator perché devo cancellare alcune prenotazioni
        // durante lo scorrimento (con un FOREACH verrebbe lanciata un'eccezione)
        Iterator<Prenotazione> i = this.prenotazioni.iterator();
        while (i.hasNext()) {
            Prenotazione p = i.next();
            int cmp = timePoint.compareTo(p.getTimeSlot().getStart());
            if (cmp >= 0) {
                // se timePoint segue o è uguale a p.getTimeSlot().getStart()
                // allora significa che la prenotazione è da cancellare
                i.remove();
                removed = true;
            } else // altrimenti (cmp < 0) significa che ho raggiunto una prenotazione con tempo
                // di inizio maggiore del tempo indicato e quindi posso smettere la procedura.
                break;
        }
        return removed; // una volta uscito dal while ritorno il flag: se almeno
        // una prenotazione è stata rimossa allora questo sarà true, altrimenti false
    }
}
