
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import datasource.ASMParser;
import domain.message.LinterMessage;
import domain.AnalyzerReturn;
import domain.analyzer.VarNameAnalyzer;

public class VarNameTest {
	// Combinations of our two classes, make sure we pull the right data
	private final String[] singleClass = { "example/varname/VarNameTestClass" };
	private final String[] multipleClasses = { "example/varname/VarNameTestClass",
			"example/varname/VarNameTestClass2" };

	// Analyzer Class
	private VarNameAnalyzer analyzer;

	// Instantiate the Analyzer Class
	public void setUpSingleClass() {
		try {
			this.analyzer = new VarNameAnalyzer(new ASMParser(singleClass));
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error reading from class files specified in arguments!");
			System.exit(1);
		}
	}

	public void setUpMultiClass() {
		try {
			this.analyzer = new VarNameAnalyzer(new ASMParser(multipleClasses));
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error reading from class files specified in arguments!");
			System.exit(1);
		}
	}

	// Given no classes, all fields within the analyzer should be blank, and return
	// no errors.
	@Test
	public void testFieldsGivenNoClasses() {
		try {
			this.analyzer = new VarNameAnalyzer(new ASMParser(new String[0]));
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error reading from class files specified in arguments!");
			System.exit(1);
		}

		AnalyzerReturn returned = this.analyzer.getFeedback(new String[0]);

		// Analyzer entries are null
		assertEquals(new HashMap<>(), this.analyzer.getFieldNames());
		assertEquals(new HashMap<>(), this.analyzer.getGlobalNames());
		assertEquals(new HashMap<>(), this.analyzer.getMethodNames());

		assertEquals(new ArrayList<>(), returned.errorsCaught);
	}

	@Test
	public void testGetFieldVariables() {
		setUpSingleClass();
		this.analyzer.getRelevantData(singleClass);

		assertTrue(this.analyzer.getFieldNames().get(singleClass[0]).contains("_underscoreField"));
		assertTrue(this.analyzer.getFieldNames().get(singleClass[0]).contains("$dollarField"));
		assertTrue(this.analyzer.getFieldNames().get(singleClass[0]).contains("BadVariable"));
		assertTrue(this.analyzer.getFieldNames().get(singleClass[0]).contains("reallyLongFieldForTestingPurposes"));
		assertTrue(this.analyzer.getFieldNames().get(singleClass[0]).contains("fV"));
	}

	@Test
	public void testGetGlobalVariables() {
		setUpSingleClass();
		this.analyzer.getRelevantData(singleClass);

		assertTrue(this.analyzer.getGlobalNames().get(singleClass[0]).contains("_UNDERSCORE_GLOBAL"));
		assertTrue(this.analyzer.getGlobalNames().get(singleClass[0]).contains("$DOLLAR_GLOBAL"));
		assertTrue(this.analyzer.getGlobalNames().get(singleClass[0]).contains("bad_Global"));
		assertTrue(this.analyzer.getGlobalNames().get(singleClass[0]).contains("BG"));
	}

	@Test
	public void testGetMethodVariables() {
		setUpSingleClass();
		this.analyzer.getRelevantData(singleClass);

		assertTrue(this.analyzer.getMethodNames().get(singleClass[0]).get("methodName").contains("_x"));
		assertTrue(this.analyzer.getMethodNames().get(singleClass[0]).get("methodName").contains("$y"));
		assertTrue(this.analyzer.getMethodNames().get(singleClass[0]).get("<init>")
				.contains("superLongUnnecessaryVariableName"));
		assertTrue(this.analyzer.getMethodNames().get(singleClass[0]).get("methodName").contains("Z"));
	}

