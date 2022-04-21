
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
import domain.analyzer.CodeToInterfaceAnalyzer;

public class CodeToInterfaceTest {
	private final String[] ourClass = { "example/code2interface/Code2InterfaceTest" };

	// Analyzer Class
	private CodeToInterfaceAnalyzer analyzer;

	// Instantiate the Analyzer Class
	public void setUpOurClass() {
		try {
			this.analyzer = new CodeToInterfaceAnalyzer(new ASMParser(ourClass));
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
			this.analyzer = new CodeToInterfaceAnalyzer(new ASMParser(new String[0]));
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error reading from class files specified in arguments!");
			System.exit(1);
		}

		ReturnType returned = this.analyzer.getFeedback(new String[0]);

		// Analyzer entries are null
		assertEquals(new HashMap<>(), this.analyzer.getFieldNames());
		assertEquals(new HashMap<>(), this.analyzer.getFieldTypes());
		assertEquals(new HashMap<>(), this.analyzer.getMethodVarNames());
		assertEquals(new HashMap<>(), this.analyzer.getMethodVarTypes());
		assertEquals(new HashMap<>(), this.analyzer.getPossibleInterfaces());

		assertEquals(new ArrayList<>(), returned.errorsCaught);
	}

	@Test
	public void testFieldsGivenClasses() {
		setUpOurClass();
		ReturnType returned = this.analyzer.getFeedback(ourClass);

		assertNotEquals(new HashMap<>(), this.analyzer.getFieldNames());
		assertNotEquals(new HashMap<>(), this.analyzer.getFieldTypes());
		assertNotEquals(new HashMap<>(), this.analyzer.getMethodVarNames());
		assertNotEquals(new HashMap<>(), this.analyzer.getMethodVarTypes());
		assertNotEquals(new HashMap<>(), this.analyzer.getPossibleInterfaces());

		assertNotEquals(new ArrayList<>(), returned.errorsCaught);
	}

