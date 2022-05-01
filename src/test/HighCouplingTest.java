
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.ErrType;
import domain.LinterError;
import domain.AnalyzerReturn;
import domain.analyzer.HighCouplingAnalyzer;

public class HighCouplingTest extends AnalyzerFixture<HighCouplingAnalyzer>{

	private final String[] exampleClasses = {
			"example/coupling/ZeroCouplingDataStruct",
			"example/coupling/ZeroCouplingObject",
			"example/coupling/ZeroCouplingStaticClass",
			"example/coupling/LowCouplingDataStruct",
			"example/coupling/LowCouplingDataStruct2",
			"example/coupling/LowCouplingObject",
			"example/coupling/LowCouplingStaticClass",
			"example/coupling/HighCouplingDataStructProjectCount",
			"example/coupling/HighCouplingDataStructTotalCount",
			"example/coupling/HighCouplingObjectProjectCount",
			"example/coupling/HighCouplingObjectTotalCount",
			"example/coupling/HighCouplingStaticClassProjectCount",
			"example/coupling/HighCouplingStaticClassTotalCount",
			"example/coupling/HighCouplingNightmareClass",
			"example/coupling/CouplingInterfaceExample",
			"example/coupling/CoupledToInterfaceExample",
	};
	@Override
	@BeforeEach
	protected void initAnalyzerUUT() {
		this.populateParserData(exampleClasses);
		this.analyzer = new HighCouplingAnalyzer(parser);
		analyzer.getRelevantData(exampleClasses);
		
	}

	// =====================Compliant Classes===================== //

	@Test
	public void testZeroCouplingDataStruct() {
		String[] expected = {};
		int expectedJRECount = 0;
		String[] actual = analyzer.countClassCoupling("example/coupling/ZeroCouplingDataStruct");
		int jreCount = analyzer.determineJavaCoupling(actual);

		assertEquals(expected.length, actual.length);

		// Convert to Lists so we can use containsAll
		List<String> expectedList = Arrays.asList(expected);
		List<String> actualList = Arrays.asList(actual);

		assertTrue(expectedList.containsAll(actualList));
		assertTrue(actualList.containsAll(expectedList));

		// Now we check the JRE counts
		assertEquals(expectedJRECount, jreCount);
	}

	@Test
	public void testZeroCouplingObject() {
		String[] expected = {};
		int expectedJRECount = 0;
		String[] actual = analyzer.countClassCoupling("example/coupling/ZeroCouplingObject");
		int jreCount = analyzer.determineJavaCoupling(actual);

		assertEquals(expected.length, actual.length);

		// Convert to Lists so we can use containsAll
		List<String> expectedList = Arrays.asList(expected);
		List<String> actualList = Arrays.asList(actual);

		assertTrue(expectedList.containsAll(actualList));
		assertTrue(actualList.containsAll(expectedList));

		// Now we check the JRE counts
		assertEquals(expectedJRECount, jreCount);
	}

	@Test
	public void testZeroCouplingStaticClass() {
		String[] expected = {};
		int expectedJRECount = 0;
		String[] actual = analyzer.countClassCoupling("example/coupling/ZeroCouplingStaticClass");
		int jreCount = analyzer.determineJavaCoupling(actual);

		assertEquals(expected.length, actual.length);

		// Convert to Lists so we can use containsAll
		List<String> expectedList = Arrays.asList(expected);
		List<String> actualList = Arrays.asList(actual);

		assertTrue(expectedList.containsAll(actualList));
		assertTrue(actualList.containsAll(expectedList));

		// Now we check the JRE counts
		assertEquals(expectedJRECount, jreCount);
	}

	@Test
	public void testLowCouplingObject() {
		String[] expected = {
				"java/lang/String",
				"java/lang/System",
				"java/io/PrintStream",
				"example/coupling/ZeroCouplingObject"
		};
		int expectedJRECount = 3;
		String[] actual = analyzer.countClassCoupling("example/coupling/LowCouplingObject");
		int jreCount = analyzer.determineJavaCoupling(actual);

		assertEquals(expected.length, actual.length);

		// Convert to Lists so we can use containsAll
		List<String> expectedList = Arrays.asList(expected);
		List<String> actualList = Arrays.asList(actual);

		assertTrue(expectedList.containsAll(actualList));
		assertTrue(actualList.containsAll(expectedList));

		// Now we check the JRE counts
		assertEquals(expectedJRECount, jreCount);
	}

