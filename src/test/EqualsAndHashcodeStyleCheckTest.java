
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import domain.message.LinterMessage;
import domain.AnalyzerReturn;
import domain.analyzer.EqualsAndHashcodeAnalyzer;

public class EqualsAndHashcodeStyleCheckTest {

	private EqualsAndHashcodeAnalyzer analyzer;

	@Test
	public void testNeitherMethodOverridden() {
		String[] classList = { "example/equalhashstyle/WithNeitherTestClass" };
		this.analyzer = new EqualsAndHashcodeAnalyzer(classList);
		AnalyzerReturn returnVal = this.analyzer.getFeedback(classList);
		List<LinterMessage> errors = returnVal.errorsCaught;
		assertTrue(errors.size() == 0);
		assertTrue(returnVal.analyzerName == "Equals And Hashcode Override Check");
	}

	@Test
	public void testEqualsOnlyMethodOverridden() {
		String[] classList = { "example/equalhashstyle/WithEqualsOnlyTestClass" };
		this.analyzer = new EqualsAndHashcodeAnalyzer(classList);
		AnalyzerReturn returnVal = this.analyzer.getFeedback(classList);
		List<LinterMessage> errors = returnVal.errorsCaught;
		assertTrue(errors.size() == 1);
		assertEquals("When overriding the equals method, you should also override the hashCode method ",
				errors.get(0).message);
		assertTrue(returnVal.analyzerName == "Equals And Hashcode Override Check");

	}

	@Test
	public void testHashCodeOnlyMethodOverridden() {
		String[] classList = { "example/equalhashstyle/WithHashCodeOnlyTestClass" };
		this.analyzer = new EqualsAndHashcodeAnalyzer(classList);
		AnalyzerReturn returnVal = this.analyzer.getFeedback(classList);
		List<LinterMessage> errors = returnVal.errorsCaught;
		assertTrue(errors.size() == 1);
		assertEquals("When overriding the hashCode method, you should also override the equals method ",
				errors.get(0).message);
		assertTrue(returnVal.analyzerName == "Equals And Hashcode Override Check");

	}

	@Test
	public void testBothMethodsOverridden() {
		String[] classList = { "example/equalhashstyle/WithBothTestClass" };
		this.analyzer = new EqualsAndHashcodeAnalyzer(classList);
		AnalyzerReturn returnVal = this.analyzer.getFeedback(classList);
		List<LinterMessage> errors = returnVal.errorsCaught;
		assertTrue(errors.size() == 0);
		assertTrue(returnVal.analyzerName == "Equals And Hashcode Override Check");
	}

}
