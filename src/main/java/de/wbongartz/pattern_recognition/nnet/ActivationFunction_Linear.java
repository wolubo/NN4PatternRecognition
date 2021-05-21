/**
 * 
 */
package de.wbongartz.pattern_recognition.nnet;

/**
 * Implementiert eine lineare Aktivierungsfunktion ohne Schwellwert und Anpassungsfaktor.
 * @author Wolfgang Bongartz
 *
 */
public class ActivationFunction_Linear extends ActivationFunction {

	/* (non-Javadoc)
	 * @see nnet.ActivationFunction#getFunctionResult(double)
	 */
	@Override
	protected double getFunctionResult(double netInput) {
		return netInput;
	}

}
