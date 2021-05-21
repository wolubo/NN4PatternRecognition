package de.wbongartz.pattern_recognition.nnet;

import org.apache.commons.math3.linear.*;

/**
 * Implementiert die Lernregel "Delta-Regel". Wird von der Klasse PatternAssociator benutzt.
 * @author Wolfgang Bongartz
 *
 */
public class DeltaRule implements RealVectorPreservingVisitor {
	
	private ArrayRealVector      _inputVector;
	private ArrayRealVector 	 _targetVector;
	private Array2DRowRealMatrix _deltaWeigths;
	private double				 _epsilon;

	/**
	 * @param inputVector  Enthält das aktuell zu lernende Bitmuster.
	 * @param targetVector Enthält die gewünschten Aktivierungszustände der Output-Neuronen.
	 * @param deltaWeigths Enthält zu jedem Gewicht die ermittelten Delta-Werte, die später beim Update verwendet werden.
	 */
	public DeltaRule(ArrayRealVector inputVector, ArrayRealVector targetVector, Array2DRowRealMatrix deltaWeigths, double epsilon) {
		_inputVector  = inputVector;
		_targetVector = targetVector;
		_deltaWeigths = deltaWeigths;
		_epsilon      = epsilon;
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.math3.linear.RealVectorPreservingVisitor#end()
	 */
	@Override
	public double end() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.math3.linear.RealVectorPreservingVisitor#start(int, int, int)
	 */
	@Override
	public void start(int arg0, int arg1, int arg2) {
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.math3.linear.RealVectorPreservingVisitor#visit(int, double)
	 */
	@Override
	public void visit(int index, double value) {
		double delta = ( _targetVector.getEntry(index) - value ) * _epsilon;
		RealVector newDeltaWeigths = _inputVector.mapMultiply(delta);
		RealVector currentDeltaWeights = _deltaWeigths.getColumnVector(index);
		currentDeltaWeights = currentDeltaWeights.add(newDeltaWeigths);
		_deltaWeigths.setColumnVector(index, currentDeltaWeights);
	}

}
