package de.wbongartz.pattern_recognition.nnet;

import org.apache.commons.math3.linear.*;

/**
 * Implementiert die Aktivitätsfunktion. Wird von der Klasse PatternAssociator verwendet.
 * @author Wolfgang Bongartz
 *
 */
public abstract class ActivationFunction implements RealVectorChangingVisitor {
	
	private ArrayRealVector      inputVector;
	private Array2DRowRealMatrix weigths;

	/**
	 * 
	 */
	public ActivationFunction() {
		setInputVector(null);
		setWeigths(null);
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.math3.linear.RealVectorChangingVisitor#end()
	 */
	@Override
	public double end() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.math3.linear.RealVectorChangingVisitor#start(int, int, int)
	 */
	@Override
	public void start(int arg0, int arg1, int arg2) {
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.math3.linear.RealVectorChangingVisitor#visit(int, double)
	 */
	@Override
	public double visit(int index, double currentValue) {

		double netInput = 0d;
		RealVector      inputWeights  = getWeigths().getColumnVector(index);
		
		// Berechne die bewerteten Eingangswerte des Output-Neurons (Propagierungsfunktion) durch Multiplikation des Input-Vektors mit der dem Output-Neuron entsprechenden Spalte der Gewichtungsmatrix.
		ArrayRealVector weightedInput = getInputVector().ebeMultiply(inputWeights);
		for(int i=0; i<weightedInput.getDimension(); i++) netInput+=weightedInput.getEntry(i);
		
		// Wende die Aktivierungsfunktion auf den Netto-Input an.
		double retVal = getFunctionResult(netInput);
		return retVal;
	}

	/**
	 * Liefert das Ergebnis der Aktivierungsfunktion für den Netto-Input eines Neurons.
	 * @param netInput 
	 * @return
	 */
	protected abstract double getFunctionResult(double netInput);
	
	/**
	 * @return the inputVector
	 */
	protected ArrayRealVector getInputVector() {
		return inputVector;
	}

	/**
	 * @param inputVector the inputVector to set
	 */
	public void setInputVector(ArrayRealVector inputVector) {
		this.inputVector = inputVector;
	}

	/**
	 * @return the weigths
	 */
	protected Array2DRowRealMatrix getWeigths() {
		return weigths;
	}

	/**
	 * @param weigths the weigths to set
	 */
	public void setWeigths(Array2DRowRealMatrix weigths) {
		this.weigths = weigths;
	}


}
