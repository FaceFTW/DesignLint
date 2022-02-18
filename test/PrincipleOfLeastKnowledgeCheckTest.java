
import org.junit.Test;

import datasource.ASMParser;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import domain.LinterError;
import domain.PrincipleOfLeastKnowledgeAnalyzer;
import domain.ReturnType;

public class PrincipleOfLeastKnowledgeCheckTest {
	
	private ASMParser parser;
	private PrincipleOfLeastKnowledgeAnalyzer analyzer;
	
	private final String[] exampleClasses = {
			"example.demeter.A",
			"example.demeter.B",
			"example.demeter.C",
			"example.demeter.D"
	};
	
	public void setupAnalyzer() {
		try {
			this.parser = new ASMParser(exampleClasses);
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.analyzer = new PrincipleOfLeastKnowledgeAnalyzer(this.parser);
		analyzer.getRelevantData(exampleClasses);
	}
	
	@Test
	public void testNoMethodCalls() {
		this.setupAnalyzer();
		String [] classes = {"example.demeter.A"};
		ReturnType results = this.analyzer.getFeedback(classes);
		for(LinterError error : results.errorsCaught) {
			assertTrue(!error.methodName.equals("doAThing"));
		}
	}
	
	@Test
	public void testUseMethodOfField() {
		this.setupAnalyzer();
		String [] classes = {"example.demeter.A"};
		ReturnType results = this.analyzer.getFeedback(classes);
		for(LinterError error : results.errorsCaught) {
			assertTrue(!error.methodName.equals("doThingWithB"));
		}
	}
	
	@Test
	public void testCommunicateThroughFriend() {
		this.setupAnalyzer();
		String [] classes = {"example.demeter.A"};
		ReturnType results = this.analyzer.getFeedback(classes);
		for(LinterError error : results.errorsCaught) {
			assertTrue(!error.methodName.equals("doThingWithCRight"));
		}
	}
	
	@Test
	public void testCommunicateWithStrangerUsingFriend() {
		this.setupAnalyzer();
		String [] classes = {"example.demeter.A"};
		ReturnType results = this.analyzer.getFeedback(classes);
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
		this.setupAnalyzer();
		String [] classes = {"example.demeter.A"};
		ReturnType results = this.analyzer.getFeedback(classes);
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
		this.setupAnalyzer();
		String [] classes = {"example.demeter.A"};
		ReturnType results = this.analyzer.getFeedback(classes);
		for(LinterError error : results.errorsCaught) {
			assertTrue(!error.methodName.equals("doThingWithCRight"));
		}
	}
	
	@Test
	public void testCommunicateWithMultipleMethodParameters() {
		this.setupAnalyzer();
		String [] classes = {"example.demeter.A"};
		ReturnType results = this.analyzer.getFeedback(classes);
		for(LinterError error : results.errorsCaught) {
			assertTrue(!error.methodName.equals("doThingWithCParamMultipleParams"));
		}
	}
	
	@Test
	public void testCommunicateWithStrangerThroughFriendParameter() {
		this.setupAnalyzer();
		String [] classes = {"example.demeter.A"};
		ReturnType results = this.analyzer.getFeedback(classes);
		for(LinterError error : results.errorsCaught) {
			assertTrue(!error.methodName.equals("doThingWithBParamRight"));
		}
	}
	
	@Test
	public void testCommunicateWithStrangerUsingFriendParameter() {
		this.setupAnalyzer();
		String [] classes = {"example.demeter.A"};
		ReturnType results = this.analyzer.getFeedback(classes);
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
		this.setupAnalyzer();
		String [] classes = {"example.demeter.A"};
		ReturnType results = this.analyzer.getFeedback(classes);
		for(LinterError error : results.errorsCaught) {
			assertTrue(!error.methodName.equals("doThingWithNewC"));
		}
	}
	
	@Test
	public void testCreateNewButCommunicateWithStranger() {
		this.setupAnalyzer();
		String [] classes = {"example.demeter.A"};
		ReturnType results = this.analyzer.getFeedback(classes);
		for(LinterError error : results.errorsCaught) {
			if(error.methodName.equals("doThingWithNewCWrong")) {
				assertTrue(true);
				return;
			}
		}
		assertTrue(false);
	}
	
	
}
