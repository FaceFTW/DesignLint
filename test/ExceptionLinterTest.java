import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import datasource.ASMParser;
import domain.ExceptionThrownAnalyzer;
import domain.ReturnType;
import domain.ExceptionThrownAnalyzer.ExceptionThrownIssue;

public class ExceptionLinterTest {
	private ASMParser parser;
	private final String[] exampleClasses = { "example.exceptionstyle.ExceptionStyleExamples" };

	// We use an explicit instance to test the protected method checkViolation()
	private ExceptionThrownAnalyzer analyzer;

	// Common Testing Setup
	public void setupAnalyzer() {
		try {
			this.parser = new ASMParser(exampleClasses);
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.analyzer = new ExceptionThrownAnalyzer(parser);
		analyzer.getRelevantData(exampleClasses);
	}

	// =========================Compliant Methods=========================//
	@Category({ ExceptionLinterTests.class, ExceptionLinterCompliantTest.class })
	@Test
	public void testCompliantMethodThrowsException() {
		// Expected List of Issues
		List<ExceptionThrownIssue> expected = new ArrayList<>();
		expected.add(ExceptionThrownIssue.NO_VIOLATION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0], "compliantMethodThrowsException"));
	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterCompliantTest.class })
	@Test
	public void testCompliantMethodCallThrowsException() {
		// Expected List of Issues
		List<ExceptionThrownIssue> expected = new ArrayList<>();
		expected.add(ExceptionThrownIssue.NO_VIOLATION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0],
				"compliantMethodCallThrowsException"));
	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterCompliantTest.class })
	@Test
	public void testCompliantMethodThrowsMultipleException() {
		// Expected List of Issues
		List<ExceptionThrownIssue> expected = new ArrayList<>();
		expected.add(ExceptionThrownIssue.NO_VIOLATION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0],
				"compliantMethodThrowsMultipleExceptions"));
	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterCompliantTest.class })
	@Test
	public void testCompliantMethodCatchException() {
		// Expected List of Issues
		List<ExceptionThrownIssue> expected = new ArrayList<>();
		expected.add(ExceptionThrownIssue.NO_VIOLATION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0], "compliantMethodCatchException"));

	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterCompliantTest.class })
	@Test
	public void testcompliantMethodCallCatchException() {
		// Expected List of Issues
		List<ExceptionThrownIssue> expected = new ArrayList<>();
		expected.add(ExceptionThrownIssue.NO_VIOLATION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0],
				"compliantMethodCallCatchException"));
	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterCompliantTest.class })
	@Test
	public void testCompliantMethodCatchMultipleException() {
		// Expected List of Issues
		List<ExceptionThrownIssue> expected = new ArrayList<>();
		expected.add(ExceptionThrownIssue.NO_VIOLATION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0],
				"compliantMethodCatchMultipleException"));
	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterCompliantTest.class })
	@Test
	public void testCompliantMethodCallCatchMultipleException() {
		// Expected List of Issues
		List<ExceptionThrownIssue> expected = new ArrayList<>();
		expected.add(ExceptionThrownIssue.NO_VIOLATION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0],
				"compliantMethodCallCatchMultipleException"));
	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterCompliantTest.class })
	@Test
	public void testCompliantMethodThrowsAndCatchesExceptions() {
		// Expected List of Issues
		List<ExceptionThrownIssue> expected = new ArrayList<>();
		expected.add(ExceptionThrownIssue.NO_VIOLATION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0],
				"compliantMethodThrowsAndCatchesException"));
	}

	// ================== Non-Compliant Methods ===================//
	@Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
	@Test
	public void testNonCompliantMethodThrowsException() {
		// Expected List of Issues
		List<ExceptionThrownIssue> expected = new ArrayList<>();
		expected.add(ExceptionThrownIssue.THROW_EXCEPTION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0],
				"nonCompliantMethodThrowsException"));
	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
	@Test
	public void testNonCompliantMethodThrowsRuntimeException() {
		// Expected List of Issues
		List<ExceptionThrownIssue> expected = new ArrayList<>();
		expected.add(ExceptionThrownIssue.THROW_RUNTIME_EXCEPTION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0],
				"nonCompliantMethodThrowsRuntimeException"));
	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
	@Test
	public void testNonCompliantMethodThrowsError() {
		// Expected List of Issues
		List<ExceptionThrownIssue> expected = new ArrayList<>();
		expected.add(ExceptionThrownIssue.THROW_ERROR);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0], "nonCompliantMethodThrowsError"));
	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
	@Test
	public void testNonCompliantMethodThrowsThrowable() {
		// Expected List of Issues
		List<ExceptionThrownIssue> expected = new ArrayList<>();
		expected.add(ExceptionThrownIssue.THROW_THROWABLE);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0],
				"nonCompliantMethodThrowsThrowable"));
	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
	@Test
	public void testNonCompliantMethodCallsCompliantButThrowsException() {
		// Expected List of Issues
		List<ExceptionThrownIssue> expected = new ArrayList<>();
		expected.add(ExceptionThrownIssue.THROW_EXCEPTION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0],
				"nonCompliantMethodCallsCompliantButThrowsException"));
	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
	@Test
	public void testNonCompliantMethodCallsCompliantWithMultipleThrowsButThrowsException() {
		// Expected List of Issues
		List<ExceptionThrownIssue> expected = new ArrayList<>();
		expected.add(ExceptionThrownIssue.THROW_EXCEPTION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0],
				"nonCompliantMethodCallsCompliantWithMultipleThrowsButThrowsException"));
	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
	@Test
	public void testNonCompliantMethodCatchesException() {
		// Expected List of Issues
		List<ExceptionThrownIssue> expected = new ArrayList<>();
		expected.add(ExceptionThrownIssue.CATCH_EXCEPTION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0],
				"nonCompliantMethodCatchesException"));
	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
	@Test
	public void testNonCompliantMethodCallsCompliantButCatchesException() {
		// Expected List of Issues
		List<ExceptionThrownIssue> expected = new ArrayList<>();
		expected.add(ExceptionThrownIssue.CATCH_EXCEPTION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0],
				"nonCompliantMethodCallsCompliantButCatchesException"));
	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
	@Test
	public void testNonCompliantMethodCallsCompliantWithMultipleThrowsButCatchesException() {
		// Expected List of Issues
		List<ExceptionThrownIssue> expected = new ArrayList<>();
		expected.add(ExceptionThrownIssue.CATCH_EXCEPTION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0],
				"nonCompliantMethodCallsCompliantWithMultipleThrowsButCatchesException"));
	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
	@Test
	public void testNonCompliantMethodCatchMultipleExceptions() {
		// Expected List of Issues
		List<ExceptionThrownIssue> expected = new ArrayList<>();
		expected.add(ExceptionThrownIssue.CATCH_EXCEPTION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0],
				"nonCompliantMethodCatchMultipleExceptions"));

	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
	@Test
	public void testNonCompliantMethodThrowsAndCatchesCompliantException() {
		// Expected List of Issues
		List<ExceptionThrownIssue> expected = new ArrayList<>();
		expected.add(ExceptionThrownIssue.THROW_EXCEPTION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0],
				"nonCompliantMethodThrowsAndCatchesCompliantException"));
	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
	@Test
	public void testNonCompliantMethodThrowsCompliantAndCatchesException() {
		// Expected List of Issues
		List<ExceptionThrownIssue> expected = new ArrayList<>();
		expected.add(ExceptionThrownIssue.CATCH_EXCEPTION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0],
				"nonCompliantMethodThrowsCompliantAndCatchesException"));
	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
	@Test
	public void testNonCompliantMethodThrowsAndCatchesException() {
		// Expected List of Issues
		List<ExceptionThrownIssue> expected = new ArrayList<>();
		expected.add(ExceptionThrownIssue.THROW_EXCEPTION);
		expected.add(ExceptionThrownIssue.CATCH_EXCEPTION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0],
				"nonCompliantMethodThrowsAndCatchesException"));

	}

	@Category({ ExceptionLinterTests.class })
	@Test
	public void testExceptionAnalyzerReturnType() {
		setupAnalyzer();

		analyzer.analyzeData();

		ReturnType expectedReturnType = analyzer.composeReturnType();

		assertEquals(expectedReturnType.analyzerName, "Generic Exception Linter");
		assertEquals(expectedReturnType.errorsCaught.size(), 14);
		assertEquals(expectedReturnType.errorsCaught.get(0),
				"example.exceptionstyle.ExceptionStyleExamples.nonCompliantMethodThrowsException() throws java.lang.Exception in its method signature instead of a specific exception class");
		assertEquals(expectedReturnType.errorsCaught.get(1),
				"example.exceptionstyle.ExceptionStyleExamples.nonCompliantMethodThrowsRuntimeException() throws java.lang.RuntimeException in its method signature instead of a specific exception class");
		assertEquals(expectedReturnType.errorsCaught.get(2),
				"example.exceptionstyle.ExceptionStyleExamples.nonCompliantMethodThrowsError() throws java.lang.Error in its method signature instead of a specific exception class");
		assertEquals(expectedReturnType.errorsCaught.get(3),
				"example.exceptionstyle.ExceptionStyleExamples.nonCompliantMethodThrowsThrowable() throws java.lang.Throwable in its method signature instead of a specific exception class");
		assertEquals(expectedReturnType.errorsCaught.get(4),
				"example.exceptionstyle.ExceptionStyleExamples.nonCompliantMethodCallsCompliantButThrowsException() throws java.lang.Exception in its method signature instead of a specific exception class");
		assertEquals(expectedReturnType.errorsCaught.get(5),
				"example.exceptionstyle.ExceptionStyleExamples.nonCompliantMethodCallsCompliantWithMultipleThrowsButThrowsException() throws java.lang.Exception in its method signature instead of a specific exception class");
		assertEquals(expectedReturnType.errorsCaught.get(6),
				"example.exceptionstyle.ExceptionStyleExamples.nonCompliantMethodCatchesException() has at least one catch block that catches java.lang.Exception instead of a specific exception class");
		assertEquals(expectedReturnType.errorsCaught.get(7),
				"example.exceptionstyle.ExceptionStyleExamples.nonCompliantMethodCallsCompliantButCatchesException() has at least one catch block that catches java.lang.Exception instead of a specific exception class");
		assertEquals(expectedReturnType.errorsCaught.get(8),
				"example.exceptionstyle.ExceptionStyleExamples.nonCompliantMethodCallsCompliantWithMultipleThrowsButCatchesException() has at least one catch block that catches java.lang.Exception instead of a specific exception class");
		assertEquals(expectedReturnType.errorsCaught.get(9),
				"example.exceptionstyle.ExceptionStyleExamples.nonCompliantMethodCatchMultipleExceptions() has at least one catch block that catches java.lang.Exception instead of a specific exception class");
		// If a throw and a catch violation is detected in the same file , throw
		// violations come first, then catch violations
		assertEquals(expectedReturnType.errorsCaught.get(10),
				"example.exceptionstyle.ExceptionStyleExamples.nonCompliantMethodThrowsCompliantAndCatchesException() has at least one catch block that catches java.lang.Exception instead of a specific exception class");
		assertEquals(expectedReturnType.errorsCaught.get(11),
				"example.exceptionstyle.ExceptionStyleExamples.nonCompliantMethodThrowsAndCatchesCompliantException() throws java.lang.Exception in its method signature instead of a specific exception class");
		assertEquals(expectedReturnType.errorsCaught.get(12),
				"example.exceptionstyle.ExceptionStyleExamples.nonCompliantMethodThrowsAndCatchesException() throws java.lang.Exception in its method signature instead of a specific exception class");
		assertEquals(expectedReturnType.errorsCaught.get(13),
				"example.exceptionstyle.ExceptionStyleExamples.nonCompliantMethodThrowsAndCatchesException() has at least one catch block that catches java.lang.Exception instead of a specific exception class");
	}

	interface ExceptionLinterTests {
	}

	interface ExceptionLinterCompliantTest {
	}

	interface ExceptionLinterNonCompliantTest {
	}
}