	@ParameterizedTest
	@CsvSource({ "_underscoreField begins with _, E",
			"$dollarField begins with $, E",
			"Field BadVariable begins with capital letter, E",
			"Field reallyLongFieldForTestingPurposes too long (>30 characters), W",
			"Field fV too short (<=2 characters), W",
			"_UNDERSCORE_GLOBAL begins with _, E",
			"$DOLLAR_GLOBAL begins with $, E",
			"Global Variable BG too short (<=2 characters), W",
			"Global Variable bad_Global must only be capital letters, E" })
	public void testReturnsVariable(String errorMessage, char errType) {
		setUpSingleClass();
		AnalyzerReturn returned = this.analyzer.getFeedback(singleClass);

		assertTrue(returned.errorsCaught.size() > 0);
		List<LinterMessage> errors = returned.errorsCaught;

		boolean found = false;
		LinterMessage foundErr = null;
		for (LinterMessage err : errors) {
			if (err.message.compareTo(errorMessage) == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertTrue(found);
		assertEquals(singleClass[0], foundErr.className);
		assertNull(foundErr.methodName);
		String expectedErrorType = errType == 'E' ? AnalyzerFixture.ERROR_MSG_TYPE : AnalyzerFixture.WARNING_MSG_TYPE;
		assertEquals(expectedErrorType, foundErr.getMessageType());
	}

	@Test
	public void testThisNotInMethodVariables() {
		setUpSingleClass();
		this.analyzer.getRelevantData(singleClass);

		for (String classKey : this.analyzer.getMethodNames().keySet()) {
			for (String methodKey : this.analyzer.getMethodNames().get(classKey).keySet()) {
				assertFalse(this.analyzer.getMethodNames().get(classKey).get(methodKey).contains("this"));
			}
		}
	}

	@ParameterizedTest
	@ValueSource(strings = { "_x begins with _",
			"$y begins with $",
			"Local Variable Z begins with capital letter" })
	public void testReturnsLocalVariableError(String errorMessage) {
		setUpSingleClass();
		AnalyzerReturn returned = this.analyzer.getFeedback(singleClass);

		assertTrue(returned.errorsCaught.size() > 0);
		List<LinterMessage> errors = returned.errorsCaught;

		boolean found = false;
		LinterMessage foundErr = null;
		for (LinterMessage err : errors) {
			if (err.message.compareTo(errorMessage) == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertTrue(found);
		assertEquals(singleClass[0], foundErr.className);
		assertEquals("methodName", foundErr.methodName);
		assertEquals(AnalyzerFixture.ERROR_MSG_TYPE, foundErr.getMessageType());
	}

	@Test
	public void testReturnsLocalVariableTooLongError() {
		setUpSingleClass();
		AnalyzerReturn returned = this.analyzer.getFeedback(singleClass);

		assertTrue(returned.errorsCaught.size() > 0);
		List<LinterMessage> errors = returned.errorsCaught;

		boolean found = false;
		LinterMessage foundErr = null;
		for (LinterMessage err : errors) {
			if (err.message
					.compareTo("Local Variable superLongUnnecessaryVariableName too long (>30 characters)") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertTrue(found);
		assertEquals(singleClass[0], foundErr.className);
		assertEquals("<init>", foundErr.methodName);
		assertEquals(AnalyzerFixture.WARNING_MSG_TYPE, foundErr.getMessageType());
	}

	@Test
	public void testGetVariablesFromMultipleClasses() {
		setUpMultiClass();
		this.analyzer.getRelevantData(multipleClasses);

		assertTrue(this.analyzer.getFieldNames().get(multipleClasses[0]).contains("reallyLongFieldForTestingPurposes"));
		assertTrue(this.analyzer.getGlobalNames().get(multipleClasses[0]).contains("bad_Global"));
		assertTrue(this.analyzer.getMethodNames().get(multipleClasses[0]).get("methodName").contains("$y"));

		assertTrue(this.analyzer.getFieldNames().get(multipleClasses[1]).contains("$BadVariable"));
		assertTrue(this.analyzer.getGlobalNames().get(multipleClasses[1]).contains("GLOBAL_VARIABLE"));
		assertTrue(this.analyzer.getMethodNames().get(multipleClasses[1]).get("methodName").contains("b"));
	}

	@Test
	public void testReturnErrorsFromClass1() {
		setUpMultiClass();
		AnalyzerReturn returned = this.analyzer.getFeedback(multipleClasses);
		List<LinterMessage> errors = returned.errorsCaught;

		boolean found = false;
		LinterMessage foundErr = null;
		for (LinterMessage err : errors) {
			if (err.message
					.compareTo("Local Variable superLongUnnecessaryVariableName too long (>30 characters)") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertTrue(found);
		assertEquals(multipleClasses[0], foundErr.className);
		assertEquals("<init>", foundErr.methodName);
		assertEquals(AnalyzerFixture.WARNING_MSG_TYPE, foundErr.getMessageType());
	}

	@Test
	public void testReturnErrorsFromClass2() {
		setUpMultiClass();
		AnalyzerReturn returned = this.analyzer.getFeedback(multipleClasses);
		List<LinterMessage> errors = returned.errorsCaught;

		boolean found = false;
		LinterMessage foundErr = null;
		for (LinterMessage err : errors) {
			if (err.message.compareTo("$BadVariable begins with $") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertTrue(found);
		assertEquals(multipleClasses[1], foundErr.className);
		assertNull(foundErr.methodName);
		assertEquals(AnalyzerFixture.ERROR_MSG_TYPE, foundErr.getMessageType());
	}

	@ParameterizedTest
	@ValueSource(strings = { "Field thirtyCharacterVariNameForTest too long (>30 characters)",
			"Local Variable anotherThirtyCharacterVarName1 too long (>30 characters)" })
	public void testThirtyCharacterFieldName(String errorMessage) {
		setUpSingleClass();
		AnalyzerReturn returned = this.analyzer.getFeedback(singleClass);

		assertTrue(returned.errorsCaught.size() > 0);
		List<LinterMessage> errors = returned.errorsCaught;

		boolean found = false;
		for (LinterMessage err : errors) {
			if (err.message.compareTo(errorMessage) == 0) {
				found = true;
			}
		}
		for (LinterMessage err : errors) {
			System.out.println(err);
		}

		assertFalse(found);
	}

	@ParameterizedTest
	@CsvSource({ "Field thirtyOneCharacterVarNameToTest too long (>30 characters), ",
			"Local Variable anotherThirty1CharacterVarName1 too long (>30 characters), methodName" })
	public void testThirtyONECharacterFieldName(String errorMessage, String methodName) {
		setUpSingleClass();
		AnalyzerReturn returned = this.analyzer.getFeedback(singleClass);

		assertTrue(returned.errorsCaught.size() > 0);
		List<LinterMessage> errors = returned.errorsCaught;

		boolean found = false;
		LinterMessage foundErr = null;
		for (LinterMessage err : errors) {
			if (err.message.compareTo(errorMessage) == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertTrue(found);
		assertEquals(multipleClasses[0], foundErr.className);
		assertEquals(methodName, foundErr.methodName);
		assertEquals(AnalyzerFixture.WARNING_MSG_TYPE, foundErr.getMessageType());
	}
}
