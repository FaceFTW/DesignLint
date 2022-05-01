
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import datasource.ASMParser;
import domain.ErrType;
import domain.LinterError;
import domain.AnalyzerReturn;
import domain.analyzer.ObjectAdapterIdentifierAnalyzer;

public class ObjectAdapterIdentifierTest extends AnalyzerFixture<ObjectAdapterIdentifierAnalyzer>{

	private final String[] exampleClasses = {
			"example.objectadapter.TargetInterface",
			"example.objectadapter.TargetAbstractClass",
			"example.objectadapter.Adaptee",
			"example.objectadapter.AdapterAbstractClassCorrect",
			"example.objectadapter.AdapterAbstractClassIncorrect",
			"example.objectadapter.AdapterBothCorrect",
			"example.objectadapter.AdapterBothIncorrect",
			"example.objectadapter.AdapterInterfaceCorrect",
			"example.objectadapter.AdapterInterfaceCorrectAbstractClassIncorrect",
			"example.objectadapter.AdapterInterfaceIncorrect",
			"example.objectadapter.AdapterInterfaceIncorrectAbstractClassCorrect",
			"example.objectadapter.InterfaceAdaptee",
			"example.objectadapter.AbstractAdaptee",
			"example.objectadapter.AdapterAdaptsInterface",
			"example.objectadapter.AdapterAdaptsAbstractClass",
			"example.objectadapter.TargetAbstractClassNoAbstractMethods",
			"example.objectadapter.AdapterFalseAbstractClass",
			"example.objectadapter.AdapterNoTargetAbstractClass",
			"example.objectadapter.AdapterNoTargetInterface"
	};

	@Override
	@BeforeEach
	protected void initAnalyzerUUT() {
		this.populateParserData(exampleClasses);
		this.analyzer = new ObjectAdapterIdentifierAnalyzer(this.parser);
	}

	@Test
	public void testInterfaceTargetAdapterAdaptee() {
		String[] classes = { "example.objectadapter.TargetInterface",
				"example.objectadapter.Adaptee",
				"example.objectadapter.AdapterInterfaceCorrect" };
		AnalyzerReturn results = this.analyzer.getFeedback(classes);
		List<String> patternCatches = new ArrayList<String>();
		assertTrue(results.errorsCaught.size() == 1);
		for (LinterError linterError : results.errorsCaught) {
			patternCatches.add(linterError.message);
			assertTrue(linterError.type == ErrType.PATTERN);
		}
		String expectedResult = "Object Adapter Pattern Recognized:\n";
		expectedResult += "Target: example.objectadapter.TargetInterface\n";
		expectedResult += "Adaptee: example.objectadapter.Adaptee\n";
		expectedResult += "Adapter: example.objectadapter.AdapterInterfaceCorrect\n";
		assertTrue(patternCatches.contains(expectedResult));
	}

	@Test
	public void testAbstractClassTargetAdapterAdaptee() {
		String[] classes = { "example.objectadapter.TargetAbstractClass",
				"example.objectadapter.Adaptee",
				"example.objectadapter.AdapterAbstractClassCorrect" };
		AnalyzerReturn results = this.analyzer.getFeedback(classes);
		List<String> patternCatches = new ArrayList<String>();
		assertTrue(results.errorsCaught.size() == 1);
		for (LinterError linterError : results.errorsCaught) {
			patternCatches.add(linterError.message);
			assertTrue(linterError.type == ErrType.PATTERN);
		}
		String expectedResult = "Object Adapter Pattern Recognized:\n";
		expectedResult += "Target: example.objectadapter.TargetAbstractClass\n";
		expectedResult += "Adaptee: example.objectadapter.Adaptee\n";
		expectedResult += "Adapter: example.objectadapter.AdapterAbstractClassCorrect\n";
		assertTrue(patternCatches.contains(expectedResult));
	}

