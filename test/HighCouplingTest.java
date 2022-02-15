import java.io.IOException;

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

	// =========================Compliant Classes=========================//

}