	@Test
	public void testLowCouplingDataStruct() {
		String[] expected = {
				"java/lang/String",
				"java/util/List",
				"java/util/ArrayList"
		};
		int expectedJRECount = 3;
		String[] actual = analyzer.countClassCoupling("example/coupling/LowCouplingDataStruct");
		int jreCount = analyzer.determineJavaCoupling(actual);

		assertEquals(expected.length, actual.length);

		// Convert to Lists so we can use containsAll
		List<String> expectedList = Arrays.asList(expected);
		List<String> actualList = Arrays.asList(actual);

		assertTrue(expectedList.containsAll(actualList));
		assertTrue(actualList.containsAll(expectedList));

		// Now we check the JRE counts
		assertEquals(expectedJRECount, jreCount);
	}

	@Test
	public void testLowCouplingDataStruct2() {
		String[] expected = {
				"example/coupling/LowCouplingDataStruct",
				"example/coupling/ZeroCouplingDataStruct",
				"java/util/List"
		};
		int expectedJRECount = 1;
		String[] actual = analyzer.countClassCoupling("example/coupling/LowCouplingDataStruct2");
		int jreCount = analyzer.determineJavaCoupling(actual);

		assertEquals(expected.length, actual.length);

		// Convert to Lists so we can use containsAll
		List<String> expectedList = Arrays.asList(expected);
		List<String> actualList = Arrays.asList(actual);

		assertTrue(expectedList.containsAll(actualList));
		assertTrue(actualList.containsAll(expectedList));

		// Now we check the JRE counts
		assertEquals(expectedJRECount, jreCount);
	}

	@Test
	public void testLowCouplingStaticClass() {
		String[] expected = {
				"example/coupling/ZeroCouplingDataStruct",
				"example/coupling/ZeroCouplingObject",
				"example/coupling/ZeroCouplingStaticClass"
		};
		int expectedJRECount = 0;
		String[] actual = analyzer.countClassCoupling("example/coupling/LowCouplingStaticClass");
		int jreCount = analyzer.determineJavaCoupling(actual);

		assertEquals(expected.length, actual.length);

		// Convert to Lists so we can use containsAll
		List<String> expectedList = Arrays.asList(expected);
		List<String> actualList = Arrays.asList(actual);

		assertTrue(expectedList.containsAll(actualList));
		assertTrue(actualList.containsAll(expectedList));

		// Now we check the JRE counts
		assertEquals(expectedJRECount, jreCount);
	}

	// ===================Non-Compliant Classes=================== //

	@Test
	public void testHighCouplingDataStructTotalCount() {
		String[] expected = {
				"java/util/HashMap",
				"java/util/Map",
				"java/util/List",
				"java/util/ArrayList",
				"java/util/Stack",
				"java/util/Queue",
				"java/util/Set",
				"java/util/Scanner",
				"java/util/TreeSet",
				"java/util/LinkedList",
				"java/lang/String",
				"java/lang/Integer",
				"java/util/Random",
				"java/lang/StringBuilder",
				"example/coupling/ZeroCouplingDataStruct",
				"example/coupling/ZeroCouplingObject",
				"example/coupling/LowCouplingDataStruct",
				"example/coupling/LowCouplingDataStruct2",
				"example/coupling/LowCouplingObject",
				"example/coupling/LowCouplingStaticClass",

		};
		int expectedJRECount = 14;
		String[] actual = analyzer.countClassCoupling("example/coupling/HighCouplingDataStructTotalCount");
		int jreCount = analyzer.determineJavaCoupling(actual);

		assertEquals(expected.length, actual.length);

		// Convert to Lists so we can use containsAll
		List<String> expectedList = Arrays.asList(expected);
		List<String> actualList = Arrays.asList(actual);

		assertTrue(expectedList.containsAll(actualList));
		assertTrue(actualList.containsAll(expectedList));

		// Now we check the JRE counts
		assertEquals(expectedJRECount, jreCount);
	}

