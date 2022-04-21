
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import domain.analyzer.TemplateMethodAnalyzer;

public class TemplateMethodTest {
	private final String[] correctClasses = { "example/template/CaffeineBeverage", "example/template/Tea",
			"example/template/Coffee" };
	private final String[] wrongClasses = { "example/template/NotTemplate", "example/template/NotTemplateSubclass" };
	private final String[] sortOfWrongClasses = { "example/template/AbstractNotTemplate",
			"example/template/AbstractSubclass",
			"example/template/ConcreteSubclass" };
	private final String[] simpleCorrectClasses = { "example/template/Template1Abstract",
			"example/template/Template1AbstractImplement" };
	private final String[] allClasses = { "example/template/CaffeineBeverage", "example/template/Tea",
			"example/template/Coffee", "example/template/NotTemplate", "example/template/NotTemplateSubclass",
			"example/template/AbstractNotTemplate", "example/template/AbstractSubclass",
			"example/template/ConcreteSubclass" };

	// Analyzer Class
	private TemplateMethodAnalyzer analyzer;

	// Instantiate the Analyzer Class
	public void setUpCorrectClasses() {
		try {
			this.analyzer = new TemplateMethodAnalyzer(new ASMParser(correctClasses));
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error reading from class files specified in arguments!");
			System.exit(1);
		}
	}

	public void setUpSimpleCorrectClasses() {
		try {
			this.analyzer = new TemplateMethodAnalyzer(new ASMParser(simpleCorrectClasses));
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error reading from class files specified in arguments!");
			System.exit(1);
		}
	}

	public void setUpAllClasses() {
		try {
			this.analyzer = new TemplateMethodAnalyzer(new ASMParser(allClasses));
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error reading from class files specified in arguments!");
			System.exit(1);
		}
	}

	@Test
	public void testFieldsGivenNoClasses() {
		try {
			this.analyzer = new TemplateMethodAnalyzer(new ASMParser(new String[0]));
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error reading from class files specified in arguments!");
			System.exit(1);
		}

		ReturnType returned = this.analyzer.getFeedback(new String[0]);

		// Analyzer entries are null
		assertEquals(new HashMap<>(), this.analyzer.getExtendedClasses());
		assertEquals(new HashMap<>(), this.analyzer.getAbstractMethods());
		assertEquals(new HashMap<>(), this.analyzer.getConcreteMethods());
		assertEquals(new HashMap<>(), this.analyzer.getAbstractInsideConcreteMethods());

		assertEquals(new ArrayList<>(), returned.errorsCaught);
	}

	@Test
	public void testGetSuperClasses() {
		setUpCorrectClasses();
		this.analyzer.getRelevantData(correctClasses);

		for (String className : this.analyzer.getExtendedClasses().keySet()) {
			System.out.println(className + ":  " + this.analyzer.getExtendedClasses().get(className));
		}

		assertTrue(this.analyzer.getExtendedClasses().get("example/template/Tea")
				.contains("example/template/CaffeineBeverage"));
		assertTrue(this.analyzer.getExtendedClasses().get("example/template/Coffee")
				.contains("example/template/CaffeineBeverage"));
	}

	@Test
	public void testGetAbstractMethod() {
		setUpCorrectClasses();
		this.analyzer.getRelevantData(correctClasses);

		System.out.println(this.analyzer.getAbstractMethods().get("example/template/CaffeineBeverage"));

		List<String> list = new ArrayList<>();
		for (List<String> innerList : this.analyzer.getAbstractMethods().get("example/template/CaffeineBeverage")) {
			list.add(innerList.get(0));
		}

		assertTrue(list.contains("brew"));
		assertTrue(list.contains("addCondiments"));
	}

	@Test
	public void testGetConcreteMethod() {
		setUpCorrectClasses();
		this.analyzer.getRelevantData(correctClasses);

		System.out.println(this.analyzer.getConcreteMethods().get("example/template/CaffeineBeverage"));

		List<String> list = new ArrayList<>();
		for (List<String> innerList : this.analyzer.getConcreteMethods().get("example/template/CaffeineBeverage")) {
			list.add(innerList.get(0));
		}

		assertTrue(list.contains("prepareRecipe"));
		assertTrue(list.contains("boilWater"));
		assertTrue(list.contains("pourInCup"));

		List<String> list2 = new ArrayList<>();
		for (List<String> innerList : this.analyzer.getConcreteMethods().get("example/template/Tea")) {
			list2.add(innerList.get(0));
		}

		assertTrue(list2.contains("brew"));
		assertTrue(list2.contains("addCondiments"));
	}

	@Test
	public void testGetAbstractInConcrete() {
		setUpAllClasses();
		this.analyzer.getRelevantData(allClasses);

		for (String className : this.analyzer.getAbstractInsideConcreteMethods().keySet()) {
			for (List<String> methodName : this.analyzer.getAbstractInsideConcreteMethods().get(className).keySet()) {
				System.out.println(className + " " + methodName + " " +
						this.analyzer.getAbstractInsideConcreteMethods().get(className).get(methodName));
			}
		}
		// System.out.println(this.analyzer.getMethodsInsideAbstractMethods());
		List<String> l1 = new ArrayList<>();
		l1.add("prepareRecipe");
		l1.add("()V");
		List<String> l2 = new ArrayList<>();
		l2.add("testFunc");
		l2.add("()V");

		assertNotNull(
				this.analyzer.getAbstractInsideConcreteMethods().get("example/template/CaffeineBeverage").get(l1));
		assertNotNull(
				this.analyzer.getAbstractInsideConcreteMethods().get("example/template/AbstractNotTemplate").get(l2));
	}

