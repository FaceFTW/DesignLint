
import org.junit.Test;

import datasource.ASMParser;

import static org.junit.Assert.assertTrue;
import domain.LinterError;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import domain.ErrType;
import domain.ReturnType;
import domain.analyzer.ObjectAdapterIdentifierAnalyzer;

public class ObjectAdapterIdentifierTest {
	
	private ASMParser parser;
	private ObjectAdapterIdentifierAnalyzer analyzer;
	
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
	
	public void setupAnalyzer() {
		try {
			this.parser = new ASMParser(this.exampleClasses);
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.analyzer = new ObjectAdapterIdentifierAnalyzer(this.parser);
		//this.analyzer.getRelevantData(this.exampleClasses);
	}
	
	@Test
	public void testInterfaceTargetAdapterAdaptee() {
		this.setupAnalyzer();
		String [] classes = {"example.objectadapter.TargetInterface",
							 "example.objectadapter.Adaptee",
							 "example.objectadapter.AdapterInterfaceCorrect"};
		ReturnType results = this.analyzer.getFeedback(classes);
		List<String> patternCatches = new ArrayList<String>();
		assertTrue(results.errorsCaught.size() == 1);
		for(LinterError linterError : results.errorsCaught) {
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
		this.setupAnalyzer();
		String [] classes = {"example.objectadapter.TargetAbstractClass",
							 "example.objectadapter.Adaptee",
							 "example.objectadapter.AdapterAbstractClassCorrect"};
		ReturnType results = this.analyzer.getFeedback(classes);
		List<String> patternCatches = new ArrayList<String>();
		assertTrue(results.errorsCaught.size() == 1);
		for(LinterError linterError : results.errorsCaught) {
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
		this.setupAnalyzer();
		String [] classes = {"example.objectadapter.TargetAbstractClass",
							 "example.objectadapter.TargetInterface",
							 "example.objectadapter.Adaptee",
							 "example.objectadapter.AdapterBothCorrect"};
		ReturnType results = this.analyzer.getFeedback(classes);
		List<String> patternCatches = new ArrayList<String>();
		assertTrue(results.errorsCaught.size() == 2);
		for(LinterError linterError : results.errorsCaught) {
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
		this.setupAnalyzer();
		String [] classes = {"example.objectadapter.Adaptee",
							 "example.objectadapter.AdapterInterfaceCorrect"};
		ReturnType results = this.analyzer.getFeedback(classes);
		assertTrue(results.errorsCaught.size() == 0);
	}
	
	@Test
	public void testNoAdaptee() {
		this.setupAnalyzer();
		String [] classes = {"example.objectadapter.TargetAbstractClass",
							 "example.objectadapter.AdapterAbstractClassCorrect"};
		ReturnType results = this.analyzer.getFeedback(classes);
		assertTrue(results.errorsCaught.size() == 0);
	}
	
	@Test
	public void testNoAdapter() {
		this.setupAnalyzer();
		String [] classes = {"example.objectadapter.TargetAbstractClass",
							 "example.objectadapter.Adaptee"};
		ReturnType results = this.analyzer.getFeedback(classes);
		assertTrue(results.errorsCaught.size() == 0);
	}
	
	@Test
	public void testAdapterAbstractClassIncorrect() {
		this.setupAnalyzer();
		String [] classes = {"example.objectadapter.TargetAbstractClass",
							 "example.objectadapter.Adaptee",
							 "example.objectadapter.AdapterAbstractClassIncorrect"};
		ReturnType results = this.analyzer.getFeedback(classes);
		assertTrue(results.errorsCaught.size() == 0);
	}
	
	@Test
	public void testAdapterInterfaceIncorrect() {
		this.setupAnalyzer();
		String [] classes = {"example.objectadapter.TargetInterface",
							 "example.objectadapter.Adaptee",
							 "example.objectadapter.AdapterInterfaceIncorrect"};
		ReturnType results = this.analyzer.getFeedback(classes);
		assertTrue(results.errorsCaught.size() == 0);
	}
	
	@Test
	public void testAdapterNoTargetInterface() {
		this.setupAnalyzer();
		String [] classes = {"example.objectadapter.TargetInterface",
							 "example.objectadapter.Adaptee",
							 "example.objectadapter.AdapterNoTargetInterface"};
		ReturnType results = this.analyzer.getFeedback(classes);
		assertTrue(results.errorsCaught.size() == 0);
	}
	
	@Test
	public void testAdapterNoTargetAbstractClass() {
		this.setupAnalyzer();
		String [] classes = {"example.objectadapter.TargetInterface",
							 "example.objectadapter.Adaptee",
							 "example.objectadapter.AdapterNoTargetAbstractClass"};
		ReturnType results = this.analyzer.getFeedback(classes);
		assertTrue(results.errorsCaught.size() == 0);
	}
	
	@Test
	public void testAdapterBothIncorrect() {
		this.setupAnalyzer();
		String [] classes = {"example.objectadapter.TargetAbstractClass",
							 "example.objectadapter.TargetInterface",
							 "example.objectadapter.Adaptee",
							 "example.objectadapter.AdapterBothIncorrect"};
		ReturnType results = this.analyzer.getFeedback(classes);
		assertTrue(results.errorsCaught.size() == 0);
	}
	
	@Test
	public void testAdapterInterfaceCorrectAbstractClassIncorrect() {
		this.setupAnalyzer();
		String [] classes = {"example.objectadapter.TargetAbstractClass",
							 "example.objectadapter.TargetInterface",
							 "example.objectadapter.Adaptee",
							 "example.objectadapter.AdapterInterfaceCorrectAbstractClassIncorrect"};
		ReturnType results = this.analyzer.getFeedback(classes);
		List<String> patternCatches = new ArrayList<String>();
		assertTrue(results.errorsCaught.size() == 1);
		for(LinterError linterError : results.errorsCaught) {
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
		this.setupAnalyzer();
		String [] classes = {"example.objectadapter.TargetAbstractClass",
							 "example.objectadapter.TargetInterface",
							 "example.objectadapter.Adaptee",
							 "example.objectadapter.AdapterInterfaceIncorrectAbstractClassCorrect"};
		ReturnType results = this.analyzer.getFeedback(classes);
		List<String> patternCatches = new ArrayList<String>();
		assertTrue(results.errorsCaught.size() == 1);
		for(LinterError linterError : results.errorsCaught) {
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
		this.setupAnalyzer();
		String [] classes = {"example.objectadapter.TargetInterface",
							 "example.objectadapter.InterfaceAdaptee",
							 "example.objectadapter.AdapterAdaptsInterface"};
		ReturnType results = this.analyzer.getFeedback(classes);
		List<String> patternCatches = new ArrayList<String>();
		assertTrue(results.errorsCaught.size() == 1);
		for(LinterError linterError : results.errorsCaught) {
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
		this.setupAnalyzer();
		String [] classes = {"example.objectadapter.TargetAbstractClass",
							 "example.objectadapter.AbstractAdaptee",
							 "example.objectadapter.AdapterAdaptsAbstractClass"};
		ReturnType results = this.analyzer.getFeedback(classes);
		List<String> patternCatches = new ArrayList<String>();
		assertTrue(results.errorsCaught.size() == 1);
		for(LinterError linterError : results.errorsCaught) {
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
		this.setupAnalyzer();
		String [] classes = {"example.objectadapter.TargetAbstractClassNoAbstractMethods",
							 "example.objectadapter.AbstractAdaptee",
							 "example.objectadapter.AdapterFalseAbstractClass"};
		ReturnType results = this.analyzer.getFeedback(classes);
		assertTrue(results.errorsCaught.size() == 0);
	}
	
}
