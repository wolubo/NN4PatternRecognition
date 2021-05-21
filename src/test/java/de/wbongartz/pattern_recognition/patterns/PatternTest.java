package de.wbongartz.pattern_recognition.patterns;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.fail;


/**
 * @author Wolfgang Bongartz
 *
 */
public class PatternTest {

	private String[] _raw, _empty;
	private Random _randomNumberSource;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	public void setUp() throws Exception {
		
		_randomNumberSource = new Random();
		
		_raw = new String[8]; 
		_raw[0]=".X......";
		_raw[1]=".X......";
		_raw[2]=".X......";
		_raw[3]=".X.XX...";
		_raw[4]=".XX..X..";
		_raw[5]=".X...X..";
		_raw[6]=".X...X..";
		_raw[7]=".XXXX...";

		_empty = new String[8]; 
		_empty[0]="........";
		_empty[1]="........";
		_empty[2]="........";
		_empty[3]="........";
		_empty[4]="........";
		_empty[5]="........";
		_empty[6]="........";
		_empty[7]="........";
	}

	@SuppressWarnings("unused")
	@Test
	public void testPatternStringArray() {
		try {
			Pattern pattern = new Pattern("b", _raw);
		} catch (Exception ex) {
			fail();
		}
	}

	@SuppressWarnings("unused")
	@Test
	public void testPatternPattern() {
		try {
			Pattern pattern = new Pattern("b", _raw);
			Pattern pattern2 = new Pattern(pattern);
		} catch (Exception ex) {
			fail();
		}
	}

	@Test
	public void testGet() {
		Pattern pattern = new Pattern("b", _raw);

		try {
			pattern.get(0, 0);
		} catch (Exception ex) {
			fail();
		}

		try {
			pattern.get(7, 7);
		} catch (Exception ex) {
			fail();
		}

		try {
			pattern.get(0,  9);
			fail();
		} catch (Exception ex) {
		}

		try {
			pattern.get(9,  0);
			fail();
		} catch (Exception ex) {
		}

		try {
			for(int i=0; i<8; i++) {
				for(int j=0; j<8; j++) {
					if(pattern.get(i,j)!= ( _raw[i].charAt(j)=='X') ) fail();
				}
			}
		} catch (Exception ex) {
			fail();
		}

	}

	@Test
	public void testClone() {
		Pattern pattern = new Pattern("b", _raw);
		try {
			Pattern clone = (Pattern) pattern.clone();
			
			if(pattern==clone) fail();
			
			for(int i=0; i<8; i++) {
				for(int j=0; j<8; j++) {
					if(pattern.get(i,j)!= ( _raw[i].charAt(j)=='X') ) fail();
				}
			}
			
			// Does the clone influence the original object?
			clone.set(0, 0, true);
			if( pattern.get(0, 0) ) fail();
		} catch (Exception ex) {
			fail();
		}

	}

	@Test
	public void testSet() {
		Pattern pattern = new Pattern("empty", _empty);

		try {
			pattern.set(0,  0, true);
		} catch (Exception ex) {
			fail();
		}

		try {
			pattern.set(7,  7, true);
		} catch (Exception ex) {
			fail();
		}

		try {
			for(int i=0; i<8; i++) {
				for(int j=0; j<8; j++) {
					pattern.set(i,j, true);
					if( ! pattern.get(i, j) ) fail();
				}
			}
		} catch (Exception ex) {
			fail();
		}

		try {
			pattern.get(0,  9);
			fail();
		} catch (Exception ex) {
		}

		try {
			pattern.get(9,  0);
			fail();
		} catch (Exception ex) {
		}
	}

	@Test
	public void testGet_height() {
		Pattern pattern = new Pattern("b", _raw);
		if( pattern.get_height()!=8 ) fail();
	}

	@Test
	public void testGet_width() {
		Pattern pattern = new Pattern("b", _raw);
		if( pattern.get_width()!=8 ) fail();
	}

	@Test
	public void testDifferences() {
		Pattern pattern1 = new Pattern("empty", _empty);
		Pattern pattern2 = new Pattern(pattern1);

		try {
			if( pattern1.differences(pattern1) != 0) fail();
			if( pattern1.differences(pattern2) != 0) fail();
			if( pattern2.differences(pattern1) != 0) fail();
			if( pattern2.differences(pattern2) != 0) fail();

			pattern1.set(4, 4, true);
			if( pattern1.differences(pattern2) != 1) fail();
			if( pattern2.differences(pattern1) != 1) fail();

			pattern1.set(0, 0, true);
			if( pattern1.differences(pattern2) != 2) fail();
			if( pattern2.differences(pattern1) != 2) fail();

			pattern1.set(7, 7, true);
			if( pattern1.differences(pattern2) != 3) fail();
			if( pattern2.differences(pattern1) != 3) fail();
		} catch(Exception ex) {
			fail();
		}

		try {
			pattern1.differences(null);
			fail();
		} catch(Exception ex) {
		}
	}

	@Test
	public void testEqualsObject() {
		try {
			Pattern pattern1 = new Pattern("b", _raw);
			Pattern pattern2 = new Pattern(pattern1);

			if( ! pattern1.equals(pattern1)) fail();
			if( ! pattern1.equals(pattern2)) fail();
			if( ! pattern2.equals(pattern1)) fail();
			if( ! pattern2.equals(pattern2)) fail();
			if( pattern1.equals(null)) fail();

			pattern1.set(1, 1, false);
			if( pattern1.equals(pattern2)) fail();
		} catch(Exception ex) {
			fail();
		}
	}

	@Test
	public void testToString() {
		try {
			Pattern pattern = new Pattern("b", _raw);
			pattern.toString();
		} catch(Exception ex) {
			fail();
		}
	}

	@Test
	public void testHashCode() {
		Pattern pattern = new Pattern("b", _raw);
		Pattern pattern2 = new Pattern(pattern);
		try {
			if( pattern.hashCode() != pattern2.hashCode() ) fail();
		} catch(Exception ex) {
			fail();
		}
	}

	@Test
	public void swapRandomly() {
		Pattern pattern1 = new Pattern("b", _raw);
		Pattern pattern2;
		try {
			pattern2 = pattern1.swapRandomly(_randomNumberSource);
			if(pattern1.differences(pattern2)>2) fail();
		} catch(Exception ex) {
			fail();
		}
	}
	
	@Test
	public void changeRandomly() {
		Pattern pattern1 = new Pattern("b", _raw);
		Pattern pattern2;

		try {
			pattern2=pattern1.changeRandomly(_randomNumberSource);
			if(pattern1.differences(pattern2)!=1) fail();
			pattern2=pattern2.changeRandomly(_randomNumberSource);
			if(pattern1.differences(pattern2)!=2) fail();
		} catch(Exception ex) {
			fail();
		}
	}

	@Test
	public void flipHorizontally() {
		Pattern pattern = new Pattern("b", _raw);
		try {
			pattern.flipHorizontally();
			fail();
		} catch(Exception ex) {
		}
	}

	@Test
	public void flipVertically() {
		Pattern pattern = new Pattern("b", _raw);
		try {
			pattern.flipVertically();
			fail();
		} catch(Exception ex) {
		}
	}

	@Test
	public void flipDiagonally() {
		Pattern pattern = new Pattern("b", _raw);
		try {
			pattern.flipDiagonally(0);
			fail();
		} catch(Exception ex) {
		}
	}
	
}
