package de.wbongartz.pattern_recognition.nnet;

import org.apache.commons.math3.linear.RealMatrixChangingVisitor;

/**
 * Setzt alle Zellen einer Matrix auf den im Konstruktor übergebenen Wert.
 * @author Wolfgang Bongartz
 */
public class SetValueVisitor implements RealMatrixChangingVisitor {
	
	private double _value;
	
	/**
	 * 
	 * @param value Wert, auf den alle Zellen der Matrix gesetzt werden sollen.
	 */
	public SetValueVisitor(double value) {
		_value = value;
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.math3.linear.RealMatrixChangingVisitor#end()
	 */
	@Override
	public double end() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.math3.linear.RealMatrixChangingVisitor#start(int, int, int, int, int, int)
	 */
	@Override
	public void start(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.math3.linear.RealMatrixChangingVisitor#visit(int, int, double)
	 */
	@Override
	public double visit(int arg0, int arg1, double arg2) {
		return _value;
	}

}
