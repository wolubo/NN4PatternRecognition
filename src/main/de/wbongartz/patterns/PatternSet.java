package de.wbongartz.pattern_recognition.patterns;

import java.util.*;

/**
 * Verwalter für Bitmuster-Objekte.
 * @author Wolfgang Bongartz
 *
 */
public class PatternSet implements Iterable<Pattern> {

	private ArrayList<Pattern> _patterns;
	private Random _randomNumberSource;

	/**
	 * 
	 * @param patterns
	 * @param randomNumberSource Wird von einigen Methoden benötigt. 
	 * Falls NULL wird ein eigenes Random-Objekt erzeugt.
	 */
	public PatternSet(ArrayList<Pattern> patterns, Random randomNumberSource) {
		_randomNumberSource = randomNumberSource;
		if(_randomNumberSource==null) _randomNumberSource = new Random();
		if(patterns==null) throw new IllegalArgumentException();
		_patterns = patterns;
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Pattern> iterator() {
		return _patterns.iterator();
	}

	/**
	 * Liefert ein Bitmuster.
	 * @param index Index des zu liefernden Bitmusters.
	 * @return
	 */
	public Pattern get_pattern(int index) {
		if(index<0 || index>_patterns.size()) throw new IllegalArgumentException();
		return _patterns.get(index);
	}

	/**
	 * Liefert die Gesamtanzahl der derzeit gespeicherten Bitmuster.
	 * @return
	 */
	public int size() {
		return _patterns.size();
	}

	/**
	 * Liefert ein zufällig ausgewähltes Bitmuster.
	 * @return
	 */
	public Pattern chooseRandomly() {
		int key_index = _randomNumberSource.nextInt(_patterns.size());
		Pattern retVal = _patterns.get(key_index);
		return retVal;
	}

	/**
	 * Erzeugt ein neues PatternSet-Objekt, dessen Inhalt aus zufällig ausgewählten und nach dem 
	 * Zufallsprinzip veränderten Bitmustern besteht.
	 * @param numberOfPatterns Anzahl der zu erzeugenden Bitmuster.
	 * @param numberOfChanges Die Anzahl zufälliger Veränderungen, die auf jedes Bitmuster höchstens angewendet werden soll. 
	 * @param include_templates 
	 * TRUE: Das neue PatternSet enthält zusätzlich auch die unveränderten Original-Bitmuster. 
	 * FALSE: Das Ergebnis enthält ausschließlich abgeleitete Bitmuster. 
	 * @return
	 */
	public PatternSet createRandomized(int numberOfPatterns, int numberOfChanges, boolean include_templates) {

		ArrayList<Pattern> retVal = new ArrayList<Pattern>();

		for(int i=0; i<numberOfPatterns; i++) {

			// Wähle das Bitmuster aus, das als Vorlage verwendet werden soll.
			Pattern original = chooseRandomly();
			Pattern changed = (Pattern) original.clone();

			if(numberOfChanges>0) {
				// Bestimme die Anzahl der auf die Vorlage anzuwendenden Veränderungen.
				int changes = _randomNumberSource.nextInt(numberOfChanges+1);

				// Führe die Veränderungen durch.
				for(int j=0; j<changes; j++) {
					changed = changed.changeRandomly(_randomNumberSource);
				}
			}

			retVal.add(changed);
		}

		assert(retVal.size()==numberOfPatterns);

		if(include_templates) {
			for(Pattern p: this) {
				retVal.add(p);
			}
		}

		return new PatternSet(retVal, this._randomNumberSource);
	}

	/**
	 * Liefert eine Liste aller vorkommenden Bitmuster-Namen.
	 * @return
	 */
	public String[] getListOfName() {
		HashSet<String> names = new HashSet<String>();
		for(Pattern p: this) {
			names.add(p.get_name());
		}
		String[] retVal = new String[names.size()];
		return names.toArray(retVal);
	}
	
	/**
	 * Liefert die für das aktuelle PatternSet gültige Anzahl relevanter Bits.
	 * Das sind die Bits, in denen sich wenigstens zwei Muster voneinander unterscheiden.
	 * @return
	 */
	public int getNumberOfRelevantBits() {
		if(_patterns.size()==0) throw new IllegalArgumentException();
		int patternSize=_patterns.get(0).get_height() * _patterns.get(0).get_width(); 
		boolean[] relevantNullBits = new boolean[patternSize];
		boolean[] relevantOneBits  = new boolean[patternSize];
		
		// Initialisieren.
		for(int i=0; i<patternSize; i++) {
			relevantNullBits[i]=false;
			relevantOneBits[i]=true;
		}
		
		// Alle gespeicherten Bitmuster durchgehen.
		boolean value;
		for(Pattern p: _patterns) {
			for(int i=0; i<p.get_height(); i++) {
				for(int j=0; j<p.get_width(); j++) {
					value = p.get(i, j);
					if( value && ! relevantNullBits[i*j] ) relevantNullBits[i*j] = true;
					if( ! value && relevantOneBits[i*j]  ) relevantOneBits[i*j]  = false;
				}
			}
		}

		// Auszählen.
		int relevantBits = 0;
		for(int i=0; i<patternSize; i++) {
			if(   relevantNullBits[i] ) relevantBits++; // Jedes mit TRUE ist relevant.
			if( ! relevantOneBits[i]  ) relevantBits++; // Jedes mit FALSE ist relevant.
		}
		
		return relevantBits;
	}

}
