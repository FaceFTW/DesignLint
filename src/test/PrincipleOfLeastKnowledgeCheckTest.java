
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import datasource.ASMParser;
import domain.LinterError;
import domain.AnalyzerReturn;
import domain.analyzer.PrincipleOfLeastKnowledgeAnalyzer;

public class PrincipleOfLeastKnowledgeCheckTest extends AnalyzerFixture<PrincipleOfLeastKnowledgeAnalyzer>{

	private final String[] exampleClasses = {
			"example.demeter.A",
			"example.demeter.B",
			"example.demeter.C",
			"example.demeter.D"
	};

	@Override
	@BeforeEach
	protected void initAnalyzerUUT() {
		this.populateParserData(exampleClasses);
		this.analyzer = new PrincipleOfLeastKnowledgeAnalyzer(this.parser);
		analyzer.getRelevantData(exampleClasses);
	}

	@Test
	public void testNoMethodCalls() {
		String[] classes = { "example.demeter.A" };
		AnalyzerReturn results = this.analyzer.getFeedback(classes);
		for (LinterError error : results.errorsCaught) {
			assertTrue(!error.methodName.equals("doAThing"));
		}
	}

	@Test
	public void testUseMethodOfField() {
		String[] classes = { "example.demeter.A" };
		AnalyzerReturn results = this.analyzer.getFeedback(classes);
		for (LinterError error : results.errorsCaught) {
			assertTrue(!error.methodName.equals("doThingWithB"));
		}
	}

	@Test
	public void testCommunicateThroughFriend() {
		String[] classes = { "example.demeter.A" };
		AnalyzerReturn results = this.analyzer.getFeedback(classes);
		for (LinterError error : results.errorsCaught) {
			assertTrue(!error.methodName.equals("doThingWithCRight"));
		}
	}

	@Test
	public void testCommunicateWithStrangerUsingFriend() {
		String[] classes = { "example.demeter.A" };
		AnalyzerReturn results = this.analyzer.getFeedback(classes);
		for (LinterError error : results.errorsCaught) {
			if (error.methodName.equals("doThingWithCWrong")) {
				assertTrue(true);
				return;
			}
		}
		assertTrue(false);
	}

	@Test
	public void testCommunicateWithStrangerUsingFriendVariable() {
		String[] classes = { "example.demeter.A" };
		AnalyzerReturn results = this.analyzer.getFeedback(classes);
		for (LinterError error : results.errorsCaught) {
			if (error.methodName.equals("doThingWithCWrongVar")) {
				assertTrue(true);
				return;
			}
		}
		assertTrue(false);
	}

	@Test
	public void testCommunicateWithSingleMethodParameter() {
		String[] classes = { "example.demeter.A" };
		AnalyzerReturn results = this.analyzer.getFeedback(classes);
		for (LinterError error : results.errorsCaught) {
			assertTrue(!error.methodName.equals("doThingWithCRight"));
		}
	}

	@Test
	public void testCommunicateWithMultipleMethodParameters() {
		String[] classes = { "example.demeter.A" };
		AnalyzerReturn results = this.analyzer.getFeedback(classes);
		for (LinterError error : results.errorsCaught) {
			assertTrue(!error.methodName.equals("doThingWithCParamMultipleParams"));
		}
	}

	@Test
	public void testCommunicateWithStrangerThroughFriendParameter() {
		String[] classes = { "example.demeter.A" };
		AnalyzerReturn results = this.analyzer.getFeedback(classes);
		for (LinterError error : results.errorsCaught) {
			assertTrue(!error.methodName.equals("doThingWithBParamRight"));
		}
	}

	@Test
	public void testCommunicateWithStrangerUsingFriendParameter() {
		String[] classes = { "example.demeter.A" };
		AnalyzerReturn results = this.analyzer.getFeedback(classes);
		for (LinterError error : results.errorsCaught) {
			if (error.methodName.equals("doThingWithBParamWrong")) {
				assertTrue(true);
				return;
			}
		}
		assertTrue(false);
	}

	@Test
	public void testCommunicateWithNewObject() {
		String[] classes = { "example.demeter.A" };
		AnalyzerReturn results = this.analyzer.getFeedback(classes);
		for (LinterError error : results.errorsCaught) {
			assertTrue(!error.methodName.equals("doThingWithNewC"));
		}
	}

	@Test
	public void testCreateNewButCommunicateWithStranger() {
		String[] classes = { "example.demeter.A" };
		AnalyzerReturn results = this.analyzer.getFeedback(classes);
		for (LinterError error : results.errorsCaught) {
			if (error.methodName.equals("doThingWithNewCWrong")) {
				assertTrue(true);
				return;
			}
		}
		assertTrue(false);
	}

	@Test
	public void testCommunicateWithNewObjectGiveParameter() {
		String[] classes = { "example.demeter.C" };
		AnalyzerReturn results = this.analyzer.getFeedback(classes);
		for (LinterError error : results.errorsCaught) {
			assertTrue(!error.methodName.equals("letADoThingWithC"));
		}
	}

	@Test
	public void testCommunicateWithSuper() {
		String[] classes = { "example.demeter.D" };
		AnalyzerReturn results = this.analyzer.getFeedback(classes);
		for (LinterError error : results.errorsCaught) {
			assertTrue(!error.methodName.equals("doDThing"));
		}
	}

}
