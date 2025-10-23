/**
 * 
 */
package it.unicam.cs.asdl2526.es3;

import java.util.Calendar;

import java.util.GregorianCalendar;

// La classe GregorianCalendar definisce una linea del tempo DISCRETA (ovviamente
// non può essere CONTINUA) che parte dall'anno 1970 e usa una scala in MILLISECONDI.
// Questo significa che l'istante del 1 gennaio 1970 a mezzanotte è uguale a 0, un
// MILLISECONDO prima è uguale a -1 e un MILLISECONDO dopo a 1: ogni istante corrisponderà
// quindi a una certa era (AD o BC), un certo anno, un certo mese ecc... fino a un
// preciso MILLISECONDO

/**
 * Un time slot è un intervallo di tempo continuo che può essere associato ad
 * una prenotazione. Gli oggetti della classe sono immutabili. Non sono ammessi
 * time slot che iniziano e finiscono nello stesso istante.
 * 
 * @author Luca Tesei
 *
 */
public class TimeSlot implements Comparable<TimeSlot> {

    /**
     * Rappresenta la soglia di tolleranza da considerare nella sovrapposizione
     * di due Time Slot. Se si sovrappongono per un numero di minuti minore o
     * uguale a questa soglia allora NON vengono considerati sovrapposti.
     */
    public static final int MINUTES_OF_TOLERANCE_FOR_OVERLAPPING = 5;

    private final GregorianCalendar start;

    private final GregorianCalendar stop;

    /**
     * Crea un time slot tra due istanti di inizio e fine
     * 
     * @param start
     *                  inizio del time slot
     * @param stop
     *                  fine del time slot
     * @throws NullPointerException
     *                                      se uno dei due istanti, start o
     *                                      stop, è null
     * @throws IllegalArgumentException
     *                                      se start è uguale o successivo a
     *                                      stop
     */
    public TimeSlot(GregorianCalendar start, GregorianCalendar stop) {
        if(start == null || stop == null)
            throw new NullPointerException("Inserire parametri validi!");
        if(start.equals(stop) || start.compareTo(stop) >= 0)
            throw new IllegalArgumentException("Il valore di START deve essere MINORE di quello di STOP");
        this.start = start;
        this.stop = stop;
    }

    /**
     * @return the start
     */
    public GregorianCalendar getStart() {
        return start;
    }

    /**
     * @return the stop
     */
    public GregorianCalendar getStop() {
        return stop;
    }