	@Test
	public void testMultipleTargetAdapter() {
		String[] classes = { "example.objectadapter.TargetAbstractClass",
				"example.objectadapter.TargetInterface",
				"example.objectadapter.Adaptee",
				"example.objectadapter.AdapterBothCorrect" };
		AnalyzerReturn results = this.analyzer.getFeedback(classes);
		List<String> patternCatches = new ArrayList<String>();
		assertTrue(results.errorsCaught.size() == 2);
		for (LinterError linterError : results.errorsCaught) {
			patternCatches.add(linterError.message);
			assertTrue(linterError.type == ErrType.PATTERN);
		}
		String expectedResult1 = "Object Adapter Pattern Recognized:\n";
		expectedResult1 += "Target: example.objectadapter.TargetAbstractClass\n";
		expectedResult1 += "Adaptee: example.objectadapter.Adaptee\n";
		expectedResult1 += "Adapter: example.objectadapter.AdapterBothCorrect\n";

		String expectedResult2 = "Object Adapter Pattern Recognized:\n";
		expectedResult2 += "Target: example.objectadapter.TargetInterface\n";
		expectedResult2 += "Adaptee: example.objectadapter.Adaptee\n";
		expectedResult2 += "Adapter: example.objectadapter.AdapterBothCorrect\n";

		assertTrue(patternCatches.contains(expectedResult1));
		assertTrue(patternCatches.contains(expectedResult2));
	}

	@Test
	public void testNoTargetInterface() {
		String[] classes = { "example.objectadapter.Adaptee",
				"example.objectadapter.AdapterInterfaceCorrect" };
		AnalyzerReturn results = this.analyzer.getFeedback(classes);
		assertTrue(results.errorsCaught.size() == 0);
	}

	@Test
	public void testNoAdaptee() {
		String[] classes = { "example.objectadapter.TargetAbstractClass",
				"example.objectadapter.AdapterAbstractClassCorrect" };
		AnalyzerReturn results = this.analyzer.getFeedback(classes);
		assertTrue(results.errorsCaught.size() == 0);
	}

	@Test
	public void testNoAdapter() {
		String[] classes = { "example.objectadapter.TargetAbstractClass",
				"example.objectadapter.Adaptee" };
		AnalyzerReturn results = this.analyzer.getFeedback(classes);
		assertTrue(results.errorsCaught.size() == 0);
	}

	@Test
	public void testAdapterAbstractClassIncorrect() {
		String[] classes = { "example.objectadapter.TargetAbstractClass",
				"example.objectadapter.Adaptee",
				"example.objectadapter.AdapterAbstractClassIncorrect" };
		AnalyzerReturn results = this.analyzer.getFeedback(classes);
		assertTrue(results.errorsCaught.size() == 0);
	}

	@Test
	public void testAdapterInterfaceIncorrect() {
		String[] classes = { "example.objectadapter.TargetInterface",
				"example.objectadapter.Adaptee",
				"example.objectadapter.AdapterInterfaceIncorrect" };
		AnalyzerReturn results = this.analyzer.getFeedback(classes);
		assertTrue(results.errorsCaught.size() == 0);
	}

	@Test
	public void testAdapterNoTargetInterface() {
		String[] classes = { "example.objectadapter.TargetInterface",
				"example.objectadapter.Adaptee",
				"example.objectadapter.AdapterNoTargetInterface" };
		AnalyzerReturn results = this.analyzer.getFeedback(classes);
		assertTrue(results.errorsCaught.size() == 0);
	}

	@Test
	public void testAdapterNoTargetAbstractClass() {
		String[] classes = { "example.objectadapter.TargetInterface",
				"example.objectadapter.Adaptee",
				"example.objectadapter.AdapterNoTargetAbstractClass" };
		AnalyzerReturn results = this.analyzer.getFeedback(classes);
		assertTrue(results.errorsCaught.size() == 0);
	}

	@Test
	public void testAdapterBothIncorrect() {
		String[] classes = { "example.objectadapter.TargetAbstractClass",
				"example.objectadapter.TargetInterface",
				"example.objectadapter.Adaptee",
				"example.objectadapter.AdapterBothIncorrect" };
		AnalyzerReturn results = this.analyzer.getFeedback(classes);
		assertTrue(results.errorsCaught.size() == 0);
	}

