
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import domain.ErrType;
import domain.GenericTypeNameAnalyzer;
import domain.LinterError;
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
    	String [] classes = {"example.typename.NoTypeClass"};
    	ReturnType results = new GenericTypeNameAnalyzer().getFeedback(classes);
    	assertTrue(results.errorsCaught.size() == 0);
    }
    
    @Test
    public void testOneTypeCorrectT() {
    	String [] classes = {"example.typename.OneTypeCorrectTClass"};
    	ReturnType results = new GenericTypeNameAnalyzer().getFeedback(classes);
    	assertTrue(results.errorsCaught.size() == 0);
    }

    @Test
    public void testOneTypeIncorrectLowercase() {
    	String [] classes = {"example.typename.OneTypeIncorrectLowercaseClass"};
    	ReturnType results = new GenericTypeNameAnalyzer().getFeedback(classes);
    	assertTrue(results.errorsCaught.size() == 1);
    	assertTrue(results.errorsCaught.get(0).type.equals(ErrType.WARNING));
    	assertEquals(results.errorsCaught.get(0).message, 
    				 "Generic Type: 'incorrect' should be capitalized.");
    }
    
    @Test
    public void testOneTypeIncorrectCapitalNonNumeric() {
    	String [] classes = {"example.typename.OneTypeIncorrectCapitalNonNumericClass"};
    	ReturnType results = new GenericTypeNameAnalyzer().getFeedback(classes);
    	assertTrue(results.errorsCaught.size() == 1);
    	assertTrue(results.errorsCaught.get(0).type.equals(ErrType.WARNING));
    	assertEquals(results.errorsCaught.get(0).message, 
    				 "Generic Type: 'GG' is of length 2 and starts with a capital character - second character should be a single numeric.");
    }
    
    @Test
    public void testOneTypeIncorrectNoT() {
    	String [] classes = {"example.typename.OneTypeIncorrectNoTClass"};
    	ReturnType results = new GenericTypeNameAnalyzer().getFeedback(classes);
    	assertTrue(results.errorsCaught.size() == 1);
    	assertTrue(results.errorsCaught.get(0).type.equals(ErrType.WARNING));
    	assertEquals(results.errorsCaught.get(0).message, 
    				 "Generic Type: 'Incorrect' is of the class name form - should end in a capital 'T'");
    }
    
    @Test
    public void testMultipleTypesAllCorrect() {
    	String [] classes = {"example.typename.MultipleTypesAllCorrectClass"};
    	ReturnType results = new GenericTypeNameAnalyzer().getFeedback(classes);
    	assertTrue(results.errorsCaught.size() == 0);
    }
    
    @Test
    public void testMultipleTypesSomeIncorrect() {
    	String [] classes = {"example.typename.MultipleTypesSomeIncorrectClass"};
    	ReturnType results = new GenericTypeNameAnalyzer().getFeedback(classes);
    	assertTrue(results.errorsCaught.size() == 1);
    	assertEquals(results.errorsCaught.get(0).message, 
    				 "Generic Type: 'incorrectAgain' should be capitalized.");
    }
    
    @Test
    public void testMultipleClassesAllCorrect() {
    	String [] classes = {"example.typename.OneTypeCorrectClass",
    						 "example.typename.MultipleTypesAllCorrectClass"};
    	ReturnType results = new GenericTypeNameAnalyzer().getFeedback(classes);
    	assertTrue(results.errorsCaught.size() == 0);
    }

    @Test
    public void testMultipleClassesSomeIncorrect() {
    	String [] classes = {"example.typename.OneTypeCorrectClass",
    						 "example.typename.OneTypeIncorrectCapitalNonNumericClass",
		 					 "example.typename.MultipleTypesAllCorrectClass",
		 					 "example.typename.OneTypeIncorrectNoTClass",
		 					 "example.typename.MultipleTypesSomeIncorrectClass",
		 					 "example.typename.OneTypeIncorrectLowercaseClass"};
		ReturnType results = new GenericTypeNameAnalyzer().getFeedback(classes);
		assertTrue(results.errorsCaught.size() == 4);
		List<String> errorMessages = new ArrayList<String>();
		for(LinterError linterError : results.errorsCaught) {
			errorMessages.add(linterError.message);
		}
		assertTrue(errorMessages.contains("Generic Type: 'GG' is of length 2 and starts with a capital character - "
										  + "second character should be a single numeric."));
		assertTrue(errorMessages.contains("Generic Type: 'Incorrect' is of the class name form - should end in a capital 'T'"));
		assertTrue(errorMessages.contains("Generic Type: 'incorrect' should be capitalized."));
		assertTrue(errorMessages.contains("Generic Type: 'incorrectAgain' should be capitalized."));
		
    }
}