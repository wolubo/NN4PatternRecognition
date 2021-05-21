package de.wbongartz.pattern_recognition.nnet;

import java.util.*;

import org.apache.commons.math3.linear.*;

import patterns.*;

/**
 * Implementiert ein neuronales Netz zur Mustererkennung.
 * Orientiert sich am entsprechenden Abschnitt im folgenden Buch:
 * 'Neuronale Netze', Günter Daniel Rey und Karl F. Wender, 2. Auflage, 2001, ISBN 978-3-456-84881-5 
 * @author Wolfgang Bongartz
 */
public class PatternAssociator {

	private ArrayRealVector _inputVector;
	private ArrayRealVector _outputVector;
	private Array2DRowRealMatrix _weigths;
	private int _noOfInputs;
	private int _noOfOutputs;
	private String[] _semantic;
	private double _epsilon;
	private ActivationFunction _activationFunction;

	/**
	 * Erzeugt das neuronale Netz.
	 * @param noOfInputs Anzahl der Input-Neuronen.
	 * @param noOfOutputs Anzahl der Output-Neuronen.
	 * @param epsilon Lernrate.
	 * @param activationFunction Instanz einer konkreten Aktivierungsfunktion.
	 */
	public PatternAssociator(int noOfInputs, int noOfOutputs, double epsilon, ActivationFunction activationFunction) {
		if(activationFunction==null) throw new IllegalArgumentException("Keine Aktivierungsfunktion angegeben!");

		_noOfInputs   		= noOfInputs;
		_noOfOutputs  		= noOfOutputs;
		_epsilon	  		= epsilon;
		_semantic     		= null;
		_activationFunction = activationFunction;
		_inputVector        = new ArrayRealVector(noOfInputs);
		_outputVector       = new ArrayRealVector(noOfOutputs);

		createNetworkTopology();
	}

	/**
	 * Erzeugt die Gewichtungsmatrix, welche die Gewichtungen aller Kanten des Netzes enthält.
	 * Das Netz besteht nur aus den Input- und den Output-Neuronen. Es gibt also keinen "hidden layer".
	 * Jedes Input-Neuron ist mit jedem Output-Neuron verknüpft. Eine Verknüpfung innerhalb 
	 * der beiden Layer existiert nicht (also keine Verknüpfung zwischen zwei Input-Neuronen oder 
	 * zwischen zwei Output-Neuronen. 
	 * Die Input-Neuronen werden in den Zeilen, die Output-Neuronen in den Spalten der Matrix
	 * aufgetragen. 
	 */
	private void createNetworkTopology() {
		_weigths = new Array2DRowRealMatrix(_noOfInputs, _noOfOutputs);
		_weigths.walkInColumnOrder(new SetValueVisitor(0d)); // Setze alle Einträge auf den Initialwert 0.
	}

	/**
	 * Präsentiert dem Netz ein Bitmuster und liefert die Anwort des Netzes zurück.
	 * @param pattern
	 * @return
	 * @throws NeuralNetworkIsUntrainedException Wird geworfen, falls das Netz vorab nicht trainiert wurde.
	 * @throws InappropriateDataException 
	 */
	public String match(Pattern pattern) throws NeuralNetworkIsUntrainedException, InappropriateDataException {
		
		if(_semantic==null) throw new NeuralNetworkIsUntrainedException();

		resetActivationState();
		set_inputVector_activationState(pattern);
		calculate_outputVector_activationState();

		return fetchAnswer();
	}

	/**
	 * Trainiert das Netz mit den übergebenen Trainingsdaten.
	 * @param trainingData Trainingsdaten, mit denen das Netz trainiert werden soll.
	 * @param batchLearningMode TRUE: Offline-learning-Moduls aktiviert. Die Gewichte werden erst nach Präsentation aller Bitmuster angepasst.
	 * 							FALSE: Online-learning-Moduls aktiviert. Die Gewichte werden nach jedem präsentierten Bitmuster aktualisiert.
	 * @param noOfEpoch Anzahl der Epochen, die durchlaufen werden sollen (Anzahl der Durchläufe mit den Trainingsdaten). 
	 * @param randomNumberSource Falls eine Quelle für Zufallszahlen übergeben wird werden die Bitmuster in zufälliger Reihenfolge durchlaufen. 
	 * Andernfalls linear, so wie sie im PatternSet gespeichert sind.
	 * @throws InappropriateDataException 
	 */
	public void train(PatternSet trainingData, boolean batchLearningMode, int noOfEpoch, Random randomNumberSource) throws InappropriateDataException {
		
		_semantic = trainingData.getListOfName();
		
		if(_noOfOutputs>_semantic.length) throw new InappropriateDataException("Die Trainingsdaten enthalten mehr Kategorien, als das Netz verarbeiten kann.");
		
		Array2DRowRealMatrix deltaWeigths = new Array2DRowRealMatrix(_noOfInputs, _noOfOutputs);
		Iterator<Pattern> iter=null;
		Pattern trainingPattern=null;

		for(int epoch=0; epoch<noOfEpoch; epoch++) {

			resetActivationState(); // Setze die Aktivierungszustände alle Neuronen zurück.
			
			deltaWeigths.walkInColumnOrder(new SetValueVisitor(0d)); // Setze alle Einträge auf 0.
			
			if(randomNumberSource!=null)
				iter = new RandomPatternIterator(trainingData, randomNumberSource);
			else
				iter = trainingData.iterator();
			
			while(iter.hasNext()) {
				trainingPattern = (Pattern) iter.next(); // Hole nächstes Bitmuster.
				ArrayRealVector targetVector = createTargetVector(trainingPattern);
				set_inputVector_activationState(trainingPattern);	// Übertrage Bitmuster in Input-Neuronen.
				calculate_outputVector_activationState();
				applyLearningRule(targetVector, deltaWeigths);
				if(!batchLearningMode) updateWeights(deltaWeigths);	// Falls Online-learning aktiviert ist: Aktualisiere nun die Gewichtsmatrix.
			}
			if(batchLearningMode) updateWeights(deltaWeigths); // Falls Offline-learning aktiviert ist:  Aktualisiere nun die Gewichtsmatrix.
		}
	}

