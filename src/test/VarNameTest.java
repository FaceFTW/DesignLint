
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;

import datasource.ASMParser;
import domain.ErrType;
import domain.LinterError;
import domain.ReturnType;
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

		ReturnType returned = this.analyzer.getFeedback(new String[0]);

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

	@Test
	public void testReturnsFieldVariableStartsWithUnderscoreError() {
		setUpSingleClass();
		ReturnType returned = this.analyzer.getFeedback(singleClass);

		assertTrue(returned.errorsCaught.size() > 0);
		List<LinterError> errors = returned.errorsCaught;

		boolean found = false;
		LinterError foundErr = null;
		for (LinterError err : errors) {
			if (err.message.compareTo("_underscoreField begins with _") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertTrue(found);
		assertEquals(singleClass[0], foundErr.className);
		assertNull(foundErr.methodName);
		assertEquals(ErrType.ERROR, foundErr.type);
	}

	@Test
	public void testReturnsFieldVariableStartsWithDollarSignError() {
		setUpSingleClass();
		ReturnType returned = this.analyzer.getFeedback(singleClass);

		assertTrue(returned.errorsCaught.size() > 0);
		List<LinterError> errors = returned.errorsCaught;

		boolean found = false;
		LinterError foundErr = null;
		for (LinterError err : errors) {
			if (err.message.compareTo("$dollarField begins with $") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertTrue(found);
		assertEquals(singleClass[0], foundErr.className);
		assertNull(foundErr.methodName);
		assertEquals(ErrType.ERROR, foundErr.type);
	}

	@Test
	public void testReturnsFieldVariableStartsWithCapitalLetterError() {
		setUpSingleClass();
		ReturnType returned = this.analyzer.getFeedback(singleClass);

		assertTrue(returned.errorsCaught.size() > 0);
		List<LinterError> errors = returned.errorsCaught;

		boolean found = false;
		LinterError foundErr = null;
		for (LinterError err : errors) {
			if (err.message.compareTo("Field BadVariable begins with capital letter") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertTrue(found);
		assertEquals(singleClass[0], foundErr.className);
		assertNull(foundErr.methodName);
		assertEquals(ErrType.ERROR, foundErr.type);
	}

	@Test
	public void testReturnsFieldVariableTooLongError() {
		setUpSingleClass();
		ReturnType returned = this.analyzer.getFeedback(singleClass);

		assertTrue(returned.errorsCaught.size() > 0);
		List<LinterError> errors = returned.errorsCaught;

		boolean found = false;
		LinterError foundErr = null;
		for (LinterError err : errors) {
			if (err.message.compareTo("Field reallyLongFieldForTestingPurposes too long (>30 characters)") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertTrue(found);
		assertEquals(singleClass[0], foundErr.className);
		assertNull(foundErr.methodName);
		assertEquals(ErrType.WARNING, foundErr.type);
	}

	@Test
	public void testReturnsFieldVariableTooShortError() {
		setUpSingleClass();
		ReturnType returned = this.analyzer.getFeedback(singleClass);

		assertTrue(returned.errorsCaught.size() > 0);
		List<LinterError> errors = returned.errorsCaught;

		boolean found = false;
		LinterError foundErr = null;
		for (LinterError err : errors) {
			if (err.message.compareTo("Field fV too short (<=2 characters)") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertTrue(found);
		assertEquals(singleClass[0], foundErr.className);
		assertNull(foundErr.methodName);
		assertEquals(ErrType.WARNING, foundErr.type);
	}

	@Test
	public void testReturnsGlobalVariableStartsWithUnderscoreError() {
		setUpSingleClass();
		ReturnType returned = this.analyzer.getFeedback(singleClass);

		assertTrue(returned.errorsCaught.size() > 0);
		List<LinterError> errors = returned.errorsCaught;

		boolean found = false;
		LinterError foundErr = null;
		for (LinterError err : errors) {
			if (err.message.compareTo("_UNDERSCORE_GLOBAL begins with _") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertTrue(found);
		assertEquals(singleClass[0], foundErr.className);
		assertNull(foundErr.methodName);
		assertEquals(ErrType.ERROR, foundErr.type);
	}

	@Test
	public void testReturnsGlobalVariableStartsWithDollarSignError() {
		setUpSingleClass();
		ReturnType returned = this.analyzer.getFeedback(singleClass);

		assertTrue(returned.errorsCaught.size() > 0);
		List<LinterError> errors = returned.errorsCaught;

		boolean found = false;
		LinterError foundErr = null;
		for (LinterError err : errors) {
			if (err.message.compareTo("$DOLLAR_GLOBAL begins with $") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertTrue(found);
		assertEquals(singleClass[0], foundErr.className);
		assertNull(foundErr.methodName);
		assertEquals(ErrType.ERROR, foundErr.type);
	}

	@Test
	public void testReturnsGlobalVariableTooShortError() {
		setUpSingleClass();
		ReturnType returned = this.analyzer.getFeedback(singleClass);

		assertTrue(returned.errorsCaught.size() > 0);
		List<LinterError> errors = returned.errorsCaught;

		boolean found = false;
		LinterError foundErr = null;
		for (LinterError err : errors) {
			if (err.message.compareTo("Global Variable BG too short (<=2 characters)") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertTrue(found);
		assertEquals(singleClass[0], foundErr.className);
		assertNull(foundErr.methodName);
		assertEquals(ErrType.WARNING, foundErr.type);
	}

	@Test
	public void testReturnsGlobalVariableNotAllCapitalError() {
		setUpSingleClass();
		ReturnType returned = this.analyzer.getFeedback(singleClass);

		assertTrue(returned.errorsCaught.size() > 0);
		List<LinterError> errors = returned.errorsCaught;

		boolean found = false;
		LinterError foundErr = null;
		for (LinterError err : errors) {
			if (err.message.compareTo("Global Variable bad_Global must only be capital letters") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertTrue(found);
		assertEquals(singleClass[0], foundErr.className);
		assertNull(foundErr.methodName);
		assertEquals(ErrType.ERROR, foundErr.type);
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

	@Test
	public void testReturnsLocalVariableStartsWithUnderscoreError() {
		setUpSingleClass();
		ReturnType returned = this.analyzer.getFeedback(singleClass);

		assertTrue(returned.errorsCaught.size() > 0);
		List<LinterError> errors = returned.errorsCaught;

		boolean found = false;
		LinterError foundErr = null;
		for (LinterError err : errors) {
			if (err.message.compareTo("_x begins with _") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertTrue(found);
		assertEquals(singleClass[0], foundErr.className);
		assertEquals("methodName", foundErr.methodName);
		assertEquals(ErrType.ERROR, foundErr.type);
	}

	@Test
	public void testReturnsLocalVariableStartsWithDollarSignError() {
		setUpSingleClass();
		ReturnType returned = this.analyzer.getFeedback(singleClass);

		assertTrue(returned.errorsCaught.size() > 0);
		List<LinterError> errors = returned.errorsCaught;

		boolean found = false;
		LinterError foundErr = null;
		for (LinterError err : errors) {
			if (err.message.compareTo("$y begins with $") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertTrue(found);
		assertEquals(singleClass[0], foundErr.className);
		assertEquals("methodName", foundErr.methodName);
		assertEquals(ErrType.ERROR, foundErr.type);
	}

	@Test
	public void testReturnsLocalVariableStartsWithCapitalLetterError() {
		setUpSingleClass();
		ReturnType returned = this.analyzer.getFeedback(singleClass);

		assertTrue(returned.errorsCaught.size() > 0);
		List<LinterError> errors = returned.errorsCaught;

		boolean found = false;
		LinterError foundErr = null;
		for (LinterError err : errors) {
			if (err.message.compareTo("Local Variable Z begins with capital letter") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertTrue(found);
		assertEquals(singleClass[0], foundErr.className);
		assertEquals("methodName", foundErr.methodName);
		assertEquals(ErrType.ERROR, foundErr.type);
	}

	@Test
	public void testReturnsLocalVariableTooLongError() {
		setUpSingleClass();
		ReturnType returned = this.analyzer.getFeedback(singleClass);

		assertTrue(returned.errorsCaught.size() > 0);
		List<LinterError> errors = returned.errorsCaught;

		boolean found = false;
		LinterError foundErr = null;
		for (LinterError err : errors) {
			if (err.message
					.compareTo("Local Variable superLongUnnecessaryVariableName too long (>30 characters)") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertTrue(found);
		assertEquals(singleClass[0], foundErr.className);
		assertEquals("<init>", foundErr.methodName);
		assertEquals(ErrType.WARNING, foundErr.type);
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
	public void testReturnErrorsFromMultipleClasses() {
		setUpMultiClass();
		ReturnType returned = this.analyzer.getFeedback(multipleClasses);

		assertTrue(testFromClass1(returned.errorsCaught));
		assertTrue(testFromClass2(returned.errorsCaught));
	}

	public boolean testFromClass1(List<LinterError> errors) {
		boolean found = false;
		LinterError foundErr = null;
		for (LinterError err : errors) {
			if (err.message
					.compareTo("Local Variable superLongUnnecessaryVariableName too long (>30 characters)") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertTrue(found);
		assertEquals(multipleClasses[0], foundErr.className);
		assertEquals("<init>", foundErr.methodName);
		assertEquals(ErrType.WARNING, foundErr.type);

		return true;
	}

	public boolean testFromClass2(List<LinterError> errors) {
		boolean found = false;
		LinterError foundErr = null;
		for (LinterError err : errors) {
			if (err.message.compareTo("$BadVariable begins with $") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertTrue(found);
		assertEquals(multipleClasses[1], foundErr.className);
		assertNull(foundErr.methodName);
		assertEquals(ErrType.ERROR, foundErr.type);

		return true;
	}

	@Test
	public void testThirtyCharacterFieldName() {
		setUpSingleClass();
		ReturnType returned = this.analyzer.getFeedback(singleClass);

		assertTrue(returned.errorsCaught.size() > 0);
		List<LinterError> errors = returned.errorsCaught;

		boolean found = false;
		for (LinterError err : errors) {
			if (err.message.compareTo("Field thirtyCharacterVariNameForTest too long (>30 characters)") == 0) {
				found = true;
			}
		}
		for (LinterError err : errors) {
			System.out.println(err);
		}

		assertFalse(found);
	}

	@Test
	public void testThirtyONECharacterFieldName() {
		setUpSingleClass();
		ReturnType returned = this.analyzer.getFeedback(singleClass);

		assertTrue(returned.errorsCaught.size() > 0);
		List<LinterError> errors = returned.errorsCaught;

		boolean found = false;
		LinterError foundErr = null;
		for (LinterError err : errors) {
			if (err.message.compareTo("Field thirtyOneCharacterVarNameToTest too long (>30 characters)") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertTrue(found);
		assertEquals(multipleClasses[0], foundErr.className);
		assertNull(foundErr.methodName);
		assertEquals(ErrType.WARNING, foundErr.type);
	}

	@Test
	public void testThirtyCharacterLocalVarName() {
		setUpSingleClass();
		ReturnType returned = this.analyzer.getFeedback(singleClass);

		assertTrue(returned.errorsCaught.size() > 0);
		List<LinterError> errors = returned.errorsCaught;

		boolean found = false;
		for (LinterError err : errors) {
			if (err.message.compareTo("Local Variable anotherThirtyCharacterVarName1 too long (>30 characters)") == 0) {
				found = true;
			}
		}
		assertFalse(found);
	}

	@Test
	public void testThirtyONECharacterLocalName() {
		setUpSingleClass();
		ReturnType returned = this.analyzer.getFeedback(singleClass);

		assertTrue(returned.errorsCaught.size() > 0);
		List<LinterError> errors = returned.errorsCaught;

		boolean found = false;
		LinterError foundErr = null;
		for (LinterError err : errors) {
			if (err.message
					.compareTo("Local Variable anotherThirty1CharacterVarName1 too long (>30 characters)") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertTrue(found);
		assertEquals(multipleClasses[0], foundErr.className);
		assertEquals("methodName", foundErr.methodName);
		assertEquals(ErrType.WARNING, foundErr.type);
	}
}
