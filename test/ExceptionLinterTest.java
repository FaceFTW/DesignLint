import java.io.IOException;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import datasource.ASMParser;
import domain.ExceptionThrownAnalyzer;

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
        
        //TODO Call ASMParser Method to do all parsing


        this.analyzer = new ExceptionThrownAnalyzer(parser);
        analyzer.analyzeData();
    }

    // Tests of Compliant Methods
    @Category({ ExceptionLinterTests.class, ExceptionLinterCompliantTest.class })
    @Test
    public void testCompliantMethodThrowsException() {
        setupAnalyzer();

    }

    @Category({ ExceptionLinterTests.class, ExceptionLinterCompliantTest.class })
    @Test
    public void testCompliantMethodCallThrowsException() {
        setupAnalyzer();
    }

    @Category({ ExceptionLinterTests.class, ExceptionLinterCompliantTest.class })
    @Test
    public void testCompliantMethodThrowsMultipleException() {
        setupAnalyzer();


    }

    @Category({ ExceptionLinterTests.class, ExceptionLinterCompliantTest.class })
    @Test
    public void testCompliantMethodCatchException() {
        setupAnalyzer();
    }

    @Category({ ExceptionLinterTests.class, ExceptionLinterCompliantTest.class })
    @Test
    public void testcompliantMethodCallCatchException() {
        setupAnalyzer();
    }

    @Category({ ExceptionLinterTests.class, ExceptionLinterCompliantTest.class })
    @Test
    public void testCompliantMethodCatchMultipleException() {
        setupAnalyzer();
    }

    @Category({ ExceptionLinterTests.class, ExceptionLinterCompliantTest.class })
    @Test
    public void testCompliantMethodCallCatchMultipleException() {
        setupAnalyzer();
    }

    // Non-Compliant Methods
    @Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
    @Test
    public void testNonCompliantMethodThrowsException() {
    }

    @Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
    @Test
    public void testNonCompliantMethodThrowsRuntimeException() {
    }

    @Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
    @Test
    public void testNonCompliantMethodThrowsError() {
    }

    @Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
    @Test
    public void testNonCompliantMethodThrowsThrowable() {
    }

    @Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
    @Test
    public void testNonCompliantMethodCallsCompliantButThrowsException() {
    }

    @Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
    @Test
    public void testNonCompliantMethodCallsCompliantWithMultipleThrowsButThrowsException() {
    }

    @Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
    @Test
    public void testNonCompliantMethodCatchesException() {
    }

    @Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
    @Test
    public void testNonCompliantMethodCallsCompliantButCatchesException() {
    }

    @Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
    @Test
    public void testNonCompliantMethodCallsCompliantWithMultipleThrowsButCatchesException() {
    }

    @Category({ ExceptionLinterTests.class, ExceptionLinterNonCompliantTest.class })
    @Test
    public void testNonCompliantMethodCatchMultipleExceptions() {
    }

    interface ExceptionLinterTests {
    }

    interface ExceptionLinterCompliantTest {
    }

    interface ExceptionLinterNonCompliantTest {
    }
}
