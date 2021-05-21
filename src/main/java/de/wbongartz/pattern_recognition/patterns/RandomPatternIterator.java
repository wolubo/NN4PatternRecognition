package de.wbongartz.pattern_recognition.patterns;

import java.util.*;

/**
 * Durchläuft alle Patterns in einem PatternSet in zufälliger Reihenfolge.
 * @author Wolfgang Bongartz
 */
public class RandomPatternIterator implements Iterator<Pattern> {
	
	private ArrayList<Pattern> _patterns;
	private Random             _randomNumberSource;
	
	public RandomPatternIterator(PatternSet patterns, Random randomNumberSource) {
		if(patterns==null) throw new IllegalArgumentException();
		if(randomNumberSource==null) throw new IllegalArgumentException();
		_randomNumberSource = randomNumberSource;
		_patterns = new ArrayList<Pattern>();
		for(Pattern p: patterns) {
			_patterns.add(p);
		}
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		return (_patterns.size() > 0);
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	@Override
	public Pattern next() {
		if(hasNext()) {
			int index = _randomNumberSource.nextInt(_patterns.size());
			Pattern theNextOne = _patterns.get(index);
			_patterns.remove(index);
			return theNextOne;
		}
		return null;
	}

}
