## Esercitazione 9

### Algoritmi di ordinamento e valutazione numerica della complessità

1. Clonare il repository e implementare le classi
    - QuickSort
    - QuickSortRandom
    - MergeSort
    - InsertionSort

Vanno implementati i metodi contenenti il commento
`// TODO implementare`. Si noti che è richiesta, in alcuni
casi, l'implementazione **in loco**.

2. Eseguire il framework di valutazione numerica degli algoritmi di
   ordinamento sulle classi implementate, importare i dati in un foglio
   elettronico o in un foglio google, ripulire i dati dai picchi e realizzare
   i grafici che visualizzano le stime dei casi ottimo, pessimo e medio (
   con +/- deviazione standard). Realizzare anche grafici di confronto tra i
   diversi algoritmi che si ritengono significativi (si vedano le slides).

   **ATTENZIONE**: Se non si riescono a implementare tutti gli algoritmi
   correttamente il framework dà errore. Si possono commentare le righe 52-56
   del file SortingAlgorithmEvaluationFramework.java non mettendo (quindi
   commentando) solo gli algoritmi che non funzionano o non sono stati
   implementati. Il framework genererà i dati solo per gli algoritmi non
   commentati e si potranno creare i grafici solo per quelli.

## Consegna

Per il codice procedere come al solito.

Per l'analisi dei dati, inserire nella cartella `dati_e_grafici` nella root
del progetto il file del foglio elettronico utilizzato per analizzare i dati e
generare i grafici. Se si è usato un foglio google inserire nella cartella un
file di testo contenente il link al foglio elettronico google, che deve essere
in condivisione dal proprio google drive con accesso Visualizzatore a chiunque
abbia il link (ricordarsi di non mettere il proprio nome/cognome nel titolo
del foglio).

Si possono fare più commit e push sul proprio repository. Dopo la prima push
l'esercitazione viene considerata consegnata.
L'ultima push viene considerata la versione finale.