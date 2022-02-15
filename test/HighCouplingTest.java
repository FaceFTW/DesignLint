import java.io.IOException;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import datasource.ASMParser;
import domain.ExceptionThrownAnalyzer;

public class HighCouplingTest {

	private ASMParser parser;
	private final String[] exampleClasses = { "example.exceptionstyle.ExceptionStyleExamples" };

	// We use an explicit instance to test the protected method checkViolation()
	private ExceptionThrownAnalyzer analyzer;

	// Common Testing Setup
	public void setupAnalyzer() {
		try {
			this.parser = new ASMParser(exampleClasses);
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.analyzer = new ExceptionThrownAnalyzer(parser);
		analyzer.getRelevantData(exampleClasses);
	}

	// =====================Compliant Classes===================== //

	@Category({ HighCouplingTest.class, HighCouplingCompliantTest.class })
	@Test
	public void testZeroCouplingDataStruct() {

	}

	@Category({ HighCouplingTest.class, HighCouplingCompliantTest.class })
	@Test
	public void testZeroCouplingObject() {

	}

	@Category({ HighCouplingTest.class, HighCouplingCompliantTest.class })
	@Test
	public void testZeroCouplingStaticClass() {

	}

	@Category({ HighCouplingTest.class, HighCouplingCompliantTest.class })
	@Test
	public void testLowCouplingObject() {

	}

	@Category({ HighCouplingTest.class, HighCouplingCompliantTest.class })
	@Test
	public void testLowCouplingDataStruct() {

	}

	@Category({ HighCouplingTest.class, HighCouplingCompliantTest.class })
	@Test
	public void testLowCouplingDataStruct2() {

	}

	@Category({ HighCouplingTest.class, HighCouplingCompliantTest.class })
	@Test
	public void testLowCouplingStaticClass() {

	}

	// ===================Non-Compliant Classes=================== //

	@Category({ HighCouplingTest.class, HighCouplingNonCompliantTest.class })
	@Test
	public void testHighCouplingDataStructTotalCount() {
	}

	@Category({ HighCouplingTest.class, HighCouplingNonCompliantTest.class })
	@Test
	public void testHighCouplingDataStructProjectCount() {

	}

	@Category({ HighCouplingTest.class, HighCouplingNonCompliantTest.class })
	@Test
	public void testHighCouplingObjectTotalCount() {

	}

	@Category({ HighCouplingTest.class, HighCouplingNonCompliantTest.class })
	@Test
	public void testHighCouplingObjectProjectCount() {

	}

	@Category({ HighCouplingTest.class, HighCouplingNonCompliantTest.class })
	@Test
	public void testHighCouplingStaticClassTotalCount() {

	}

	@Category({ HighCouplingTest.class, HighCouplingNonCompliantTest.class })
	@Test
	public void testHighCouplingStaticClassProjectCount() {

	}

	// ====================Output Testing============================ //

	@Category({ HighCouplingTest.class })
	@Test
	public void testReturnType() {

	}

	// ==================Test Category Interfaces==================== //
	public interface HighCouplingTests {
	};

	public interface HighCouplingCompliantTest {
	};

	public interface HighCouplingNonCompliantTest {
	};

}