    /*
     * Un time slot è uguale a un altro se rappresenta esattamente lo stesso
     * intervallo di tempo, cioè se inizia nello stesso istante e termina nello
     * stesso istante.
     */
    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(!(obj instanceof TimeSlot))
            return false;
        TimeSlot other = (TimeSlot) obj;
        if(this.start.equals(other.start) && this.stop.equals(other.stop))
            return true;
        return false;
    }

    /*
     * Il codice hash associato a un timeslot viene calcolato a partire dei due
     * istanti di inizio e fine, in accordo con i campi usati per il metodo
     * equals.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = this.start.hashCode();
        result = prime * result + (int)(temp ^ (temp >>> 32));
        temp = this.stop.hashCode();
        result = prime * result + (int)(temp ^ (temp >>> 32));
        return result;
    }

    /*
     * Un time slot precede un altro se inizia prima. Se due time slot iniziano
     * nello stesso momento quello che finisce prima precede l'altro. Se hanno
     * stesso inizio e stessa fine sono uguali, in compatibilità con equals.
     */
    @Override
    public int compareTo(TimeSlot o) {
        if(o == null)
            throw new NullPointerException("Parametro NON valido!");
        if(this.equals(o))
            return 0;
        if(this.start.compareTo(o.start) < 0)
            return -1;
        if(this.start.compareTo(o.start) == 0 && this.stop.compareTo(o.stop) < 0)
            return -1;
        return 1;
    }

    /**
     * Determina il numero di minuti di sovrapposizione tra questo timeslot e
     * quello passato.
     * 
     * @param o
     *              il time slot da confrontare con questo
     * @return il numero di minuti di sovrapposizione tra questo time slot e
     *         quello passato, oppure -1 se non c'è sovrapposizione. Se questo
     *         time slot finisce esattamente al millisecondo dove inizia il time
     *         slot <code>o</code> non c'è sovrapposizione, così come se questo
     *         time slot inizia esattamente al millisecondo in cui finisce il
     *         time slot <code>o</code>. In questi ultimi due casi il risultato
     *         deve essere -1 e non 0. Nel caso in cui la sovrapposizione non è
     *         di un numero esatto di minuti, cioè ci sono secondi e
     *         millisecondi che avanzano, il numero dei minuti di
     *         sovrapposizione da restituire deve essere arrotondato per difetto
     * @throws NullPointerException
     *                                      se il time slot passato è nullo
     * @throws IllegalArgumentException
     *                                      se i minuti di sovrapposizione
     *                                      superano Integer.MAX_VALUE
     */
    public int getMinutesOfOverlappingWith(TimeSlot o) {
        if(o == null)
            throw new NullPointerException("Parametro NON valido!");

        if(this.stop.equals(o.start) || this.start.equals(o.stop))
            return -1;

        // Salvo tutti i confronti in delle variabili per evitare
        // di richiamare il metodo compareTo in ogni caso da esaminare

        int startStart = this.start.compareTo(o.start);
        int startStop = this.start.compareTo(o.stop);
        int stopStart = this.stop.compareTo(o.start);
        int stopStop = this.stop.compareTo(o.stop);

        // Variabile in cui memorizzo il risultato da ritornare alla fine
        long result;
        
        // CASO 1
        // Questo timeslot inizia prima di quello passato e termina prima
        // che quello passato come argomento sia finito (ma dopo che sia iniziato)
        if (startStart < 0 && stopStop < 0 && stopStart > 0) {
            // SOVRAPPOSIZIONE: dall'inizio di o alla fine di this
            result = this.stop.getTimeInMillis() - o.start.getTimeInMillis();
            return roundMinutesOfOverlapping(result);
        }
        
        // CASO 2
        // Questo timeslot inizia prima di quello passato e termina 
        // dopo che quello passato come argomento sia finito
        if (startStart < 0 && stopStop > 0) {
            // SOVRAPPOSIZIONE: dall'inizio alla fine di o (tutto o)
            result = o.stop.getTimeInMillis() - o.start.getTimeInMillis();
            return roundMinutesOfOverlapping(result);
        }
        
        // CASO 3
        // Questo timeslot inizia dopo di quello passato (ma prima che finisca)
        // e termina dopo che quello passato come argomento sia finito
        if (startStart > 0 && stopStop > 0 && startStop < 0) {
            // SOVRAPPOSIZIONE: dall'inizio di this alla fine di o
            result = o.stop.getTimeInMillis() - this.start.getTimeInMillis();
            return roundMinutesOfOverlapping(result);
        }
        
        // CASO 4
        // Questo timeslot inizia dopo di quello passato e termina
        // prima che quello passato come argomento sia finito
        if (startStart > 0 && stopStop < 0) {
            // SOVRAPPOSIZIONE: dall'inizio alla fine di this (tutto this)
            result = this.stop.getTimeInMillis() - this.start.getTimeInMillis();
            return roundMinutesOfOverlapping(result);
        }
        return -1;
    }


    // Metodo che arrotonda il risultato di getMinutesOfOverlappingWith
    // in accordo con la sua API
    private int roundMinutesOfOverlapping(long x) {
        // 1 minuto = 60 secondi quindi 1 minuto = (60 * 1000) millisecondi
        // perciò per passare dai millisecondi ai minuti divido per 60000
        long result = x / 60000;
        if (result > Integer.MAX_VALUE)
            throw new IllegalArgumentException("Numero di minuti di sovrapposizione TROPPO GRANDE!");
        return (int) result;
    }

    /**
     * Determina se questo time slot si sovrappone a un altro time slot dato,
     * considerando la soglia di tolleranza.
     * 
     * @param o
     *              il time slot che viene passato per il controllo di
     *              sovrapposizione
     * @return true se questo time slot si sovrappone per più (strettamente) di
     *         MINUTES_OF_TOLERANCE_FOR_OVERLAPPING minuti a quello passato
     * @throws NullPointerException
     *                                  se il time slot passato è nullo
     */
    public boolean overlapsWith(TimeSlot o) {
        if(o == null)
            throw new NullPointerException("Parametro NON valido!");
        if(this.getMinutesOfOverlappingWith(o) > MINUTES_OF_TOLERANCE_FOR_OVERLAPPING)
            return true;
        return false;
    }

    /*
     * Ridefinisce il modo in cui viene reso un TimeSlot con una String.
     * 
     * Esempio 1, stringa da restituire: "[4/11/2019 11.0 - 4/11/2019 13.0]"
     * 
     * Esempio 2, stringa da restituire: "[10/11/2019 11.15 - 10/11/2019 23.45]"
     * 
     * I secondi e i millisecondi eventuali non vengono scritti.
     */
    @Override
    public String toString() {
        return "[" + start.get(Calendar.DAY_OF_MONTH) + "/"
                + (start.get(Calendar.MONTH) + 1) + "/"
                + start.get(Calendar.YEAR) + " "
                + start.get(Calendar.HOUR_OF_DAY) + "."
                + start.get(Calendar.MINUTE) + " - "
                + stop.get(Calendar.DAY_OF_MONTH) + "/"
                + (stop.get(Calendar.MONTH) + 1) + "/" + stop.get(Calendar.YEAR)
                + " " + stop.get(Calendar.HOUR_OF_DAY) + "."
                + stop.get(Calendar.MINUTE) + "]";
    }

}
