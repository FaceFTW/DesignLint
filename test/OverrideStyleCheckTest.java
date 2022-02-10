
import org.junit.Test;
import static org.junit.Assert.assertTrue;

import domain.OverrideAnnotationAnalyzer;
import domain.ReturnType;

public class OverrideStyleCheckTest {
	
    @Test
    public void testOverrideUsed() {
    	String [] classes = {"example.overridestyle.ConcreteSuperclassExample",
    						 "example.overridestyle.SubclassOverrideOne"};
    	ReturnType results = new OverrideAnnotationAnalyzer().getFeedback(classes);
    	assertTrue(results.errorsCaught.size() == 0);
    }

    @Test
    public void testOverrideNotUsed() {
    	String [] classes = {"example.overridestyle.ConcreteSuperclassExample",
		 					 "example.overridestyle.SubclassOverrideNotUsed"};
		ReturnType results = new OverrideAnnotationAnalyzer().getFeedback(classes);
		assertTrue(results.errorsCaught.size() == 1);
    }

    @Test
    public void testNoMethodsOverridden() {

    }

    @Test
    public void testMultipleMethodsOverriddenWithOverride() {

    }

    @Test
    public void testMultipleMethodsOverriddenWithoutOverride() {

    }

    @Test
    public void testMultipleMethodsOverriddenWithAndWithoutOverride() {
    	
    }
    
    @Test
    public void testOverrideUsedAbstractMethod() {

    }

    @Test
    public void testOverrideNotUsedAbstractMethod() {

    }

    @Test
    public void testNoMethodsOverriddenAbstractMethod() {

    }
}
