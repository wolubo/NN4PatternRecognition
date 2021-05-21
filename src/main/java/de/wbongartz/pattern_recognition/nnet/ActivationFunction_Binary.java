/**
 * 
 */
package de.wbongartz.pattern_recognition.nnet;

/**
 * Implementiert eine binäre Aktivierungsfunktion ohne Schwellwert.
 * @author Wolfgang Bongartz
 *
 */
public class ActivationFunction_Binary extends ActivationFunction {

	/* (non-Javadoc)
	 * @see nnet.ActivationFunction#getFunctionResult(double)
	 */
	@Override
	protected double getFunctionResult(double netInput) {
		if(netInput>=0d)
			return 1d;
		else
			return 0d;
	}

}
