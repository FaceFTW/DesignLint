
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import domain.ErrType;
import domain.LinterError;
import domain.PrincipleOfLeastKnowledgeAnalyzer;
import domain.ReturnType;

public class PrincipleOfLeastKnowledgeCheckTest {
	
	 @Test
	    public void testNoMethodCalls() {
	    	String [] classes = {"example.typename.OneTypeCorrectClass"};
	    	ReturnType results = new PrincipleOfLeastKnowledgeAnalyzer().getFeedback(classes);
	    	assertTrue(results.errorsCaught.size() == 0);
	    }
}
