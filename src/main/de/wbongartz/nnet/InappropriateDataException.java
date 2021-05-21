package de.wbongartz.pattern_recognition.nnet;

/**
 * Exception die geworfen wird, falls versucht wird, dem Netz Trainingsdaten zu präsentieren, die mehr Output-Neuronen erfordern als tatsächlich vorhanden sind.
 * @author Wolfgang Bongartz
 *
 */
public class InappropriateDataException extends Exception {
	
	public InappropriateDataException(String msg) {
		super(msg);
	}

	private static final long serialVersionUID = -1050265697766935045L;

}
