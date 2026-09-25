# Algoritmi e Strutture Dati — Esercitazioni in Java

**Università di Camerino (UNICAM)** · 13 esercitazioni di laboratorio · Java · JUnit

Questo repository raccoglie le 13 esercitazioni che ho svolto per la parte di laboratorio del corso di **Algoritmi e Strutture Dati** dell'Università di Camerino, riunendole in unico posto e rendendole facili da consultare. Le attività seguono un percorso che parte dalla programmazione orientata agli oggetti e dalle collezioni Java, per arrivare all'implementazione e all'analisi di strutture dati, algoritmi di ordinamento, programmazione dinamica e algoritmi sui grafi.

## Organizzazione del repository

Ogni esercitazione è organizzata in una cartella dedicata, che conserva i file del progetto e il README originale con la relativa consegna. Le esercitazioni sono state riunite a partire dai singoli repository di GitHub Classroom **senza comprimere la cronologia Git**, mantenendo i commit e le date originali.

```text
Algoritmi-StruttureDati/
├── Esercitazione-01/
├── Esercitazione-02/
├── ...
├── Esercitazione-13/
└── README.md
```

Le esercitazioni sono quindi **progetti indipendenti**, non moduli di un'unica applicazione. Per esplorarne una, è possibile aprire la cartella corrispondente in un IDE Java e consultare il relativo README per leggere la consegna originale e le eventuali istruzioni specifiche. Non è previsto un unico comando di compilazione o di esecuzione dei test valido per l'intero repository.

## Indice

| N. | Argomento | Contenuti principali |
|:--:|---|---|
| [01](./Esercitazione-01/) | Equazioni di secondo grado e testing | Implementazione di un risolutore di equazioni di secondo grado, scrittura di test JUnit (compresi i casi di eccezione) e debug di un'interfaccia testuale. |
| [02](./Esercitazione-02/) | Cassaforte e scassinatore | Implementazione di classi a partire da API Java documentate, con un'interfaccia grafica e test forniti. |
| [03](./Esercitazione-03/) | Prenotazioni e intervalli temporali | Gestione di date e orari tramite `Calendar` e `GregorianCalendar`; implementazione di `equals`, `hashCode` e `compareTo`. |
| [04](./Esercitazione-04/) | Aule, prenotazioni e servizi | Ereditarietà e polimorfismo attraverso diverse tipologie di servizi; gestione di prenotazioni e servizi mediante array a ridimensionamento dinamico. |
| [05](./Esercitazione-05/) | Collezioni Java | Riorganizzazione della gestione delle aule tramite `Set` e `SortedSet`, utilizzo dell'ordinamento naturale e implementazione di un gestore delle aule. |
| [06](./Esercitazione-06/) | Linked List | Implementazione di alcuni metodi dell'interfaccia generica `List<E>` mediante una lista semplicemente concatenata. |
| [07](./Esercitazione-07/) | Tabelle hash | Implementazione di un `Set<E>` ridimensionabile, con gestione delle collisioni tramite liste e ridimensionamento basato sul fattore di carico. |
| [08](./Esercitazione-08/) | Ricorsione e alberi binari di ricerca | Operazioni ricorsive su liste e alberi binari di ricerca (*Binary Search Tree*). |
| [09](./Esercitazione-09/) | Ordinamento e analisi sperimentale | Quick Sort, Quick Sort randomizzato, Merge Sort e Insertion Sort; misurazione e analisi delle prestazioni nei casi migliore, peggiore e medio realizzata con un foglio excel nel quale sono presenti anche numerosi grafici che mostrano bene i risultati. |
| [10](./Esercitazione-10/) | Heap binari e Heap Sort | Implementazione di un max-heap generico e di Heap Sort *in loco*, con conteggio dei confronti. |
| [11](./Esercitazione-11/) | Programmazione dinamica | Problema dell'ordine ottimale di moltiplicazione di una catena di matrici e problema della sottosequenza comune più lunga (*LCS*). |
| [12](./Esercitazione-12/) | Grafi non orientati | Rappresentazione mediante liste di adiacenza, visite in ampiezza (*BFS*) e in profondità (*DFS*) e algoritmo di Prim per l'albero ricoprente minimo. |
| [13](./Esercitazione-13/) | Grafi orientati e cammini minimi | Rappresentazione mediante matrici di adiacenza e algoritmo di Dijkstra per il calcolo dei cammini minimi. |

> [!NOTE]
> Le esercitazioni derivano dai **template forniti dal docente tramite GitHub Classroom**: il mio lavoro ha riguardato l'implementazione dei metodi richiesti e le modifiche previste dalle consegne (sostituire i TODO presenti nei template in sostanza); ad esempio alcuni progetti includono anche classi di test e codice di supporto già presenti nei template.
