import static org.junit.Assert.*;

import org.junit.Test;

import domain.ErrType;
import domain.ReturnType;
import domain.SingletonAnalyzer;

public class SingletonTest {
    private SingletonAnalyzer analyzer;

    @Test
    public void hasPublicConstructorStaticFieldStaticMethodTest() {
        // This test shouldn't detect a singleton
        String[] classList = { "example/singleton/PubConStaticFieldStaticMethod" };
        this.analyzer = new SingletonAnalyzer(classList);
        ReturnType returned = this.analyzer.getFeedback(classList);

        assertEquals("SingletonAnalyzer", returned.analyzerName);
        assertEquals(0, returned.errorsCaught.size());
    }

    @Test
    public void hasPublicConstructorNoFieldStaticMethodTest() {
        // this test shouldn't detect a singleton
        String[] classList = { "example/singleton/PubConNoFieldStaticMethod" };
        this.analyzer = new SingletonAnalyzer(classList);
        ReturnType returned = this.analyzer.getFeedback(classList);

        assertEquals("SingletonAnalyzer", returned.analyzerName);
        assertEquals(0, returned.errorsCaught.size());
    }

    @Test
    public void hasPublicConstructorStaticFieldNoMethodTest() {
        // This test shouldn't detect a singleton
        String[] classList = { "example/singleton/PubConStaticFieldNoMethod" };
        this.analyzer = new SingletonAnalyzer(classList);
        ReturnType returned = this.analyzer.getFeedback(classList);

        assertEquals("SingletonAnalyzer", returned.analyzerName);
        assertEquals(0, returned.errorsCaught.size());
    }

    @Test
    public void hasPublicConstructorNoFieldNoMethodTest() {
        // this test shouldn't detect a singleton
        String[] classList = { "example/singleton/PubConNoFieldNoMethod" };
        this.analyzer = new SingletonAnalyzer(classList);
        ReturnType returned = this.analyzer.getFeedback(classList);

        assertEquals("SingletonAnalyzer", returned.analyzerName);
        assertEquals(0, returned.errorsCaught.size());

    }

    @Test
    public void hasPrivateConstructorNoFieldStaticMethodTest() {
        // This test shouldn't detect a singleton
        String[] classList = { "example/singleton/PriConNoFieldStaticMethod" };
        this.analyzer = new SingletonAnalyzer(classList);
        ReturnType returned = this.analyzer.getFeedback(classList);

        assertEquals("SingletonAnalyzer", returned.analyzerName);
        assertEquals(0, returned.errorsCaught.size());
    }

    @Test
    public void hasPrivateConstructorNoFieldNoMethodTest() {
        // This test shouldn't detect a singleton
        String[] classList = { "example/singleton/PriConNoFieldNoMethod" };
        this.analyzer = new SingletonAnalyzer(classList);
        ReturnType returned = this.analyzer.getFeedback(classList);

        assertEquals("SingletonAnalyzer", returned.analyzerName);
        assertEquals(0, returned.errorsCaught.size());
    }

    @Test
    public void hasPrivateConstructorStaticFieldNoMethodTest() {
        // This test shouldn't detect a singleton
        String[] classList = { "example/singleton/PriConStaticFieldNoMethod" };
        this.analyzer = new SingletonAnalyzer(classList);
        ReturnType returned = this.analyzer.getFeedback(classList);

        assertEquals("SingletonAnalyzer", returned.analyzerName);
        assertEquals(0, returned.errorsCaught.size());
    }

    @Test
    public void hasPrivateConstructorStaticFieldStaticMethodTest() {
        // This test SHOULD detect a singleton
        String[] classList = { "example/singleton/PriConStaticFieldStaticMethod" };
        this.analyzer = new SingletonAnalyzer(classList);
        ReturnType returned = this.analyzer.getFeedback(classList);

        assertEquals("SingletonAnalyzer", returned.analyzerName);
        assertEquals(1, returned.errorsCaught.size());
        assertEquals("PriConStaticFieldStaticMethod", returned.errorsCaught.get(0).className);
        assertEquals("Singleton Pattern detected!", returned.errorsCaught.get(0).message);
        assertEquals(ErrType.PATTERN, returned.errorsCaught.get(0).type);
    }

    @Test
    public void allClassesTest() {
        String[] classList = { "example/singleton/PriConStaticFieldStaticMethod",
                "example/singleton/PriConStaticFieldNoMethod", "example/singleton/PriConNoFieldNoMethod",
                "example/singleton/PriConNoFieldStaticMethod", "example/singleton/PubConNoFieldNoMethod",
                "example/singleton/PubConStaticFieldNoMethod", "example/singleton/PubConNoFieldStaticMethod",
                "example/singleton/PubConStaticFieldStaticMethod" };
        this.analyzer = new SingletonAnalyzer(classList);
        ReturnType returned = this.analyzer.getFeedback(classList);

        assertEquals("SingletonAnalyzer", returned.analyzerName);
        assertEquals(1, returned.errorsCaught.size());
        assertEquals("PriConStaticFieldStaticMethod", returned.errorsCaught.get(0).className);
        assertEquals("Singleton Pattern detected!", returned.errorsCaught.get(0).message);
        assertEquals(ErrType.PATTERN, returned.errorsCaught.get(0).type);
    }

}