	/**
	 * Erzeugt einen Vektor, der dem gewünschten Output des Netzes entspricht.
	 * @param trainingPattern
	 * @return
	 */
	private ArrayRealVector createTargetVector(Pattern trainingPattern) {
		
		String targetName = trainingPattern.get_name();
		int targetIndex=-1;
		for(int i=0; i<_semantic.length; i++) {
			if(_semantic[i].compareTo(targetName)==0) {
				targetIndex=i;
			}
		}
		
		ArrayRealVector retVal = new ArrayRealVector(_noOfOutputs);
		retVal.setEntry(targetIndex, 1.0d);
		return retVal;
	}

	/**
	 * Setzt den Aktivierungszustand aller Neuronen zurück auf "0".
	 */
	private void resetActivationState() {
		_inputVector  = new ArrayRealVector(_noOfInputs);
		_outputVector = new ArrayRealVector(_noOfOutputs);
	}

	/**
	 * Berechne das Delta zur aktuellen Gewichtsmatrix.
	 * @param deltaWeigths
	 */
	private void applyLearningRule(ArrayRealVector targetVector, Array2DRowRealMatrix deltaWeigths) {
		DeltaRule deltaRule = new DeltaRule(_inputVector, targetVector, deltaWeigths, _epsilon);
		_outputVector.walkInDefaultOrder(deltaRule);
	}

	/**
	 * Setze die Aktivierungszustände der Input-Neuronen.
	 * @param inputVector
	 * @throws InappropriateDataException 
	 */
	private void set_inputVector_activationState(Pattern pattern) throws InappropriateDataException {
		int inputVectorSize = pattern.get_height()*pattern.get_width();
		if( inputVectorSize != _noOfInputs ) throw new InappropriateDataException("Größe des Bitmusters ist für das Netz nicht passend! Das Netz hat " + _noOfInputs + " Eingangsneuronen. Das Bitmuster besteht aber aus " + inputVectorSize + " Bits.");

		// Convert pattern to input vector.
		for(int i=0; i<pattern.get_height(); i++) {
			for(int j=0; j<pattern.get_width(); j++) {
				// Setze den Aktivierungszustand aller Input-Neuronen.
				if( pattern.get(i, j) )
					_inputVector.setEntry(i+j, 1.0d);
				else
					_inputVector.setEntry(i+j, 0.0d);
			}
		}
	}

	/**
	 * Berechnet die Aktivierungszustände der Output-Neuronen.
	 */
	private void calculate_outputVector_activationState() {
		_activationFunction.setInputVector(_inputVector);
		_activationFunction.setWeigths(_weigths);
		_outputVector.walkInDefaultOrder(_activationFunction);
	}

	/**
	 * Addiere das aktuelle Delta auf die Gewichtsmatrix.
	 * @param deltaWeigths
	 */
	private void updateWeights(Array2DRowRealMatrix deltaWeigths) {
		Array2DRowRealMatrix newWeigths = deltaWeigths.add(_weigths);
		_weigths = newWeigths;
	}

	/**
	 * Sucht das Output-Neuron mit dem höchsten Ausgangswert.
	 * Liefert die semantische Bedeutung dieses Neurons zurück.
	 * Falls es mehrere Output-Neuronen gibt, deren Ausgangswert gleich dem höchsten Ausgangswert ist: Liefert die semantische Bedeutung des Neurons mit dem niedrigsten Index.
	 * @return Antwort des Netzes.
	 */
	private String fetchAnswer() {
		double maxValue=Double.NEGATIVE_INFINITY;
		int    maxValueIndex=-1;
		for(int i=0; i<_outputVector.getDimension(); i++) {
			if(_outputVector.getEntry(i)>maxValue) {
				maxValueIndex=i;
				maxValue=_outputVector.getEntry(i);
			}
		}
		return _semantic[maxValueIndex];
	}

}