	@Test
	public void testTemplateMethodFound1() {
		setUpCorrectClasses();
		ReturnType returned = this.analyzer.getFeedback(correctClasses);

		List<LinterError> patterns = returned.errorsCaught;

		boolean found = false;
		LinterError foundErr = null;
		for (LinterError err : patterns) {
			if (err.message.compareTo(
					"Template Method Pattern Found: prepareRecipe (Subclass: example/template/Coffee)") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertTrue(found);
		assertEquals("example/template/CaffeineBeverage", foundErr.className);
		assertEquals("prepareRecipe", foundErr.methodName);
		assertEquals(ErrType.PATTERN, foundErr.type);
	}

	@Test
	public void testTemplateMethodFound2() {
		setUpCorrectClasses();
		ReturnType returned = this.analyzer.getFeedback(correctClasses);

		List<LinterError> patterns = returned.errorsCaught;

		boolean found = false;
		LinterError foundErr = null;
		for (LinterError err : patterns) {
			if (err.message
					.compareTo("Template Method Pattern Found: prepareRecipe (Subclass: example/template/Tea)") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertTrue(found);
		assertEquals("example/template/CaffeineBeverage", foundErr.className);
		assertEquals("prepareRecipe", foundErr.methodName);
		assertEquals(ErrType.PATTERN, foundErr.type);
	}

	@Test
	public void testTemplateMethodFoundOfLength1() {
		setUpSimpleCorrectClasses();
		ReturnType returned = this.analyzer.getFeedback(simpleCorrectClasses);

		for (String className : this.analyzer.getAbstractInsideConcreteMethods().keySet()) {
			System.out.println(className + " " + this.analyzer.getAbstractInsideConcreteMethods().get(className));
		}
		// System.out.println(this.analyzer.foundPatterns);

		List<LinterError> patterns = returned.errorsCaught;

		boolean found = false;
		LinterError foundErr = null;
		for (LinterError err : patterns) {
			if (err.message.compareTo(
					"Template Method Pattern Found: testAbstract (Subclass: example/template/Template1AbstractImplement)") == 0) {
				found = true;
				foundErr = err;
			}
		}

		assertTrue(found);
		assertEquals("example/template/Template1Abstract", foundErr.className);
		assertEquals("testAbstract", foundErr.methodName);
		assertEquals(ErrType.PATTERN, foundErr.type);
	}

	@Test
	public void testTemplateMethodFoundAfterRemovingAbstractMethodData() {
		setUpCorrectClasses();
		this.analyzer.getRelevantData(correctClasses);
		// Bout to meddle
		this.analyzer.getAbstractMethods().get("example/template/CaffeineBeverage").remove(0);
		this.analyzer.analyzeData();
		ReturnType returned = this.analyzer.composeReturnType();

		List<LinterError> patterns = returned.errorsCaught;

		boolean found = false;
		LinterError foundErr = null;
		for (LinterError err : patterns) {
			if (err.message
					.compareTo("Template Method Pattern Found: prepareRecipe (Subclass: example/template/Tea)") == 0) {
				found = true;
				foundErr = err;
			}
		}
		assertFalse(found);
	}

	@Test
	public void testTemplateMethodNotFound() {
		setUpAllClasses();
		ReturnType returned = this.analyzer.getFeedback(allClasses);

		List<LinterError> patterns = returned.errorsCaught;

		boolean found = false;
		for (LinterError err : patterns) {
			if (err.message.compareTo(
					"Template Method Pattern Found: testFunc (Subclass: example/template/ConcreteSubclass)") == 0) {
				found = true;
			}
		}
		for (LinterError err : patterns) {
			System.out.println(err);
		}
		assertFalse(found);
	}

	@Test
	public void testTemplateMethodNotFound2() {
		setUpAllClasses();
		ReturnType returned = this.analyzer.getFeedback(allClasses);

		List<LinterError> patterns = returned.errorsCaught;

		boolean found = false;
		for (LinterError err : patterns) {
			if (err.message.compareTo(
					"Template Method Pattern Found: prepareRecipe (Subclass: example/template/CaffeineBeverage)") == 0) {
				found = true;
			}
		}
		for (LinterError err : patterns) {
			System.out.println(err);
		}
		assertFalse(found);
	}

	@Test
	public void testTemplateMethodNotFound3() {
		setUpAllClasses();
		ReturnType returned = this.analyzer.getFeedback(allClasses);

		List<LinterError> patterns = returned.errorsCaught;

		boolean found = false;
		for (LinterError err : patterns) {
			if (err.message.compareTo(
					"Template Method Pattern Found: notTemplate (Subclass: example/template/NotTemplate)") == 0) {
				found = true;
			}
		}
		for (LinterError err : patterns) {
			System.out.println(err);
		}
		assertFalse(found);
	}
}
