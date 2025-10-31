/**
 * 
 */
package it.unicam.cs.asdl2526.es4;

/**
 * Una Presence Facility è una facility che può essere presente oppure no. Ad
 * esempio la presenza di un proiettore HDMI oppure la presenza dell'aria
 * condizionata.
 * 
 * @author Template: Luca Tesei, Implementation: Collective
 *
 */
public class PresenceFacility extends Facility {

    /**
     * Costruisce una presence facility.
     * 
     * @param codice
     * @param descrizione
     * @throws NullPointerException
     *                                  se una qualsiasi delle informazioni
     *                                  richieste è nulla.
     */
    public PresenceFacility(String codice, String descrizione) {
        super(codice, descrizione);
    }

    /*
     * Una Presence Facility soddisfa una facility solo se la facility passata è
     * una Presence Facility ed ha lo stesso codice.
     * 
     */
    @Override
    public boolean satisfies(Facility o) {
        if(this == o)
            return true;
        if(o == null)
            throw new NullPointerException("Parametro NON valido!");
        if(!(o instanceof PresenceFacility))
            return false;
        return this.getCodice().equals(o.getCodice());
    // Non serve fare il cast come nell equals poiché, visto che PresenceFacility è
    // sottoclasse di Facility, posso accedere ai campi e chiamare i metodi della
    // classe Facility anche con un'istanza della classe PresanceFacility
    }

}
