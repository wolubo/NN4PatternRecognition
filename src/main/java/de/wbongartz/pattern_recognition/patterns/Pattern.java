package de.wbongartz.pattern_recognition.patterns;

import java.util.Arrays;
import java.util.Random;

/**
 * Kapselt ein Bitmuster.
 * @author Wolfgang Bongartz
 */
public class Pattern implements Cloneable {

	private boolean[][] _pattern;
	private String      _name;
	private int 	    _height;
	private int 	    _width;

	@SuppressWarnings("unused")
	private Pattern() {
	}

	/**
	 * Konstruktor
	 * @param newPattern
	 */
	public Pattern(String name, boolean[][] newPattern) {
		if(newPattern==null||newPattern.length==0) throw new IllegalArgumentException("Bitmuster ist unvollständig!");
		if(name==null || name.length()==0) throw new IllegalArgumentException();
		_name=name;
		_height  = newPattern.length;
		_width   = newPattern[0].length;
		_pattern = new boolean[_height][_width];
		for(int i=0; i<_height; i++) {
			_pattern[i] = Arrays.copyOf(newPattern[i], _width);
		}
	}

	public Pattern(String name, String[] newPattern) {
		this(name, convertString2BooleanArray(newPattern));
	}
	
	/**
	 * @param newPattern
	 * @return
	 */
	private static boolean[][] convertString2BooleanArray(String[] newPattern) {
		if(newPattern==null||newPattern.length==0) throw new IllegalArgumentException("Bitmuster ist unvollständig!");
		int height = newPattern.length;
		int width  = newPattern[0].length();
		boolean[][] pattern = new boolean[height][width];
		for(int i=0; i<height; i++) {
			String rowStr = newPattern[i];
			for(int j=0; j<width; j++) {
				pattern[i][j] = rowStr.charAt(j)=='X';
			}
		}
		return pattern;
	}

	/**
	 * Copy-Konstruktor
	 * @param other
	 */
	public Pattern(Pattern other) {
		this(other._name, other._pattern);
	}

	/**
	 * 
	 */
	public Object clone()  {
		Pattern retVal=new Pattern(this); 
		return retVal;
	}

	/**
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean get(int row, int col) {
		return _pattern[row][col];
	}

	/**
	 * 
	 * @param row
	 * @param col
	 * @param value
	 */
	public void set(int row, int col, boolean value) {
		_pattern[row][col]=value;
	}

	/**
	 * Vertauscht den Inhalt zweier zufällig ausgewählter Zellen des Bitmusters.
	 * @param randomNumberSource
	 * @return
	 */
	public Pattern swapRandomly(Random randomNumberSource) {
		if(randomNumberSource==null) randomNumberSource = new Random();

		Pattern retVal = (Pattern) this.clone();

		int row1, row2, col1, col2;

		row1 = randomNumberSource.nextInt(_height);
		col1 = randomNumberSource.nextInt(_width);

		do {
			row2 = randomNumberSource.nextInt(_height);
			col2 = randomNumberSource.nextInt(_width);
		} while (row1==row2 && col1==col2);

		boolean temp = retVal.get(row1, col1);
		retVal.set(row1, col1, retVal.get(row2, col2));
		retVal.set(row2, col2, temp);

		return retVal;
	}

	/**
	 * Kehrt den Inhalt einer zufällig ausgewählten Zelle um.
	 * @param randomNumberSource
	 * @return
	 */
	public Pattern changeRandomly(Random randomNumberSource) {
		if(randomNumberSource==null) randomNumberSource = new Random();

		Pattern retVal = (Pattern) this.clone();

		int row, col;

		row = randomNumberSource.nextInt(_height);
		col = randomNumberSource.nextInt(_width);
		
		retVal.set( row, col, ! get(row,col) );

		return retVal;
	}

	/**
	 * Spiegelt das Bitmuster an der Hozizontalen.
	 */
	public Pattern flipHorizontally() {
		// TODO Horizontales Spiegeln einbauen
		throw new IllegalStateException("Not implemented yet!");
	}

	/**
	 * Spiegelt das Bitmuster an der Vertikalen.
	 */
	public Pattern flipVertically() {
		// TODO Vertikales Spiegeln einbauen
		throw new IllegalStateException("Not implemented yet!");
	}

	/**
	 * Spiegelt das Bitmuster an der Diagonalen.
	 * @param direction 0: Die linke obere Ecke wird zur rechten unteren Ecke.
	 * 					1: Die linke untere Ecke wird zu rechten oberen Ecke.
	 */
	public Pattern flipDiagonally(int direction) {
		// TODO Diagonales Spiegeln einbauen
		throw new IllegalStateException("Not implemented yet!");
	}

	/**
	 * @return Höhe des Bitmusters
	 */
	public int get_height() {
		return _height;
	}

	/**
	 * @return Breite des Bitmusters.
	 */
	public int get_width() {
		return _width;
	}

	/**
	 * @return Name des Bitmusters.
	 */
	public String get_name() {
		return _name;
	}

	/**
	 * Zählt die Unterschiede zwischen zwei Bitmustern.
	 * @param other
	 * @return Anzahl der Unterschiede zwischen zwei Bitmustern.
	 */
	public int differences(Pattern other) {
		if(other==null) throw new IllegalArgumentException();
		if(this==other) return 0;
		if(this.get_height()!=other.get_height())  throw new IllegalArgumentException();
		if(this.get_width()!=other.get_width())  throw new IllegalArgumentException();

		int retVal=0;
		boolean l, r;
		for(int row=0; row<_height; row++) {
			for(int col=0; col<_width; col++) {
				l = this.get(row,col);
				r = other.get(row,col);
				if(l!=r) retVal++;
			}
		}

		return retVal;
	}

	@Override
	public boolean equals(Object obj) {
		if(this==obj) return true;
		if(obj==null) return false;
		if( ! (obj instanceof Pattern) ) return false;

		Pattern other = (Pattern) obj;
		if(this.get_height()!=other.get_height()) return false;
		if(this.get_width()!=other.get_width())   return false;
		if(this._name.compareTo(other._name)!=0)  return false;

		return ( differences(other)==0 );
	}

	@Override
	public String toString() {
		String retVal = _name + "\n";
		for(int row=0; row<_height; row++) {
			for(int col=0; col<_width; col++) {
				if(get(row,col))
					retVal+="X";
				else
					retVal+=".";
			}
			retVal+="\n";
		}
		return retVal;
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}

}
