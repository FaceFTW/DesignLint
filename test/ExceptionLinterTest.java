import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import datasource.ASMParser;
import domain.ExceptionThrownAnalyzer;
import domain.ReturnType;

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
        setupAnalyzer();
        assertTrue(analyzer.checkMethodThrowsCompliance(exampleClasses[0], "compliantMethodThrowsException"));
    }

    @Category({ ExceptionLinterTests.class, ExceptionLinterCompliantTest.class })
    @Test
    public void testCompliantMethodCallThrowsException() {
        setupAnalyzer();
        assertTrue(analyzer.checkMethodThrowsCompliance(exampleClasses[0], "compliantMethodCallThrowsException"));
        assertTrue(analyzer.checkMethodCatchCompliance(exampleClasses[0], "compliantMethodCallThrowsException"));

    }

    @Category({ ExceptionLinterTests.class, ExceptionLinterCompliantTest.class })
    @Test
    public void testCompliantMethodThrowsMultipleException() {
        setupAnalyzer();
        assertTrue(analyzer.checkMethodThrowsCompliance(exampleClasses[0], "compliantMethodThrowsMultipleExceptions"));
        assertTrue(analyzer.checkMethodCatchCompliance(exampleClasses[0], "compliantMethodThrowsMultipleExceptions"));
    }

    @Category({ ExceptionLinterTests.class, ExceptionLinterCompliantTest.class })
    @Test
    public void testCompliantMethodCatchException() {
        setupAnalyzer();
        assertTrue(analyzer.checkMethodThrowsCompliance(exampleClasses[0], "compliantMethodCatchException"));
        assertTrue(analyzer.checkMethodCatchCompliance(exampleClasses[0], "compliantMethodThrowsMultipleExceptions"));
    }

    @Category({ ExceptionLinterTests.class, ExceptionLinterCompliantTest.class })
    @Test
    public void testcompliantMethodCallCatchException() {
        setupAnalyzer();
        assertTrue(analyzer.checkMethodThrowsCompliance(exampleClasses[0], "compliantMethodCallCatchException"));
        assertTrue(analyzer.checkMethodCatchCompliance(exampleClasses[0], "compliantMethodThrowsMultipleExceptions"));

    }

    @Category({ ExceptionLinterTests.class, ExceptionLinterCompliantTest.class })
    @Test
    public void testCompliantMethodCatchMultipleException() {
        setupAnalyzer();
        assertTrue(analyzer.checkMethodThrowsCompliance(exampleClasses[0], "compliantMethodCatchMultipleException"));
        assertTrue(analyzer.checkMethodCatchCompliance(exampleClasses[0], "compliantMethodThrowsMultipleExceptions"));

    }

    @Category({ ExceptionLinterTests.class, ExceptionLinterCompliantTest.class })
    @Test
    public void testCompliantMethodCallCatchMultipleException() {
        setupAnalyzer();
        assertTrue(
                analyzer.checkMethodThrowsCompliance(exampleClasses[0], "compliantMethodCallCatchMultipleException"));
        assertTrue(
                analyzer.checkMethodCatchCompliance(exampleClasses[0], "compliantMethodCallCatchMultipleException"));
    }

    @Category({ ExceptionLinterTests.class, ExceptionLinterCompliantTest.class })
    @Test
    public void testCompliantMethodThrowsAndCatchesExceptions() {
        setupAnalyzer();
        assertTrue(analyzer.checkMethodThrowsCompliance(exampleClasses[0], "compliantMethodThrowsAndCatchesException"));
        assertTrue(analyzer.checkMethodCatchCompliance(exampleClasses[0], "compliantMethodThrowsAndCatchesException"));

    }

    // ================== Non-Compliant Methods ===================//
    @Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
    @Test
    public void testNonCompliantMethodThrowsException() {
        setupAnalyzer();
        assertTrue(!analyzer.checkMethodThrowsCompliance(exampleClasses[0], "nonCompliantMethodThrowsException"));
        assertTrue(analyzer.checkMethodThrowsCompliance(exampleClasses[0], "nonCompliantMethodThrowsException"));
    }

    @Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
    @Test
    public void testNonCompliantMethodThrowsRuntimeException() {
        setupAnalyzer();
        assertTrue(
                !analyzer.checkMethodThrowsCompliance(exampleClasses[0], "nonCompliantMethodThrowsRuntimeException"));
        assertTrue(analyzer.checkMethodCatchCompliance(exampleClasses[0], "nonCompliantMethodThrowsRuntimeException"));
    }

    @Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
    @Test
    public void testNonCompliantMethodThrowsError() {
        setupAnalyzer();
        assertTrue(!analyzer.checkMethodThrowsCompliance(exampleClasses[0], "nonCompliantMethodThrowsError"));
        assertTrue(analyzer.checkMethodCatchCompliance(exampleClasses[0], "nonCompliantMethodThrowsError"));

    }

    @Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
    @Test
    public void testNonCompliantMethodThrowsThrowable() {
        setupAnalyzer();
        assertTrue(!analyzer.checkMethodThrowsCompliance(exampleClasses[0], "nonCompliantMethodThrowsThrowable"));
        assertTrue(analyzer.checkMethodCatchCompliance(exampleClasses[0], "nonCompliantMethodThrowsError"));

    }

    @Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
    @Test
    public void testNonCompliantMethodCallsCompliantButThrowsException() {
        setupAnalyzer();
        assertTrue(!analyzer.checkMethodThrowsCompliance(exampleClasses[0],
                "nonCompliantMethodCallsCompliantButThrowsException"));
        assertTrue(analyzer.checkMethodCatchCompliance(exampleClasses[0],
                "nonCompliantMethodCallsCompliantButThrowsException"));
    }

    @Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
    @Test
    public void testNonCompliantMethodCallsCompliantWithMultipleThrowsButThrowsException() {
        setupAnalyzer();
        assertTrue(!analyzer.checkMethodThrowsCompliance(exampleClasses[0],
                "nonCompliantMethodCallsCompliantWithMultipleThrowsButThrowsException"));
        assertTrue(analyzer.checkMethodCatchCompliance(exampleClasses[0],
                "nonCompliantMethodCallsCompliantWithMultipleThrowsButThrowsException"));
    }

    @Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
    @Test
    public void testNonCompliantMethodCatchesException() {
        setupAnalyzer();
        assertTrue(analyzer.checkMethodThrowsCompliance(exampleClasses[0], "nonCompliantMethodCatchesException"));
        assertTrue(!analyzer.checkMethodCatchCompliance(exampleClasses[0], "nonCompliantMethodCatchesException"));

    }

    @Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
    @Test
    public void testNonCompliantMethodCallsCompliantButCatchesException() {
        setupAnalyzer();
        assertTrue(analyzer.checkMethodThrowsCompliance(exampleClasses[0],
                "nonCompliantMethodCallsCompliantButCatchesException"));
        assertTrue(!analyzer.checkMethodCatchCompliance(exampleClasses[0],
                "nonCompliantMethodCallsCompliantButCatchesException"));
    }

    @Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
    @Test
    public void testNonCompliantMethodCallsCompliantWithMultipleThrowsButCatchesException() {
        setupAnalyzer();
        assertTrue(analyzer.checkMethodThrowsCompliance(exampleClasses[0],
                "nonCompliantMethodCallsCompliantWithMultipleThrowsButCatchesException"));
        assertTrue(!analyzer.checkMethodCatchCompliance(exampleClasses[0],
                "nonCompliantMethodCallsCompliantWithMultipleThrowsButCatchesException"));
    }

    @Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
    @Test
    public void testNonCompliantMethodCatchMultipleExceptions() {
        setupAnalyzer();
        assertTrue(
                analyzer.checkMethodThrowsCompliance(exampleClasses[0], "nonCompliantMethodCatchMultipleExceptions"));
        assertTrue(
                !analyzer.checkMethodCatchCompliance(exampleClasses[0], "nonCompliantMethodCatchMultipleExceptions"));
    }

    @Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
    @Test
    public void testNonCompliantMethodThrowsAndCatchesCompliantException() {
        setupAnalyzer();
        assertTrue(!analyzer.checkMethodThrowsCompliance(exampleClasses[0],
                "nonCompliantMethodThrowsAndCatchesCompliantException"));
        assertTrue(analyzer.checkMethodCatchCompliance(exampleClasses[0],
                "nonCompliantMethodThrowsAndCatchesCompliantException"));
    }

    @Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
    @Test
    public void testNonCompliantMethodThrowsCompliantAndCatchesException() {
        setupAnalyzer();
        assertTrue(analyzer.checkMethodThrowsCompliance(exampleClasses[0],
                "nonCompliantMethodThrowsCompliantAndCatchesException"));
        assertTrue(!analyzer.checkMethodCatchCompliance(exampleClasses[0],
                "nonCompliantMethodThrowsCompliantAndCatchesException"));
    }

    @Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
    @Test
    public void testNonCompliantMethodThrowsAndCatchesException() {
        setupAnalyzer();
        assertTrue(!analyzer.checkMethodThrowsCompliance(exampleClasses[0],
                "nonCompliantMethodThrowsAndCatchesException"));
        assertTrue(!analyzer.checkMethodCatchCompliance(exampleClasses[0],
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
