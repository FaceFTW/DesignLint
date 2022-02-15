import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import datasource.ASMParser;
import domain.ErrType;
import domain.ExceptionThrownAnalyzer;
import domain.LinterError;
import domain.ReturnType;
import domain.ExceptionThrownAnalyzer.ExceptionLinterIssue;

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
		List<ExceptionLinterIssue> expected = new ArrayList<>();
		expected.add(ExceptionLinterIssue.NO_VIOLATION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0], "compliantMethodThrowsException"));
	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterCompliantTest.class })
	@Test
	public void testCompliantMethodCallThrowsException() {
		// Expected List of Issues
		List<ExceptionLinterIssue> expected = new ArrayList<>();
		expected.add(ExceptionLinterIssue.NO_VIOLATION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0],
				"compliantMethodCallThrowsException"));
	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterCompliantTest.class })
	@Test
	public void testCompliantMethodThrowsMultipleException() {
		// Expected List of Issues
		List<ExceptionLinterIssue> expected = new ArrayList<>();
		expected.add(ExceptionLinterIssue.NO_VIOLATION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0],
				"compliantMethodThrowsMultipleExceptions"));
	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterCompliantTest.class })
	@Test
	public void testCompliantMethodCatchException() {
		// Expected List of Issues
		List<ExceptionLinterIssue> expected = new ArrayList<>();
		expected.add(ExceptionLinterIssue.NO_VIOLATION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0], "compliantMethodCatchException"));

	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterCompliantTest.class })
	@Test
	public void testcompliantMethodCallCatchException() {
		// Expected List of Issues
		List<ExceptionLinterIssue> expected = new ArrayList<>();
		expected.add(ExceptionLinterIssue.NO_VIOLATION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0],
				"compliantMethodCallCatchException"));
	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterCompliantTest.class })
	@Test
	public void testCompliantMethodCatchMultipleException() {
		// Expected List of Issues
		List<ExceptionLinterIssue> expected = new ArrayList<>();
		expected.add(ExceptionLinterIssue.NO_VIOLATION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0],
				"compliantMethodCatchMultipleException"));
	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterCompliantTest.class })
	@Test
	public void testCompliantMethodCallCatchMultipleException() {
		// Expected List of Issues
		List<ExceptionLinterIssue> expected = new ArrayList<>();
		expected.add(ExceptionLinterIssue.NO_VIOLATION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0],
				"compliantMethodCallCatchMultipleException"));
	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterCompliantTest.class })
	@Test
	public void testCompliantMethodThrowsAndCatchesExceptions() {
		// Expected List of Issues
		List<ExceptionLinterIssue> expected = new ArrayList<>();
		expected.add(ExceptionLinterIssue.NO_VIOLATION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0],
				"compliantMethodThrowsAndCatchesException"));
	}

	// ================== Non-Compliant Methods ===================//
	@Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
	@Test
	public void testNonCompliantMethodThrowsException() {
		// Expected List of Issues
		List<ExceptionLinterIssue> expected = new ArrayList<>();
		expected.add(ExceptionLinterIssue.THROW_EXCEPTION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0],
				"nonCompliantMethodThrowsException"));
	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
	@Test
	public void testNonCompliantMethodThrowsRuntimeException() {
		// Expected List of Issues
		List<ExceptionLinterIssue> expected = new ArrayList<>();
		expected.add(ExceptionLinterIssue.THROW_RUNTIME_EXCEPTION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0],
				"nonCompliantMethodThrowsRuntimeException"));
	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
	@Test
	public void testNonCompliantMethodThrowsError() {
		// Expected List of Issues
		List<ExceptionLinterIssue> expected = new ArrayList<>();
		expected.add(ExceptionLinterIssue.THROW_ERROR);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0], "nonCompliantMethodThrowsError"));
	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
	@Test
	public void testNonCompliantMethodThrowsThrowable() {
		// Expected List of Issues
		List<ExceptionLinterIssue> expected = new ArrayList<>();
		expected.add(ExceptionLinterIssue.THROW_THROWABLE);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0],
				"nonCompliantMethodThrowsThrowable"));
	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
	@Test
	public void testNonCompliantMethodCallsCompliantButThrowsException() {
		// Expected List of Issues
		List<ExceptionLinterIssue> expected = new ArrayList<>();
		expected.add(ExceptionLinterIssue.THROW_EXCEPTION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0],
				"nonCompliantMethodCallsCompliantButThrowsException"));
	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
	@Test
	public void testNonCompliantMethodCallsCompliantWithMultipleThrowsButThrowsException() {
		// Expected List of Issues
		List<ExceptionLinterIssue> expected = new ArrayList<>();
		expected.add(ExceptionLinterIssue.THROW_EXCEPTION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0],
				"nonCompliantMethodCallsCompliantWithMultipleThrowsButThrowsException"));
	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
	@Test
	public void testNonCompliantMethodCatchesException() {
		// Expected List of Issues
		List<ExceptionLinterIssue> expected = new ArrayList<>();
		expected.add(ExceptionLinterIssue.CATCH_EXCEPTION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0],
				"nonCompliantMethodCatchesException"));
	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
	@Test
	public void testNonCompliantMethodCallsCompliantButCatchesException() {
		// Expected List of Issues
		List<ExceptionLinterIssue> expected = new ArrayList<>();
		expected.add(ExceptionLinterIssue.CATCH_EXCEPTION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0],
				"nonCompliantMethodCallsCompliantButCatchesException"));
	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
	@Test
	public void testNonCompliantMethodCallsCompliantWithMultipleThrowsButCatchesException() {
		// Expected List of Issues
		List<ExceptionLinterIssue> expected = new ArrayList<>();
		expected.add(ExceptionLinterIssue.CATCH_EXCEPTION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0],
				"nonCompliantMethodCallsCompliantWithMultipleThrowsButCatchesException"));
	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
	@Test
	public void testNonCompliantMethodCatchMultipleExceptions() {
		// Expected List of Issues
		List<ExceptionLinterIssue> expected = new ArrayList<>();
		expected.add(ExceptionLinterIssue.CATCH_EXCEPTION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0],
				"nonCompliantMethodCatchMultipleExceptions"));

	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
	@Test
	public void testNonCompliantMethodThrowsAndCatchesCompliantException() {
		// Expected List of Issues
		List<ExceptionLinterIssue> expected = new ArrayList<>();
		expected.add(ExceptionLinterIssue.THROW_EXCEPTION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0],
				"nonCompliantMethodThrowsAndCatchesCompliantException"));
	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
	@Test
	public void testNonCompliantMethodThrowsCompliantAndCatchesException() {
		// Expected List of Issues
		List<ExceptionLinterIssue> expected = new ArrayList<>();
		expected.add(ExceptionLinterIssue.CATCH_EXCEPTION);

		setupAnalyzer();
		assertEquals(expected, analyzer.checkMethodCompliance(exampleClasses[0],
				"nonCompliantMethodThrowsCompliantAndCatchesException"));
	}

	@Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
	@Test
	public void testNonCompliantMethodThrowsAndCatchesException() {
		// Expected List of Issues
		List<ExceptionLinterIssue> expected = new ArrayList<>();
		expected.add(ExceptionLinterIssue.THROW_EXCEPTION);
		expected.add(ExceptionLinterIssue.CATCH_EXCEPTION);

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
		List<String> linterErrorStrings = new ArrayList<>();

		for (LinterError err : expectedReturnType.errorsCaught) {
			linterErrorStrings.add(err.toString());
		}

		// All of the LinterErrors we expect to get returned
		LinterError error0Expected = new LinterError("example.exceptionstyle.ExceptionStyleExamples",
				"nonCompliantMethodThrowsException",
				"example.exceptionstyle.ExceptionStyleExamples.nonCompliantMethodThrowsException() throws java.lang.Exception in its method signature instead of a specific exception class",
				ErrType.WARNING);

		LinterError error1Expected = new LinterError("example.exceptionstyle.ExceptionStyleExamples",
				"nonCompliantMethodThrowsRuntimeException",
				"example.exceptionstyle.ExceptionStyleExamples.nonCompliantMethodThrowsRuntimeException() throws java.lang.RuntimeException in its method signature instead of a specific exception class",
				ErrType.WARNING);

		LinterError error2Expected = new LinterError("example.exceptionstyle.ExceptionStyleExamples",
				"nonCompliantMethodThrowsError",
				"example.exceptionstyle.ExceptionStyleExamples.nonCompliantMethodThrowsError() throws java.lang.Error in its method signature instead of a specific exception class",
				ErrType.WARNING);

		LinterError error3Expected = new LinterError("example.exceptionstyle.ExceptionStyleExamples",
				"nonCompliantMethodThrowsThrowable",
				"example.exceptionstyle.ExceptionStyleExamples.nonCompliantMethodThrowsThrowable() throws java.lang.Throwable in its method signature instead of a specific exception class",
				ErrType.WARNING);

		LinterError error4Expected = new LinterError("example.exceptionstyle.ExceptionStyleExamples",
				"nonCompliantMethodCallsCompliantButThrowsException",
				"example.exceptionstyle.ExceptionStyleExamples.nonCompliantMethodCallsCompliantButThrowsException() throws java.lang.Exception in its method signature instead of a specific exception class",
				ErrType.WARNING);

		LinterError error5Expected = new LinterError("example.exceptionstyle.ExceptionStyleExamples",
				"nonCompliantMethodCallsCompliantWithMultipleThrowsButThrowsException",
				"example.exceptionstyle.ExceptionStyleExamples.nonCompliantMethodCallsCompliantWithMultipleThrowsButThrowsException() throws java.lang.Exception in its method signature instead of a specific exception class",
				ErrType.WARNING);

		LinterError error6Expected = new LinterError("example.exceptionstyle.ExceptionStyleExamples",
				"nonCompliantMethodCatchesException",
				"example.exceptionstyle.ExceptionStyleExamples.nonCompliantMethodCatchesException() has at least one catch block that catches java.lang.Exception instead of a specific exception class",
				ErrType.WARNING);

		LinterError error7Expected = new LinterError("example.exceptionstyle.ExceptionStyleExamples",
				"nonCompliantMethodCallsCompliantButCatchesException",
				"example.exceptionstyle.ExceptionStyleExamples.nonCompliantMethodCallsCompliantButCatchesException() has at least one catch block that catches java.lang.Exception instead of a specific exception class",
				ErrType.WARNING);

		LinterError error8Expected = new LinterError("example.exceptionstyle.ExceptionStyleExamples",
				"nonCompliantMethodCallsCompliantWithMultipleThrowsButCatchesException",
				"example.exceptionstyle.ExceptionStyleExamples.nonCompliantMethodCallsCompliantWithMultipleThrowsButCatchesException() has at least one catch block that catches java.lang.Exception instead of a specific exception class",
				ErrType.WARNING);

		LinterError error9Expected = new LinterError("example.exceptionstyle.ExceptionStyleExamples",
				"nonCompliantMethodCatchMultipleExceptions",
				"example.exceptionstyle.ExceptionStyleExamples.nonCompliantMethodCatchMultipleExceptions() has at least one catch block that catches java.lang.Exception instead of a specific exception class",
				ErrType.WARNING);
		LinterError error10Expected = new LinterError("example.exceptionstyle.ExceptionStyleExamples",
				"nonCompliantMethodThrowsCompliantAndCatchesException",
				"example.exceptionstyle.ExceptionStyleExamples.nonCompliantMethodThrowsCompliantAndCatchesException() has at least one catch block that catches java.lang.Exception instead of a specific exception class",
				ErrType.WARNING);
		LinterError error11Expected = new LinterError("example.exceptionstyle.ExceptionStyleExamples",
				"nonCompliantMethodThrowsAndCatchesCompliantException",
				"example.exceptionstyle.ExceptionStyleExamples.nonCompliantMethodThrowsAndCatchesCompliantException() throws java.lang.Exception in its method signature instead of a specific exception class",
				ErrType.WARNING);
		LinterError error12Expected = new LinterError("example.exceptionstyle.ExceptionStyleExamples",
				"nonCompliantMethodThrowsAndCatchesException",
				"example.exceptionstyle.ExceptionStyleExamples.nonCompliantMethodThrowsAndCatchesException() throws java.lang.Exception in its method signature instead of a specific exception class",
				ErrType.WARNING);
		LinterError error13Expected = new LinterError("example.exceptionstyle.ExceptionStyleExamples",
				"nonCompliantMethodThrowsAndCatchesException",
				"example.exceptionstyle.ExceptionStyleExamples.nonCompliantMethodThrowsAndCatchesException() has at least one catch block that catches java.lang.Exception instead of a specific exception class",
				ErrType.WARNING);

		assertEquals(expectedReturnType.analyzerName, "Generic Exception Linter");
		assertEquals(expectedReturnType.errorsCaught.size(), 14);
		assertTrue(linterErrorStrings.contains(error0Expected.toString()));
		assertTrue(linterErrorStrings.contains(error1Expected.toString()));
		assertTrue(linterErrorStrings.contains(error2Expected.toString()));
		assertTrue(linterErrorStrings.contains(error3Expected.toString()));
		assertTrue(linterErrorStrings.contains(error4Expected.toString()));
		assertTrue(linterErrorStrings.contains(error5Expected.toString()));
		assertTrue(linterErrorStrings.contains(error6Expected.toString()));
		assertTrue(linterErrorStrings.contains(error7Expected.toString()));
		assertTrue(linterErrorStrings.contains(error8Expected.toString()));
		assertTrue(linterErrorStrings.contains(error9Expected.toString()));
		// If a throw and a catch violation is detected in the same file , throw
		// violations come first, then catch violations
		assertTrue(linterErrorStrings.contains(error10Expected.toString()));
		assertTrue(linterErrorStrings.contains(error11Expected.toString()));
		assertTrue(linterErrorStrings.contains(error12Expected.toString()));
		assertTrue(linterErrorStrings.contains(error13Expected.toString()));
	}

	interface ExceptionLinterTests {
	}

	interface ExceptionLinterCompliantTest {
	}

	interface ExceptionLinterNonCompliantTest {
	}
}
