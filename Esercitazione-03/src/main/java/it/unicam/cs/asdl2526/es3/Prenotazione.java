package it.unicam.cs.asdl2526.es3;

/**
 * Una prenotazione riguarda una certa aula per un certo time slot.
 * 
 * @author Luca Tesei
 *
 */
public class Prenotazione implements Comparable<Prenotazione> {

    private final String aula;

    private final TimeSlot timeSlot;

    private String docente;

    private String motivo;

    /**
     * Costruisce una prenotazione.
     * 
     * @param aula
     *                     l'aula a cui la prenotazione si riferisce
     * @param timeSlot
     *                     il time slot della prenotazione
     * @param docente
     *                     il nome del docente che ha prenotato l'aula
     * @param motivo
     *                     il motivo della prenotazione
     * @throws NullPointerException
     *                                  se uno qualsiasi degli oggetti passati è
     *                                  null
     */
    public Prenotazione(String aula, TimeSlot timeSlot, String docente,
            String motivo) {
        if (aula == null)
            throw new NullPointerException("Tentativo di costruire una prenotazione senza aula");
        if (timeSlot == null)
            throw new NullPointerException("Tentativo di costruire una prenotazione senza time slot");
        if (docente == null)
            throw new NullPointerException("Tentativo di costruire una prenotazione senza docente");
        if (motivo == null)
            throw new NullPointerException("Tentativo di costruire una prenotazione senza motivo");
        this.docente = docente;
        this.motivo = motivo;
        this.aula = aula;
        this.timeSlot = timeSlot;
    }

    /**
     * @return the aula
     */
    public String getAula() {
        return aula;
    }

    /**
     * @return the timeSlot
     */
    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    /**
     * @return the docente
     */
    public String getDocente() {
        return docente;
    }

    /**
     * @return the motivo
     */
    public String getMotivo() {
        return motivo;
    }

    /**
     * @param docente
     *                    the docente to set
     */
    public void setDocente(String docente) {
        this.docente = docente;
    }

    /**
     * @param motivo
     *                   the motivo to set
     */
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    /*
     * Due prenotazioni sono uguali se hanno la stessa aula e lo stesso time
     * slot. Il docente e il motivo possono cambiare senza influire
     * sull'uguaglianza.
     */
    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(!(obj instanceof Prenotazione))
            return false;
        Prenotazione other = (Prenotazione) obj;
        return this.aula.equals(other.aula) && this.timeSlot.equals(other.timeSlot);
    }

    /*
     * L hashcode di una prenotazione si calcola a partire dai due campi usati
     * per equals.
     */
    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + this.timeSlot.hashCode();
        result = prime * result + this.aula.hashCode();
        return result;
    }

    /*
     * Una prenotazione precede un'altra in base all'ordine dei time slot. Se
     * due prenotazioni hanno lo stesso time slot allora una precede l'altra in
     * base all'ordine tra le aule.
     */
    @Override
    public int compareTo(Prenotazione o) {
        if(o == null)
            throw new NullPointerException("Parametro NON valido!");
        if(this.timeSlot.compareTo(o.timeSlot) == 0) // se 2 prenotazioni hanno lo stesso timeSlot,
            return this.aula.compareTo(o.aula); // il confronto si basa sull'ordinamento tra le aule,
        return this.timeSlot.compareTo(o.timeSlot); // altrimenti su quello tra i timeSlot
    }

    @Override
    public String toString() {
        return "Prenotazione [aula = " + aula + ", time slot =" + timeSlot
                + ", docente=" + docente + ", motivo=" + motivo + "]";
    }

}