	@Test
	public void testHighCouplingDataStructProjectCount() {
		String[] expected = {
				"example/coupling/ZeroCouplingDataStruct",
				"example/coupling/ZeroCouplingObject",
				"example/coupling/ZeroCouplingStaticClass",
				"example/coupling/LowCouplingDataStruct",
				"example/coupling/LowCouplingDataStruct2",
				"example/coupling/LowCouplingObject",
				"example/coupling/LowCouplingStaticClass",
				"example/coupling/HighCouplingObjectTotalCount",
				"example/coupling/HighCouplingObjectProjectCount"
		};
		int expectedJRECount = 0;
		String[] actual = analyzer.countClassCoupling("example/coupling/HighCouplingDataStructProjectCount");
		int jreCount = analyzer.determineJavaCoupling(actual);

		assertEquals(expected.length, actual.length);

		// Convert to Lists so we can use containsAll
		List<String> expectedList = Arrays.asList(expected);
		List<String> actualList = Arrays.asList(actual);

		assertTrue(expectedList.containsAll(actualList));
		assertTrue(actualList.containsAll(expectedList));

		// Now we check the JRE counts
		assertEquals(expectedJRECount, jreCount);
	}

	@Test
	public void testHighCouplingObjectTotalCount() {
		String[] expected = {
				"java/util/HashMap",
				"java/util/Map",
				"java/util/List",
				"java/util/ArrayList",
				"java/util/Stack",
				"java/util/Queue",
				"java/util/Set",
				"java/util/Scanner",
				"java/util/TreeSet",
				"java/util/LinkedList",
				"java/lang/String",
				"java/lang/Integer",
				"java/util/Random",
				"java/lang/StringBuilder",
				"java/io/PrintStream",
				"example/coupling/ZeroCouplingDataStruct",
				"example/coupling/ZeroCouplingObject",
				"example/coupling/ZeroCouplingStaticClass",
				"example/coupling/LowCouplingDataStruct",
				"example/coupling/LowCouplingDataStruct2",
				"example/coupling/LowCouplingObject",
				"example/coupling/LowCouplingStaticClass",

		};
		int expectedJRECount = 15;
		String[] actual = analyzer.countClassCoupling("example/coupling/HighCouplingObjectTotalCount");
		int jreCount = analyzer.determineJavaCoupling(actual);

		assertEquals(expected.length, actual.length);

		// Convert to Lists so we can use containsAll
		List<String> expectedList = Arrays.asList(expected);
		List<String> actualList = Arrays.asList(actual);

		assertTrue(expectedList.containsAll(actualList));
		assertTrue(actualList.containsAll(expectedList));

		// Now we check the JRE counts
		assertEquals(expectedJRECount, jreCount);
	}

	@Test
	public void testHighCouplingObjectProjectCount() {
		String[] expected = {
				"example/coupling/ZeroCouplingDataStruct",
				"example/coupling/ZeroCouplingObject",
				"example/coupling/ZeroCouplingStaticClass",
				"example/coupling/LowCouplingDataStruct",
				"example/coupling/LowCouplingDataStruct2",
				"example/coupling/LowCouplingObject",
				"example/coupling/HighCouplingObjectTotalCount",
				"example/coupling/HighCouplingNightmareClass",
				"example/coupling/HighCouplingDataStructTotalCount"
		};
		int expectedJRECount = 0;
		String[] actual = analyzer.countClassCoupling("example/coupling/HighCouplingObjectProjectCount");
		int jreCount = analyzer.determineJavaCoupling(actual);

		assertEquals(expected.length, actual.length);

		// Convert to Lists so we can use containsAll
		List<String> expectedList = Arrays.asList(expected);
		List<String> actualList = Arrays.asList(actual);

		assertTrue(expectedList.containsAll(actualList));
		assertTrue(actualList.containsAll(expectedList));

		// Now we check the JRE counts
		assertEquals(expectedJRECount, jreCount);
	}