	@Test
	public void testReturnedArrayListField() {
		setUpOurClass();
		ReturnType returned = this.analyzer.getFeedback(ourClass);

		assertTrue(returned.errorsCaught.size() > 0);
		List<LinterError> errors = returned.errorsCaught;

		boolean found = false;
		LinterError foundErr = null;
		for (LinterError err : errors) {
			if (err.message.compareTo("Field fieldListTest should be of type List<>, not ArrayList<>") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertTrue(found);
		assertEquals(ourClass[0], foundErr.className);
		assertNull(foundErr.methodName);
		assertEquals(ErrType.ERROR, foundErr.type);
	}

	@Test
	public void testReturnedHashMapField() {
		setUpOurClass();
		ReturnType returned = this.analyzer.getFeedback(ourClass);

		assertTrue(returned.errorsCaught.size() > 0);
		List<LinterError> errors = returned.errorsCaught;

		boolean found = false;
		LinterError foundErr = null;
		for (LinterError err : errors) {
			if (err.message.compareTo("Field fieldMapTest should be of type Map<>, not HashMap<>") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertTrue(found);
		assertEquals(ourClass[0], foundErr.className);
		assertNull(foundErr.methodName);
		assertEquals(ErrType.ERROR, foundErr.type);
	}

	@Test
	public void testReturnedHashSetField() {
		setUpOurClass();
		ReturnType returned = this.analyzer.getFeedback(ourClass);

		assertTrue(returned.errorsCaught.size() > 0);
		List<LinterError> errors = returned.errorsCaught;

		boolean found = false;
		LinterError foundErr = null;
		for (LinterError err : errors) {
			if (err.message.compareTo("Field fieldSetTest should be of type Set<>, not HashSet<>") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertTrue(found);
		assertEquals(ourClass[0], foundErr.className);
		assertNull(foundErr.methodName);
		assertEquals(ErrType.ERROR, foundErr.type);
	}

	@Test
	public void testFieldWithNoInterfacesToImplement() {
		setUpOurClass();
		ReturnType returned = this.analyzer.getFeedback(ourClass);

		assertTrue(returned.errorsCaught.size() > 0);
		List<LinterError> errors = returned.errorsCaught;

		boolean found = false;
		LinterError foundErr = null;
		for (LinterError err : errors) {
			if (err.message.compareTo("Possible Interface for triangleTest: example/code2interface/Shape") == 0 ||
					err.message
							.compareTo("Possible Interface for triangleTest: example/code2interface/Triangle") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertFalse(found);
	}

	// Tests that tri1 will detect being type Shape
	@Test
	public void testMethodVarOfType1() {
		setUpOurClass();
		ReturnType returned = this.analyzer.getFeedback(ourClass);

		assertTrue(returned.errorsCaught.size() > 0);
		List<LinterError> errors = returned.errorsCaught;

		boolean found = false;
		LinterError foundErr = null;
		for (LinterError err : errors) {
			if (err.message.compareTo("Potential Interface for tri1: example/code2interface/Shape") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertTrue(found);
		assertEquals(ourClass[0], foundErr.className);
		assertEquals("testFunc", foundErr.methodName);
		assertEquals(ErrType.WARNING, foundErr.type);
	}

	@Test
	public void testMethodVarOfType2() {
		setUpOurClass();
		ReturnType returned = this.analyzer.getFeedback(ourClass);

		assertTrue(returned.errorsCaught.size() > 0);
		List<LinterError> errors = returned.errorsCaught;

		boolean found = false;
		LinterError foundErr = null;
		for (LinterError err : errors) {
			if (err.message.compareTo("Potential Interface for tri2: example/code2interface/Printable") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertTrue(found);
		assertEquals(ourClass[0], foundErr.className);
		assertEquals("testFunc", foundErr.methodName);
		assertEquals(ErrType.WARNING, foundErr.type);
	}

	// Tri3 is called by two Shape methods, getArea and compareShape
	@Test
	public void testMethodVarOfType3() {
		setUpOurClass();
		ReturnType returned = this.analyzer.getFeedback(ourClass);

		assertTrue(returned.errorsCaught.size() > 0);
		List<LinterError> errors = returned.errorsCaught;

		boolean found = false;
		LinterError foundErr = null;
		for (LinterError err : errors) {
			if (err.message.compareTo("Potential Interface for tri3: example/code2interface/Shape") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertTrue(found);
		assertEquals(ourClass[0], foundErr.className);
		assertEquals("testFunc", foundErr.methodName);
		assertEquals(ErrType.WARNING, foundErr.type);
	}

	// Tri4 called a Triangle method, and should not throw an error.
	@Test
	public void testMethodVarOfType4() {
		setUpOurClass();
		ReturnType returned = this.analyzer.getFeedback(ourClass);

		assertTrue(returned.errorsCaught.size() > 0);
		List<LinterError> errors = returned.errorsCaught;

		boolean found = false;
		LinterError foundErr = null;
		for (LinterError err : errors) {
			if (err.message.compareTo("Potential Interface for tri4: example/code2interface/Shape") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertFalse(found);
	}

	// Tri5 calls another Triangle method, so it should not throw an error.
	@Test
	public void testMethodVarOfType5() {
		setUpOurClass();
		ReturnType returned = this.analyzer.getFeedback(ourClass);

		assertTrue(returned.errorsCaught.size() > 0);
		List<LinterError> errors = returned.errorsCaught;

		boolean found = false;
		LinterError foundErr = null;
		for (LinterError err : errors) {
			if (err.message.compareTo("Potential Interface for tri5: example/code2interface/Shape") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertFalse(found);
	}

	// Tri6 already a Shape (even if a Triangle object), so it should not throw an
	// error.
	@Test
	public void testMethodVarOfType6() {
		setUpOurClass();
		ReturnType returned = this.analyzer.getFeedback(ourClass);

		assertTrue(returned.errorsCaught.size() > 0);
		List<LinterError> errors = returned.errorsCaught;

		boolean found = false;
		LinterError foundErr = null;
		for (LinterError err : errors) {
			if (err.message.compareTo("Potential Interface for tri6: example/code2interface/Shape") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertFalse(found);
	}

	@Test
	public void testMethodVarList() {
		setUpOurClass();
		ReturnType returned = this.analyzer.getFeedback(ourClass);

		assertTrue(returned.errorsCaught.size() > 0);
		List<LinterError> errors = returned.errorsCaught;

		boolean found = false;
		LinterError foundErr = null;
		for (LinterError err : errors) {
			if (err.message.compareTo("Local Variable testList should be of type List<>, not ArrayList<>") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertTrue(found);
		assertEquals(ourClass[0], foundErr.className);
		assertEquals("anotherFunc", foundErr.methodName);
		assertEquals(ErrType.ERROR, foundErr.type);
	}

	@Test
	public void testMethodVarMap() {
		setUpOurClass();
		ReturnType returned = this.analyzer.getFeedback(ourClass);

		assertTrue(returned.errorsCaught.size() > 0);
		List<LinterError> errors = returned.errorsCaught;

		boolean found = false;
		LinterError foundErr = null;
		for (LinterError err : errors) {
			if (err.message.compareTo("Local Variable mapTest should be of type Map<>, not HashMap<>") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertTrue(found);
		assertEquals(ourClass[0], foundErr.className);
		assertEquals("anotherFunc", foundErr.methodName);
		assertEquals(ErrType.ERROR, foundErr.type);
	}

	@Test
	public void testMethodVarSet() {
		setUpOurClass();
		ReturnType returned = this.analyzer.getFeedback(ourClass);

		assertTrue(returned.errorsCaught.size() > 0);
		List<LinterError> errors = returned.errorsCaught;

		boolean found = false;
		LinterError foundErr = null;
		for (LinterError err : errors) {
			if (err.message.compareTo("Local Variable setTest should be of type Set<>, not HashSet<>") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertTrue(found);
		assertEquals(ourClass[0], foundErr.className);
		assertEquals("anotherFunc", foundErr.methodName);
		assertEquals(ErrType.ERROR, foundErr.type);
	}

	@Test
	public void testFindShortcutExits() {
		setUpOurClass();
		this.analyzer.getRelevantData(ourClass);

		// Variable Does Not Exist
		assertFalse(this.analyzer.findShortCut(ourClass[0], "testFunc", "tri7"));
		assertTrue(this.analyzer.findShortCut(ourClass[0], "anotherFunc", "testList"));
		assertTrue(this.analyzer.findShortCut(ourClass[0], "anotherFunc", "mapTest"));
		assertTrue(this.analyzer.findShortCut(ourClass[0], "anotherFunc", "setTest"));
	}

	@Test
	public void testClassImplementsOwnMethod() {
		setUpOurClass();
		this.analyzer.getRelevantData(ourClass);

		// Variable Does Not Exist
		assertFalse(this.analyzer.findShortCut(ourClass[0], "testFunc", "tri7"));
		assertTrue(this.analyzer.findShortCut(ourClass[0], "anotherFunc", "testList"));
		assertTrue(this.analyzer.findShortCut(ourClass[0], "anotherFunc", "mapTest"));
		assertTrue(this.analyzer.findShortCut(ourClass[0], "anotherFunc", "setTest"));
	}

}
