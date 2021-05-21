package de.wbongartz.pattern_recognition.patterns;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import patterns.*;

/**
 * @author Wolfgang Bongartz
 *
 */
public class PatternSetTest {

	private Pattern _a, _b, _c, _x; 
	private Random _randomNumberSource;
	private ArrayList<Pattern> _patternArray;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		_randomNumberSource = new Random();

		_patternArray = new ArrayList<Pattern>();

		String[] a, b, c, x;
		a = new String[8]; 
		a[0]=".XXX..";
		a[1]="X...X.";
		a[2]="....X.";
		a[3]=".XXXX.";
		a[4]="X...X.";
		a[5]="X...X.";
		a[6]="X..XX.";
		a[7]=".XX.X.";
		_a = new Pattern("a", a);
		_patternArray.add(_a);

		b = new String[8]; 
		b[0]="X.....";
		b[1]="X.....";
		b[2]="X.....";
		b[3]="X.XX..";
		b[4]="XX..X.";
		b[5]="X...X.";
		b[6]="X...X.";
		b[7]="XXXX..";
		_b = new Pattern("b", b);
		_patternArray.add(_b);

		c = new String[8]; 
		c[0]="......";
		c[1]="..XXX.";
		c[2]=".X...X";
		c[3]="X.....";
		c[4]="X.....";
		c[5]="X.....";
		c[6]=".X...X";
		c[7]="..XXX.";
		_c = new Pattern("c", c);
		_patternArray.add(_c);

		x = new String[8]; 
		x[0]="......";
		x[1]="......";
		x[2]="X...X.";
		x[3]=".X.X..";
		x[4]="..X...";
		x[5]="..X...";
		x[6]=".X.X..";
		x[7]="X...X.";
		_x = new Pattern("x", x);
		_patternArray.add(_x);

	}

	@Test
	public void testPatternHandlerString() {
		try {
			PatternSet letters = new PatternSet(_patternArray, _randomNumberSource);
			if(letters.size()!=4) fail();
//			int counter=0;
//			for(Map.Entry<String, ArrayList<Pattern>> entry: letters) {
//				String current_name = entry.getKey();
//				if(current_name==null || current_name.length()==0) fail();
//				ArrayList<Pattern> current_list = entry.getValue();
//				for(Pattern p: current_list) {
//					if(p.get_height()!=8) fail();
//					if(p.get_width()!=8) fail();
//					counter++;
//				}
//			}
//			if(counter!=5) fail();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void  test_get_patternInt() {
		PatternSet letters=null;
		
		try {
			letters = new PatternSet(_patternArray, _randomNumberSource);
		} catch (Exception ex) {
			fail();
		}

		try {
			if( ! letters.get_pattern(0).equals(new Pattern(_a)) ) fail();
			if( ! letters.get_pattern(1).equals(new Pattern(_b)) ) fail();
			if( ! letters.get_pattern(2).equals(new Pattern(_c)) ) fail();
			if( ! letters.get_pattern(3).equals(new Pattern(_x)) ) fail();
		} catch (Exception ex) {
			fail();
		}

		try {
			letters.get_pattern(9);
			fail();
		} catch (Exception ex) {
		}
	}

	@Test
	public void testChooseRandomly() {
		try {
			PatternSet letters = new PatternSet(_patternArray, _randomNumberSource);
			Pattern entry = letters.chooseRandomly();
			if(entry==null) fail();
		} catch (Exception ex) {
			fail();
		}
		
	}

	@Test
	public void testGetListOfName() {
		try {
			PatternSet letters = new PatternSet(_patternArray, _randomNumberSource);
			String[] names = letters.getListOfName();
			if(names.length!=4) fail();
			if(names[0].compareTo("a")!=0) fail();
			if(names[1].compareTo("b")!=0) fail();
			if(names[2].compareTo("c")!=0) fail();
			if(names[3].compareTo("x")!=0) fail();
		} catch (Exception ex) {
			fail();
		}
	}
	
	//	@Test
//	public void  test_find_pattern() {
//		try {
//			PatternSet letters = new PatternSet(_patternArray);
//			if( letters.find_pattern(_a)!=1 ) fail();
//			if( letters.find_pattern(_b)!=0 ) fail();
//			if( letters.find_pattern(_c)!=2 ) fail();
//			if( letters.find_pattern(_x)!=4 ) fail();
//			if( letters.find_pattern(_unnamed)!=3 ) fail();
//		} catch (Exception ex) {
//			fail();
//		}		
//	}

}
