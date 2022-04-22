
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import datasource.ASMParser;
import domain.ErrType;
import domain.LinterError;
import domain.ReturnType;
import domain.analyzer.GenericTypeNameAnalyzer;

public class GenericTypeStyleCheckTest extends AnalyzerFixture<GenericTypeNameAnalyzer> {

	private final String[] exampleClasses = { "example.typename.OneTypeCorrectClass",
	"example.typename.OneTypeIncorrectCapitalNonNumericClass",
	"example.typename.MultipleTypesAllCorrectClass",
	"example.typename.OneTypeIncorrectNoTClass",
	"example.typename.MultipleTypesSomeIncorrectClass",
	"example.typename.OneTypeIncorrectLowercaseClass",
	"example.typename.NoTypeClass",
	"example.typename.OneTypeCorrectTClass",
	"example.typename.InterfaceCorrect",
	"example.typename.InterfaceIncorrect",
	"example.typename.AbstractCorrect"};

	@Override
	@BeforeEach
	protected void initAnalyzerUUT() {
		this.populateParserData(exampleClasses);
		this.analyzer = new GenericTypeNameAnalyzer(this.parser);
	}

	@Test
	public void testOneTypeCorrect() {
		String[] classes = { "example.typename.OneTypeCorrectClass" };
		ReturnType results = this.analyzer.getFeedback(classes);
		assertTrue(results.errorsCaught.size() == 0);
	}

	@Test
	public void testNoType() {
		String[] classes = { "example.typename.NoTypeClass" };
		ReturnType results = this.analyzer.getFeedback(classes);
		assertTrue(results.errorsCaught.size() == 0);
	}

	@Test
	public void testOneTypeCorrectT() {
		String[] classes = { "example.typename.OneTypeCorrectTClass" };
		ReturnType results = this.analyzer.getFeedback(classes);
		assertTrue(results.errorsCaught.size() == 0);
	}

	@Test
	public void testOneTypeIncorrectLowercase() {
		String[] classes = { "example.typename.OneTypeIncorrectLowercaseClass" };
		ReturnType results = this.analyzer.getFeedback(classes);
		assertTrue(results.errorsCaught.size() == 1);
		assertTrue(results.errorsCaught.get(0).type.equals(ErrType.WARNING));
		assertEquals(results.errorsCaught.get(0).message,
				"Generic Type: 'incorrect' should be capitalized.");
	}

	@Test
	public void testOneTypeIncorrectCapitalNonNumeric() {
		String[] classes = { "example.typename.OneTypeIncorrectCapitalNonNumericClass" };
		ReturnType results = this.analyzer.getFeedback(classes);
		assertTrue(results.errorsCaught.size() == 1);
		assertTrue(results.errorsCaught.get(0).type.equals(ErrType.WARNING));
		assertEquals(results.errorsCaught.get(0).message,
				"Generic Type: 'GG' is of length 2 and starts with a capital character - second character should be a single numeric.");
	}

	@Test
	public void testOneTypeIncorrectNoT() {
		String[] classes = { "example.typename.OneTypeIncorrectNoTClass" };
		ReturnType results = this.analyzer.getFeedback(classes);
		assertTrue(results.errorsCaught.size() == 1);
		assertTrue(results.errorsCaught.get(0).type.equals(ErrType.WARNING));
		assertEquals(results.errorsCaught.get(0).message,
				"Generic Type: 'Incorrect' is of the class name form - should end in a capital 'T'");
	}

	@Test
	public void testMultipleTypesAllCorrect() {
		String[] classes = { "example.typename.MultipleTypesAllCorrectClass" };
		ReturnType results = this.analyzer.getFeedback(classes);
		assertTrue(results.errorsCaught.size() == 0);
	}

	@Test
	public void testMultipleTypesSomeIncorrect() {
		String[] classes = { "example.typename.MultipleTypesSomeIncorrectClass" };
		ReturnType results = this.analyzer.getFeedback(classes);
		assertTrue(results.errorsCaught.size() == 1);
		assertEquals(results.errorsCaught.get(0).message,
				"Generic Type: 'incorrectAgain' should be capitalized.");
	}

	@Test
	public void testMultipleClassesAllCorrect() {
		String[] classes = { "example.typename.OneTypeCorrectClass",
				"example.typename.MultipleTypesAllCorrectClass" };
		ReturnType results = this.analyzer.getFeedback(classes);
		assertTrue(results.errorsCaught.size() == 0);
	}

	@Test
	public void testMultipleClassesSomeIncorrect() {
		String[] classes = { "example.typename.OneTypeCorrectClass",
				"example.typename.OneTypeIncorrectCapitalNonNumericClass",
				"example.typename.MultipleTypesAllCorrectClass",
				"example.typename.OneTypeIncorrectNoTClass",
				"example.typename.MultipleTypesSomeIncorrectClass",
				"example.typename.OneTypeIncorrectLowercaseClass" };
		ReturnType results = this.analyzer.getFeedback(classes);
		assertTrue(results.errorsCaught.size() == 4);
		List<String> errorMessages = new ArrayList<String>();
		for (LinterError linterError : results.errorsCaught) {
			errorMessages.add(linterError.message);
		}
		assertTrue(errorMessages.contains("Generic Type: 'GG' is of length 2 and starts with a capital character - "
				+ "second character should be a single numeric."));
		assertTrue(errorMessages
				.contains("Generic Type: 'Incorrect' is of the class name form - should end in a capital 'T'"));
		assertTrue(errorMessages.contains("Generic Type: 'incorrect' should be capitalized."));
		assertTrue(errorMessages.contains("Generic Type: 'incorrectAgain' should be capitalized."));

	}

	@Test
	public void testInterfaceCorrect() {
		String[] classes = { "example.typename.InterfaceCorrect" };
		ReturnType results = this.analyzer.getFeedback(classes);
		assertTrue(results.errorsCaught.size() == 0);
	}

	@Test
	public void testInterfaceIncorrect() {
		String[] classes = { "example.typename.InterfaceIncorrect" };
		ReturnType results = this.analyzer.getFeedback(classes);
		assertTrue(results.errorsCaught.size() == 1);
		assertEquals(results.errorsCaught.get(0).message,
				"Generic Type: 'coolT' should be capitalized.");
	}

	@Test
	public void testAbstractClassCorrect() {
		String[] classes = { "example.typename.AbstractCorrect" };
		ReturnType results = this.analyzer.getFeedback(classes);
		assertTrue(results.errorsCaught.size() == 0);
	}
}
