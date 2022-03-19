import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import domain.LinterError;
import domain.ReturnType;
import domain.analyzer.EqualsAndHashcodeAnalyzer;

public class EqualsAndHashcodeStyleCheckTest {
   
    private EqualsAndHashcodeAnalyzer analyzer;

    @Test
    public void testNeitherMethodOverridden(){
        String[] classList = {"example/equalhashstyle/WithNeitherTestClass"};
        this.analyzer = new EqualsAndHashcodeAnalyzer(classList);
        ReturnType returnVal = this.analyzer.getFeedback(classList);
        List<LinterError> errors = returnVal.errorsCaught;
        assertTrue(errors.size() == 0);
        assertTrue(returnVal.analyzerName == "Equals And Hashcode Override Check");
    }
    @Test 
    public void testEqualsOnlyMethodOverridden(){
        String[] classList = {"example/equalhashstyle/WithEqualsOnlyTestClass"};
        this.analyzer = new EqualsAndHashcodeAnalyzer(classList);
        ReturnType returnVal = this.analyzer.getFeedback(classList);
        List<LinterError> errors = returnVal.errorsCaught;
        assertTrue(errors.size() == 1);
        assertEquals("When overriding the equals method, you should also override the hashCode method ", errors.get(0).message);
        assertTrue(returnVal.analyzerName == "Equals And Hashcode Override Check");

    }
    @Test
    public void testHashCodeOnlyMethodOverridden(){
        String[] classList = {"example/equalhashstyle/WithHashCodeOnlyTestClass"};
        this.analyzer = new EqualsAndHashcodeAnalyzer(classList);
        ReturnType returnVal = this.analyzer.getFeedback(classList);
        List<LinterError> errors = returnVal.errorsCaught;
        assertTrue(errors.size() == 1);
        assertEquals("When overriding the hashCode method, you should also override the equals method ", errors.get(0).message);
        assertTrue(returnVal.analyzerName == "Equals And Hashcode Override Check");

    }
    @Test
    public void testBothMethodsOverridden(){
        String[] classList = {"example/equalhashstyle/WithBothTestClass"};
        this.analyzer = new EqualsAndHashcodeAnalyzer(classList);
        ReturnType returnVal = this.analyzer.getFeedback(classList);
        List<LinterError> errors = returnVal.errorsCaught;
        assertTrue(errors.size() == 0);
        assertTrue(returnVal.analyzerName == "Equals And Hashcode Override Check");
    }

}
