package de.wbongartz.pattern_recognition;

import java.io.*;
import java.util.*;

import nnet.*;
import patterns.*;

/**
 * The main program.
 * 
 * @author Wolfgang Bongartz
 */
public class NN4PatternRecognition {
	
	private Random     	_randomNumberSource    = new Random();
	private PatternSet 	_originalSamples       = null;	// Samples, auf die das Netz trainiert wird.
	private int        	_patternWidth          = 6;    	// Breite der Patterns.
	private int        	_patternHeight         = 8;    	// Höhe der Patterns.
	private double     	_epsilon               = 0.5d; 	// Lernrate.
	private int        	_numberOfInputNeurons;			// Anzahl der Input-Neuronen.
	private int 		_numberOfOutputNeurons;			// Anzahl der Output-Neuronen.
	private boolean 	_batchLearnMode        = false;	// Steuert den Lernmodus. TRUE=Batch-Learning. FALSE=Online-Learning.
	private int 		_numberOfLaps          = 10;	// Anzahl der durchzuführenden Trainingsrunden.
	private int 		_numberOfRandomSamples = 10;	// Anzahl der zufällig erzeugten Samples, die das Netz erkennen soll.
	private int 		_maxErrorsPerSample    = 2;		// Maximale Anzahl der Fehler, in denen sich die Zufalls-Samples von den Originalen unterscheiden.
	private boolean 	_batchMode             = true;	// Steuert den Programmablauf: TRUE=Ablauf ohne User-Interaktion. FALSE=Jedes Sample wird angezeigt.
	private boolean     _presentOriginals 	   = true;	// Steuert, ob die Originalmuster auch wieder präsentiert werden sollen.
	private ActivationFunction _activationFunction = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String filename="good_set.txt";
		if(args.length==1) {
			filename=args[0];
		}

