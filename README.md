## Esercitazione 5

### Esercitazione sull'uso delle Collections della Java SE, in particolare Set e SortedSet

#### Aule, Set e SortedSet

Scaricare il codice associato dal link sottostante e riconsiderare il proprio
codice della Esercitazione a casa N 4.

1) Modificare la classe Aula come segue:

- cambiare il tipo della variabile istanza facilities da Facility[] a
  Set<Facility>
- cambiare il tipo della variabile istanza prenotazioni da Prenotazione[] a
  SortedSet<Prenotazione>
- riadattare tutti gli altri metodi di conseguenza (si veda il template e le
  API della classe aggiornati), utilizzando le API delle due interface Set<E>
  e SortedSet<E>; in particolare reimplementare il metodo isFree() sfruttando
  l'ordinamento naturale tra le prenotazioni per farlo in maniera efficiente
- implementare il nuovo metodo boolean removePrenotazione(Prenotazione p)
- implementare il nuovo metodo boolean removePrenotazioniBefore(
  GregorianCalendar timePoint) sfruttando l'ordinamento naturale tra le
  prenotazioni per farlo in maniera efficiente

La classe di test JUnit per Aula contiene i test aggiornati.

2) Implementare la classe GestoreAule di cui è dato il template e una classe
   di test JUnit. Sostituire cioè le parti "//TODO implementare ..." con il
   codice opportuno

## Consegna

Si possono fare più commit e push sul proprio repository. Dopo la prima push
l'esercitazione viene considerata consegnata.
L'ultima push viene considerata la versione finale.