package de.wbongartz.pattern_recognition.nnet;

/**
 * Implementiert die Aktivierungsfunktion 'Tangens Hyperbolicus'.
 * @author Wolfgang Bongartz
 *
 */
public class ActivationFunction_TangensHyperbolicus extends ActivationFunction {
	
	/**
	 */
	public ActivationFunction_TangensHyperbolicus() {
		super();
	}

	/* (non-Javadoc)
	 * @see nnet.ActivationFunction#getFunctionResult(double)
	 */
	@Override
	protected double getFunctionResult(double netInput) {
		return Math.tanh(netInput); // Tangens Hyperbolicus
	}

}