	@Test
	public void testHighCouplingStaticClassTotalCount() {
		String[] expected = {
				"java/util/List",
				"java/util/ArrayList",
				"java/lang/String",
				"java/util/Random",
				"java/io/InputStream",
				"java/io/ByteArrayInputStream",
				"java/io/PrintStream",
				"java/lang/Integer",
				"java/lang/Double",
				"java/lang/Character",
				"java/lang/Byte",
				"java/lang/Long",
				"java/lang/Short",
				"example/coupling/ZeroCouplingDataStruct",
				"example/coupling/ZeroCouplingObject",
				"example/coupling/LowCouplingDataStruct",
				"example/coupling/LowCouplingDataStruct2",
				"example/coupling/LowCouplingObject",
				"example/coupling/LowCouplingStaticClass",
				"example/coupling/ZeroCouplingStaticClass",
		};
		int expectedJRECount = 13;
		String[] actual = analyzer.countClassCoupling("example/coupling/HighCouplingStaticClassTotalCount");
		int jreCount = analyzer.determineJavaCoupling(actual);

		// For debugging to determine if I missed a class by accident
		Arrays.sort(expected);
		Arrays.sort(actual);

		assertEquals(expected.length, actual.length);

		// Convert to Lists so we can use containsAll
		List<String> expectedList = Arrays.asList(expected);
		List<String> actualList = Arrays.asList(actual);

		assertTrue(expectedList.containsAll(actualList));
		assertTrue(actualList.containsAll(expectedList));

		// Now we check the JRE counts
		assertEquals(expectedJRECount, jreCount);
	}

	@Test
	public void testHighCouplingStaticClassProjectCount() {
		String[] expected = {
				"example/coupling/ZeroCouplingDataStruct",
				"example/coupling/ZeroCouplingObject",
				"example/coupling/ZeroCouplingStaticClass",
				"example/coupling/LowCouplingObject",
				"example/coupling/LowCouplingStaticClass",
				"example/coupling/LowCouplingDataStruct",
				"example/coupling/LowCouplingDataStruct2",
				"example/coupling/HighCouplingObjectTotalCount",
				"example/coupling/HighCouplingDataStructProjectCount",
				"java/lang/String",
				"java/io/InputStream",
				"java/io/ByteArrayInputStream"
		};
		int expectedJRECount = 3;
		String[] actual = analyzer.countClassCoupling("example/coupling/HighCouplingStaticClassProjectCount");
		int jreCount = analyzer.determineJavaCoupling(actual);

		assertEquals(expected.length, actual.length);

		// Convert to Lists so we can use containsAll
		List<String> expectedList = Arrays.asList(expected);
		List<String> actualList = Arrays.asList(actual);

		assertTrue(expectedList.containsAll(actualList));
		assertTrue(actualList.containsAll(expectedList));

		// Now we check the JRE counts
		assertEquals(expectedJRECount, jreCount);
	}

	@Test
	public void testHighCouplingNightmareClass() {
		String[] expected = {
				"java/util/HashMap",
				"java/util/Map",
				"java/util/List",
				"java/util/ArrayList",
				"java/util/Stack",
				"java/util/Queue",
				"java/util/Set",
				"java/util/TreeSet",
				"java/util/LinkedList",
				"java/lang/String",
				"java/lang/Integer",
				"java/util/Random",
				"java/io/InputStream",
				"java/util/concurrent/ConcurrentLinkedDeque",
				"java/io/ByteArrayInputStream",
				"java/io/PrintStream",
				"java/lang/Double",
				"java/lang/Character",
				"java/lang/Byte",
				"java/lang/Long",
				"java/lang/Short",
				"example/coupling/ZeroCouplingDataStruct",
				"example/coupling/ZeroCouplingObject",
				"example/coupling/LowCouplingDataStruct",
				"example/coupling/LowCouplingDataStruct2",
				"example/coupling/LowCouplingObject",
				"example/coupling/LowCouplingStaticClass",
				"example/coupling/ZeroCouplingStaticClass",
		};
		int expectedJRECount = 21;
		String[] actual = analyzer.countClassCoupling("example/coupling/HighCouplingNightmareClass");
		int jreCount = analyzer.determineJavaCoupling(actual);

		assertEquals(expected.length, actual.length);

		// Convert to Lists so we can use containsAll
		List<String> expectedList = Arrays.asList(expected);
		List<String> actualList = Arrays.asList(actual);

		assertTrue(expectedList.containsAll(actualList));
		assertTrue(actualList.containsAll(expectedList));

		// Now we check the JRE counts
		assertEquals(expectedJRECount, jreCount);
	}

