
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
		String [] classes = {"example.demeter.A"};
		ReturnType results = new PrincipleOfLeastKnowledgeAnalyzer().getFeedback(classes);
		for(LinterError error : results.errorsCaught) {
			assertTrue(!error.methodName.equals("doAThing"));
		}
	}
	
	@Test
	public void testUseMethodOfField() {
		String [] classes = {"example.demeter.A"};
		ReturnType results = new PrincipleOfLeastKnowledgeAnalyzer().getFeedback(classes);
		for(LinterError error : results.errorsCaught) {
			assertTrue(!error.methodName.equals("doThingWithB"));
		}
	}
	
	@Test
	public void testCommunicateThroughFriend() {
		String [] classes = {"example.demeter.A"};
		ReturnType results = new PrincipleOfLeastKnowledgeAnalyzer().getFeedback(classes);
		for(LinterError error : results.errorsCaught) {
			assertTrue(!error.methodName.equals("doThingWithCRight"));
		}
	}
	
	@Test
	public void testCommunicateWithStrangerUsingFriend() {
		String [] classes = {"example.demeter.A"};
		ReturnType results = new PrincipleOfLeastKnowledgeAnalyzer().getFeedback(classes);
		for(LinterError error : results.errorsCaught) {
			if(error.methodName.equals("doThingWithCWrong")) {
				assertTrue(true);
				return;
			}
		}
		assertTrue(false);
	}
	
	@Test
	public void testCommunicateWithStrangerUsingFriendVariable() {
		String [] classes = {"example.demeter.A"};
		ReturnType results = new PrincipleOfLeastKnowledgeAnalyzer().getFeedback(classes);
		for(LinterError error : results.errorsCaught) {
			if(error.methodName.equals("doThingWithCWrongVar")) {
				assertTrue(true);
				return;
			}
		}
		assertTrue(false);
	}
	
	@Test
	public void testCommunicateWithSingleMethodParameter() {
		String [] classes = {"example.demeter.A"};
		ReturnType results = new PrincipleOfLeastKnowledgeAnalyzer().getFeedback(classes);
		for(LinterError error : results.errorsCaught) {
			assertTrue(!error.methodName.equals("doThingWithCRight"));
		}
	}
	
	@Test
	public void testCommunicateWithMultipleMethodParameters() {
		String [] classes = {"example.demeter.A"};
		ReturnType results = new PrincipleOfLeastKnowledgeAnalyzer().getFeedback(classes);
		for(LinterError error : results.errorsCaught) {
			assertTrue(!error.methodName.equals("doThingWithCParamMultipleParams"));
		}
	}
	
	@Test
	public void testCommunicateWithStrangerThroughFriendParameter() {
		String [] classes = {"example.demeter.A"};
		ReturnType results = new PrincipleOfLeastKnowledgeAnalyzer().getFeedback(classes);
		for(LinterError error : results.errorsCaught) {
			assertTrue(!error.methodName.equals("doThingWithBParamRight"));
		}
	}
	
	@Test
	public void testCommunicateWithStrangerUsingFriendParameter() {
		String [] classes = {"example.demeter.A"};
		ReturnType results = new PrincipleOfLeastKnowledgeAnalyzer().getFeedback(classes);
		for(LinterError error : results.errorsCaught) {
			if(error.methodName.equals("doThingWithBParamWrong")) {
				assertTrue(true);
				return;
			}
		}
		assertTrue(false);
	}
	
	@Test
	public void testCommunicateWithNewObject() {
		String [] classes = {"example.demeter.A"};
		ReturnType results = new PrincipleOfLeastKnowledgeAnalyzer().getFeedback(classes);
		for(LinterError error : results.errorsCaught) {
			assertTrue(!error.methodName.equals("doThingWithNewC"));
		}
	}
	
	@Test
	public void testCreateNewButCommunicateWithStranger() {
		String [] classes = {"example.demeter.A"};
		ReturnType results = new PrincipleOfLeastKnowledgeAnalyzer().getFeedback(classes);
		for(LinterError error : results.errorsCaught) {
			if(error.methodName.equals("doThingWithNewCWrong")) {
				assertTrue(true);
				return;
			}
		}
		assertTrue(false);
	}
	
	
}
