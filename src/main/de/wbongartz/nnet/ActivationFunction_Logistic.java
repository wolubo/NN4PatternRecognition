package de.wbongartz.pattern_recognition.nnet;

/**
 * Implementiert eine logistische Aktivierungsfunktion.
 * @author Wolfgang Bongartz
 *
 */
public class ActivationFunction_Logistic extends ActivationFunction {

	/* (non-Javadoc)
	 * @see nnet.ActivationFunction#getFunctionResult(double)
	 */
	@Override
	protected double getFunctionResult(double netInput) {
		double retVal;
		retVal = 1d / ( 1d + Math.exp(-netInput) );
		return retVal;
	}

}
