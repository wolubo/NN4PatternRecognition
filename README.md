# NN4PatternRecognition
# Neural Network for Pattern Recognition

Das Programm simuliert ein neuronales Netzwerk zur Erkennung von Bitmustern. Die zu erkennenden Bitmuster und alle für das Training des Netzes und den Programmablauf relevanten Parameter werden aus einem Textfile eingeladen. Danach wir das neuronale Netz zunächst auf die eingelesenen Bitmuster trainiert. Danach werden die aus dem Textfile eingelesenen Bitmustern nach dem Zufallsprinzip verändert und die so veränderten Bitmuster werden dem neuronalen Netz in zufälliger Reihenfolge präsentiert. Zum Abschluss wird die erreichte Erkennungsrate ausgegeben.

## Start des Programms
````
java –jar NN4PatternRecognition <filename>
````
oder alternativ
````
gradle run --args "<filename>"
````
Als Argument wird eine Textdatei übergeben, die sowohl die Bitmuster als auch die Angaben zur Konfiguration 
enthält. Wird kein Dateiname angeben, so wird die Datei ‚good_set.txt’ geladen. Beispiel: 
````
java –jar NN4PatternRecognition avg_set.txt
````
bzw.
````
gradle run --args "avg_set.txt"
````

## Die Topologie des neuronalen Netzes
Das implementierte neuronale Netz besteht aus einer Eingabe- und einer Ausgabeschicht. Eine Zwischenschicht existiert nicht. Jedem Neuron der Eingabeschicht ist genau ein Bit des Bitmusters zugeordnet. Jedem Neuron der Ausgabeschicht ist wiederum der Name eines der zu erkennenden Bitmuster zugeordnet. Jedes Eingabe-Neuron ist mit jedem Ausgabe-Neuron verbunden. Verbindungen zwischen den Neuronen einer Schicht existieren nicht. Orientiert habe ich mich am Buch "Neuronale Netze" von Günter Daniel Rey und Karl F. Wender (2. Auflage, 2001, ISBN 978-3-456-84881-5).

## Der Aufbau der Input-Dateien

Alle Konfigurationsangaben werden nach dem Muster
````
<argument> = <wert>
````
angegeben. Die Dateien 'avg_set.txt', 'bad_set.txt' und 'good_set.txt' dienen als Beispiele.

### Programm-Konfiguration
| Argument | Werte |
| --- | --- |
| mode | Nach dem Training werden werden dem Netz nacheinander alle generierten Zufalls-Bitmuster präsentiert. Der Parameter ‚mode’ steuert, ob die einzelnen Bitmuster auch angezeigt werden. Mögliche Werte: *batch* (keine Anzeige der Muster; nur Zusammenfassung) und *interactive* (jedes Bitmuster wird zusammen mit dem Ergebnis der Bewertung angezeigt. Mit der Eingabetaste gelangt der Benutzer zum nächsten Bitmuster. Mit der Eingabe des Wortes ‚ende’ kann das Programm vorzeitig beendet werden). |
| numberOfRandomSamples | Definiert, wie viele Bitmuster nach dem Zufallsprinzip erzeugt werden sollen. Muss größer oder gleich Null sein. Falls Null: Es werden dem Netz nur die Original-Bitmuster vorgelegt. |
| maxErrorsPerSample | Legt fest, wie viele Fehler jedes Zufalls-Bitmuster höchstens enthalten soll. Mögliche Werte: Zwischen 0 und 10 (einschließlich). |
| presentOriginals | *yes* (Neben den Zufalls-Bitmustern werden dem Netz auch die Original-Bitmuster zur Erkennung vorgelegt, mit denen es trainiert wurde); *no* (Vorgelegt werden nur die Zufalls-Bitmuster) |

### Netz-Konfiguration
| Argument | Werte |
| --- | --- |
| epsilon | Legt die beim Training zu verwendende Lernrate fest. |
| laps | Legt fest, wie oft die Menge der Original-Bitmuster trainiert werden soll (Anzahl der Trainingsdurchläufe). |
| activation_function | Legt die zu verwendende Aktivierungsfunktion fest. Möglich sind folgende Werte: *Linear* (a = nettoInput), *Binary* (a = 1 falls nettoInput >= 0 und a = 0 wenn nettoInput < 0), *TangensHyperbolicus* (a = tanh(nettoInput)), *Logistic* (Logistische Aktivierungsfunktion) |
| learnmode | Legt fest, zu welchem Zeitpunkt die Verbindungsgewichte zwischen den Input- und den Output-Neuronen angepasst werden sollen: *batch* (Zunächst werden alle Bitmuster präsentiert. Die dabei ermittelten Änderungen werden aufsummiert und erst im Anschluss zu den Verbindungsgewichten addiert), *online* (Die Verbindungsgewichte werden nach jedem einzelnen Bitmuster aktualisiert) |

### Definition der Bitmuster
| Argument | Werte |
| --- | --- |
| width | Höhe der Bitmuster in Bit. Alle Bitmuster in der Datei müssen die gleiche Höhe haben. |
| height | Breite der Bitmuster in Bit. Alle Bitmuster in der Datei müssen die gleiche Breite haben. |
| name | Name des der Zeile folgenden Bitmusters. Jedes Bitmuster muss einen eindeutigen Namen haben. |

Ein Bitmuster wird einfach durch zeilenweise Angabe der einzelnen Bitwerte definiert. Dabei werden das Zeichen ‚.’ („Punkt“) als 0 und das Zeichen ‚X’ als 1 interpretiert.
Beispiel:
````
name=a
.XXX..
X...X.
....X.
.XXXX.
X...X.
X..XX.
.XX.X.
````