		try {
			
			// Programm-Objekt erzeugen.
			NN4PatternRecognition prog = new NN4PatternRecognition(filename);
			
			// Programm starten.
			prog.run();
			
		} catch (FileNotFoundException e) {
			System.out.println("Konfigdatei konnte nicht geladen werden: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Beim Laden der Konfigdatei ist ein Fehler aufgetreten: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Programmfehler: " + e.getMessage());
		}
	}
	
	public NN4PatternRecognition(String configFile) throws IOException {
		// Konfig-Daten und Original-Samples aus einer Datei laden.
		loadConfigFile(configFile);
		
		_numberOfInputNeurons  = this._patternWidth * this._patternHeight;
		_numberOfOutputNeurons = this._originalSamples.size();

		System.out.println("Anzahl Bits pro Bitmuster: " + _numberOfInputNeurons);
		System.out.println("davon sind relevant:       " + _originalSamples.getNumberOfRelevantBits());
	}
	

	/**
	 * @throws InappropriateDataException 
	 * @throws NeuralNetworkIsUntrainedException 
	 * 
	 */
	public void run() throws NeuralNetworkIsUntrainedException, InappropriateDataException {
		
		// Neuronales Netz erzeugen.
		PatternAssociator pa = new PatternAssociator(_numberOfInputNeurons, _numberOfOutputNeurons, _epsilon, _activationFunction);

		// Neuronales Netz trainieren.
		pa.train(_originalSamples, _batchLearnMode, _numberOfLaps, _randomNumberSource);
		
		// Aus den Originalen zufällige Abwandlungen erzeugen.
		PatternSet randomSamples = _originalSamples.createRandomized(_numberOfRandomSamples, _maxErrorsPerSample, _presentOriginals);

		// Dem Netz alle Samples präsentieren und die Ergebnisse ausgeben.
		Scanner userInput=null;
		if( ! _batchMode ) userInput=new Scanner(System.in);
		String answer;
		int errorCounter=0;
		int sampleCounter=0;
		for(Pattern sample: randomSamples) {
			if( ! _batchMode ) {
				System.out.println("Nächstes Sample:");
				System.out.println(sample.toString());
				System.out.print("Erkannt als: ");
			}

			answer = pa.match(sample);
			
			if( ! _batchMode ) {
				System.out.println(answer);
				if(answer.compareTo(sample.get_name())==0) {
					System.out.println("Das ist korrekt.");
				} else {
					System.out.println("Das ist nicht korrekt.");
				}
				System.out.println();
			}

			sampleCounter++;
			if(answer.compareTo(sample.get_name())!=0) errorCounter++;

			if( ! _batchMode ) {
				System.out.println("Fortsetzen? Dann Enter drücken. Sonst 'ende' eingeben.");
				answer = userInput.nextLine();
				if(answer.compareTo("ende")==0) break;
			}
		}
		
		int correct=sampleCounter-errorCounter;
		long rate = Math.round( (double) correct / sampleCounter * 100d );
		System.out.println("Präsentiert wurden " + sampleCounter + " Muster.");
		System.out.println("Davon wurden " + correct + " korrekt erkannt.");
		System.out.println("Erkennungsrate: " + rate + "%");

	}

	/**
	 * @param configFile
	 * @throws IOException 
	 */
	private void loadConfigFile(String configFile) throws IOException {
		ArrayList<Pattern> patternArray = new ArrayList<Pattern>();

		BufferedReader reader=null;
		int lineNo = 0;
		String name = "";
		try {
			reader = new BufferedReader(new FileReader(configFile));
			String line = reader.readLine();
			while(line!=null) {
				lineNo++;
				line = line.trim();
				String[] parts=line.split("=");
				if(parts.length==1) {
					if(parts[0].length()==0) {
						// Skip empty lines.
					} else {
						String pattern[] = new String[_patternHeight];
						int i=0;
						do {
							if(line.length()!=_patternWidth) throw new IllegalStateException("FEHLER: Zeile ist zu kurz für das Pattern. Zeile: " + lineNo);
							pattern[i++]=line;
							line = reader.readLine();
							if(line!=null) line = line.trim();
							lineNo++;
						} while (i<_patternHeight && line!=null);
						if(i<_patternHeight && line==null) throw new IllegalStateException("FEHLER: Unvollständiges Pattern. Zeile: " + lineNo);
						Pattern newPattern = new Pattern(name, pattern);
						name="";
						patternArray.add(newPattern);
					}
				} else if(parts.length==2) {
					String key   = parts[0];
					String value = parts[1];
					if(key.compareTo("width")==0) {
						_patternWidth = Integer.parseInt(value);
						if(_patternWidth<1) throw new IllegalStateException("'width' zu gering. Zeile: " + lineNo);
					} else if(key.compareTo("epsilon")==0) {
						_epsilon = Double.parseDouble(value);
						if(_epsilon<0d || _epsilon>1d) throw new IllegalStateException("'epsilon' außerhalb des Wertebereichs (0.0 - 1.0). Zeile: " + lineNo);
					} else if(key.compareTo("height")==0) {
						_patternHeight = Integer.parseInt(value);
						if(_patternHeight<1) throw new IllegalStateException("'height' zu gering. Zeile: " + lineNo);
					} else if(key.compareTo("laps")==0) {
						_numberOfLaps = Integer.parseInt(value);
						if(_numberOfLaps<1) throw new IllegalStateException("Anzahl der Trainingsrunden zu gering. Zeile: " + lineNo);
					} else if(key.compareTo("name")==0) {
						name = value;
					} else if(key.compareTo("learnmode")==0) {
						if(value.compareTo("batch")==0) {
							_batchLearnMode = true;
						} else if(value.compareTo("online")==0) {
							_batchLearnMode = false;
						} else {
							throw new IllegalStateException("Unbekannter Lernmodus. Möglich sind: 'batch' und 'online'. " + lineNo);							
						}
					} else if(key.compareTo("activation_function")==0) {
						if(value.compareTo("TangensHyperbolicus")==0) {
							_activationFunction  = new ActivationFunction_TangensHyperbolicus();
						} else if(value.compareTo("Linear")==0) {
							_activationFunction = new ActivationFunction_Linear();
						} else if(value.compareTo("Logistic")==0) {
							_activationFunction = new ActivationFunction_Logistic();
						} else if(value.compareTo("Binary")==0) {
							_activationFunction = new ActivationFunction_Binary();
						} else {
							throw new IllegalStateException("Unbekannte Aktivierungsfunktion. Möglich sind: 'Linear', 'Binary', 'TangensHyperbolicus' und 'Logistic'. " + lineNo);							
						}
					} else if(key.compareTo("presentOriginals")==0) {
						if(value.compareTo("yes")==0) {
							    _presentOriginals  = true;
						} else {
							_presentOriginals = false;
						}
					} else if(key.compareTo("numberOfRandomSamples")==0) {
						_numberOfRandomSamples = Integer.parseInt(value);
						if(_numberOfRandomSamples<0) throw new IllegalStateException("Anzahl der Zufalls-Pattern zu gering. Zeile: " + lineNo);
					} else if(key.compareTo("maxErrorsPerSample")==0) {
						_maxErrorsPerSample = Integer.parseInt(value);
						if(_maxErrorsPerSample<0||_maxErrorsPerSample>10) throw new IllegalStateException("Anzahl der Fehler pro Zufalls-Pattern unpassend. Zeile: " + lineNo);
					} else if(key.compareTo("mode")==0) {
						if(value.compareTo("batch")==0) {
							_batchMode = true;
						} else if(value.compareTo("interactive")==0) {
							_batchMode = false;
						} else {
							throw new IllegalStateException("Unbekannter Programmmodus. Möglich sind: 'batch' und 'interactive'. " + lineNo);							
						}
					} else {
						throw new IllegalStateException("Key unbekannt! Zeile: " + lineNo);
					}
				} else {
					throw new IllegalStateException("Syntaxfehler in Zeile: " + lineNo);
				}
				line = reader.readLine();
			}
		} finally {
			if(reader!=null) reader.close();
		}
		_originalSamples = new PatternSet(patternArray, _randomNumberSource);
	}

}
