package de.wbongartz.pattern_recognition.nnet;

import java.util.*;

import de.wbongartz.pattern_recognition.patterns.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author Wolfgang Bongartz
 *
 */
public class PatternAssociatorTest {

	private Pattern pattern_a, pattern_b, pattern_c; 
	private PatternSet minimalPatternSet;
	private PatternSet fullPatternSet;
	private Random _randomNumberSource;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	public void setUp() throws Exception {
		String[] a_raw = { 
				".XXX..",
				"X...X.",
				"....X.",
				".XXXX.",
				"X...X.",
				"X..XX.",
				".XX.X."
		};
		this.pattern_a=new Pattern("a", a_raw);

		String[] b_raw = { 
				"X.....",
				"X.....",
				"X.....",
				"X.XX..",
				"XX..X.",
				"X...X.",
				"XXXX.."
		};
		this.pattern_b=new Pattern("b", b_raw);


		String[] c_raw = { 
				"..XXX.",
				".X...X",
				"X.....",
				"X.....",
				"X.....",
				".X...X",
				"..XXX."
		};
		this.pattern_c=new Pattern("c", c_raw);

		ArrayList<Pattern> minimal = new ArrayList<Pattern>();
		minimal.add(this.pattern_a);

		this.minimalPatternSet = new PatternSet(minimal, null);

		ArrayList<Pattern> full = new ArrayList<Pattern>();
		full.add(this.pattern_a);
		full.add(this.pattern_b);
		full.add(this.pattern_c);
		this.fullPatternSet = new PatternSet(full, null);
	}

	@Test
	public void testPatternAssociator() {
		try {
			@SuppressWarnings("unused")
			PatternAssociator pa = new PatternAssociator(42, 1, 0.5d, new ActivationFunction_Linear());
		} catch(Exception ex) {
			fail();
		}
	}

	@Test
	public void testMatch() {
		String answer;
		
		// Call whitout any training.
		try {
			PatternAssociator pa = new PatternAssociator(42, 1, 0.5d, new ActivationFunction_Linear());
			pa.match(this.pattern_a);
			fail();
		} catch(NeuralNetworkIsUntrainedException ex) {
		} catch(Exception ex) {
			fail();
		}

		try {
			PatternAssociator pa = new PatternAssociator(42, 1, 0.5d, new ActivationFunction_Linear());
			pa.train(this.minimalPatternSet, false, 1, null);
			answer = pa.match(this.pattern_a);
			if(answer.compareTo("a")!=0) fail();
		} catch(Exception ex) {
			fail();
		}

		try {
			PatternAssociator pa = new PatternAssociator(42, 3, 0.5d, new ActivationFunction_Linear());
			
			pa.train(this.fullPatternSet, true, 1, null);
			
			answer = pa.match(this.pattern_a);
			if(answer.compareTo("a")!=0) fail();
			
			answer = pa.match(this.pattern_b);
			if(answer.compareTo("b")!=0) fail();
			
			answer = pa.match(this.pattern_c);
			if(answer.compareTo("c")!=0) fail();
		} catch(Exception ex) {
			fail();
		}
	}

	@Test
	public void testTrain() {
		try {
			PatternAssociator pa = new PatternAssociator(42, 1, 0.5d, new ActivationFunction_Linear());
			pa.train(minimalPatternSet, true, 1, null);
		} catch(Exception ex) {
			fail();
		}

		try {
			PatternAssociator pa = new PatternAssociator(42, 1, 0.5d, new ActivationFunction_Linear());
			pa.train(minimalPatternSet, false, 1, null);
		} catch(Exception ex) {
			fail();
		}

		try {
			PatternAssociator pa = new PatternAssociator(42, 1, 0.5d, new ActivationFunction_Linear());
			pa.train(minimalPatternSet, true, 1, _randomNumberSource);
		} catch(Exception ex) {
			fail();
		}

		try {
			PatternAssociator pa = new PatternAssociator(42, 3, 0.5d, new ActivationFunction_Linear());
			pa.train(fullPatternSet, true, 100, _randomNumberSource);
		} catch(Exception ex) {
			fail();
		}
	}

}