	@Test
	public void testInterfaceCouplingCount() {
		String[] expected = {
				"example/coupling/ZeroCouplingDataStruct",
				"java/lang/String"
		};
		int expectedJRECount = 1;
		String[] actual = analyzer.countClassCoupling("example/coupling/CouplingInterfaceExample");
		int jreCount = analyzer.determineJavaCoupling(actual);

		assertEquals(expected.length, actual.length);

		// Convert to Lists so we can use containsAll
		List<String> expectedList = Arrays.asList(expected);
		List<String> actualList = Arrays.asList(actual);

		assertTrue(expectedList.containsAll(actualList));
		assertTrue(actualList.containsAll(expectedList));

		// Now we check the JRE counts
		assertEquals(expectedJRECount, jreCount);
	}

	@Test
	public void testCoupledToInterfaceCount() {
		String[] expected = {
				"example/coupling/CouplingInterfaceExample",
				"example/coupling/ZeroCouplingDataStruct",
				"java/lang/Integer",
				"java/lang/String",
				"java/io/PrintStream",
				"java/lang/System"
		};
		int expectedJRECount = 4;
		String[] actual = analyzer.countClassCoupling("example/coupling/CoupledToInterfaceExample");
		int jreCount = analyzer.determineJavaCoupling(actual);

		// For debugging to determine if I missed a class by accident
		Arrays.sort(expected);
		Arrays.sort(actual);

		assertEquals(expected.length, actual.length);

		// Convert to Lists so we can use containsAll
		List<String> expectedList = Arrays.asList(expected);
		List<String> actualList = Arrays.asList(actual);

		assertTrue(expectedList.containsAll(actualList));
		assertTrue(actualList.containsAll(expectedList));

		// Now we check the JRE counts
		assertEquals(expectedJRECount, jreCount);
	}

	// ====================Output Testing============================ //

	@Test
	public void testReturnType() {
		analyzer.analyzeData();

		AnalyzerReturn expectedReturnType = analyzer.composeReturnType();
		List<String> linterErrorStrings = new ArrayList<>();

		for (LinterError err : expectedReturnType.errorsCaught) {
			linterErrorStrings.add(err.toString());
		}

		assertEquals(expectedReturnType.analyzerName, "High Coupling Linter");
		assertEquals(expectedReturnType.errorsCaught.size(), 7);
		LinterError linterError0 = new LinterError("example.coupling.HighCouplingStaticClassProjectCount",
				"Class has excessive coupling to project classes! (Total Coupling - 12, JRE Coupling - 3)",
				ErrType.WARNING);
		LinterError linterError1 = new LinterError("example.coupling.HighCouplingStaticClassTotalCount",
				"Class has excessive coupling to classes overall! (Total Coupling - 20, JRE Coupling - 13)",
				ErrType.WARNING);
		LinterError linterError2 = new LinterError("example.coupling.HighCouplingObjectProjectCount",
				"Class has excessive coupling to project classes! (Total Coupling - 9, JRE Coupling - 0)",
				ErrType.WARNING);
		LinterError linterError3 = new LinterError("example.coupling.HighCouplingObjectTotalCount",
				"Class has excessive coupling to classes overall! (Total Coupling - 22, JRE Coupling - 15)",
				ErrType.WARNING);
		LinterError linterError4 = new LinterError("example.coupling.HighCouplingDataStructProjectCount",
				"Class has excessive coupling to project classes! (Total Coupling - 9, JRE Coupling - 0)",
				ErrType.WARNING);
		LinterError linterError5 = new LinterError("example.coupling.HighCouplingDataStructTotalCount",
				"Class has excessive coupling to classes overall! (Total Coupling - 20, JRE Coupling - 14)",
				ErrType.WARNING);
		LinterError linterError6 = new LinterError("example.coupling.HighCouplingNightmareClass",
				"Class has excessive coupling to classes overall! (Total Coupling - 28, JRE Coupling - 21)",
				ErrType.WARNING);

		assertTrue(linterErrorStrings.contains(linterError0.toString()));
		assertTrue(linterErrorStrings.contains(linterError1.toString()));
		assertTrue(linterErrorStrings.contains(linterError2.toString()));
		assertTrue(linterErrorStrings.contains(linterError3.toString()));
		assertTrue(linterErrorStrings.contains(linterError4.toString()));
		assertTrue(linterErrorStrings.contains(linterError5.toString()));
		assertTrue(linterErrorStrings.contains(linterError6.toString()));

	}


}
