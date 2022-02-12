
import org.junit.Test;
import static org.junit.Assert.assertTrue;

import domain.GenericTypeNameAnalyzer;
import domain.ReturnType;

public class GenericTypeStyleCheckTest {
	
    @Test
    public void testOneTypeCorrect() {
    	String [] classes = {"example.typename.OneTypeCorrectClass"};
    	ReturnType results = new GenericTypeNameAnalyzer().getFeedback(classes);
    	assertTrue(results.errorsCaught.size() == 0);
    }
    
    @Test
    public void testNoType() {

    }
    
    @Test
    public void testOneTypeCorrectT() {

    }

    @Test
    public void testOneTypeIncorrectLowercase() {

    }
    
    @Test
    public void testOneTypeIncorrectNoT() {

    }
    
    @Test
    public void testMultipleTypesAllCorrect() {
    	
    }
    
    @Test
    public void testMultipleTypesSomeIncorrect() {
    	
    }
    
    @Test
    public void testMultipleClassesAllCorrect() {
    	
    }

    @Test
    public void testMultipleClassesSomeIncorrect() {
    	
    }
}