	@Test
	public void testAdapterInterfaceCorrectAbstractClassIncorrect() {
		String[] classes = { "example.objectadapter.TargetAbstractClass",
				"example.objectadapter.TargetInterface",
				"example.objectadapter.Adaptee",
				"example.objectadapter.AdapterInterfaceCorrectAbstractClassIncorrect" };
		AnalyzerReturn results = this.analyzer.getFeedback(classes);
		List<String> patternCatches = new ArrayList<String>();
		assertTrue(results.errorsCaught.size() == 1);
		for (LinterError linterError : results.errorsCaught) {
			patternCatches.add(linterError.message);
			assertTrue(linterError.type == ErrType.PATTERN);
		}
		String expectedResult = "Object Adapter Pattern Recognized:\n";
		expectedResult += "Target: example.objectadapter.TargetInterface\n";
		expectedResult += "Adaptee: example.objectadapter.Adaptee\n";
		expectedResult += "Adapter: example.objectadapter.AdapterInterfaceCorrectAbstractClassIncorrect\n";
		assertTrue(patternCatches.contains(expectedResult));
	}

	@Test
	public void testAdapterInterfaceIncorrectAbstractClassCorrect() {
		String[] classes = { "example.objectadapter.TargetAbstractClass",
				"example.objectadapter.TargetInterface",
				"example.objectadapter.Adaptee",
				"example.objectadapter.AdapterInterfaceIncorrectAbstractClassCorrect" };
		AnalyzerReturn results = this.analyzer.getFeedback(classes);
		List<String> patternCatches = new ArrayList<String>();
		assertTrue(results.errorsCaught.size() == 1);
		for (LinterError linterError : results.errorsCaught) {
			patternCatches.add(linterError.message);
			assertTrue(linterError.type == ErrType.PATTERN);
		}
		String expectedResult = "Object Adapter Pattern Recognized:\n";
		expectedResult += "Target: example.objectadapter.TargetAbstractClass\n";
		expectedResult += "Adaptee: example.objectadapter.Adaptee\n";
		expectedResult += "Adapter: example.objectadapter.AdapterInterfaceIncorrectAbstractClassCorrect\n";
		assertTrue(patternCatches.contains(expectedResult));
	}

	@Test
	public void testInterfaceAdaptee() {
		String[] classes = { "example.objectadapter.TargetInterface",
				"example.objectadapter.InterfaceAdaptee",
				"example.objectadapter.AdapterAdaptsInterface" };
		AnalyzerReturn results = this.analyzer.getFeedback(classes);
		List<String> patternCatches = new ArrayList<String>();
		assertTrue(results.errorsCaught.size() == 1);
		for (LinterError linterError : results.errorsCaught) {
			patternCatches.add(linterError.message);
			assertTrue(linterError.type == ErrType.PATTERN);
		}
		String expectedResult = "Object Adapter Pattern Recognized:\n";
		expectedResult += "Target: example.objectadapter.TargetInterface\n";
		expectedResult += "Adaptee: example.objectadapter.InterfaceAdaptee\n";
		expectedResult += "Adapter: example.objectadapter.AdapterAdaptsInterface\n";
		assertTrue(patternCatches.contains(expectedResult));
	}

	@Test
	public void testAbstractClassAdaptee() {
		String[] classes = { "example.objectadapter.TargetAbstractClass",
				"example.objectadapter.AbstractAdaptee",
				"example.objectadapter.AdapterAdaptsAbstractClass" };
		AnalyzerReturn results = this.analyzer.getFeedback(classes);
		List<String> patternCatches = new ArrayList<String>();
		assertTrue(results.errorsCaught.size() == 1);
		for (LinterError linterError : results.errorsCaught) {
			patternCatches.add(linterError.message);
			assertTrue(linterError.type == ErrType.PATTERN);
		}
		String expectedResult = "Object Adapter Pattern Recognized:\n";
		expectedResult += "Target: example.objectadapter.TargetAbstractClass\n";
		expectedResult += "Adaptee: example.objectadapter.AbstractAdaptee\n";
		expectedResult += "Adapter: example.objectadapter.AdapterAdaptsAbstractClass\n";
		assertTrue(patternCatches.contains(expectedResult));
	}

	@Test
	public void testTargetAbstractClassNoAbstractMethods() {
		String[] classes = { "example.objectadapter.TargetAbstractClassNoAbstractMethods",
				"example.objectadapter.AbstractAdaptee",
				"example.objectadapter.AdapterFalseAbstractClass" };
		AnalyzerReturn results = this.analyzer.getFeedback(classes);
		assertTrue(results.errorsCaught.size() == 0);
	}

}
