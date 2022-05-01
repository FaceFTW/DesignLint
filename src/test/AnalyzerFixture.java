import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;

import datasource.ASMParser;
import domain.DomainAnalyzer;

public abstract class AnalyzerFixture<T extends DomainAnalyzer> {

	public static final String ERROR_MSG_TYPE = "ERROR";
	public static final String PATTERN_MSG_TYPE = "PATTERN";
	public static final String WARNING_MSG_TYPE = "WARNING";
	public static final String INFO_MSG_TYPE = "INFO";


	protected ASMParser parser;
	protected T analyzer;

	protected void populateParserData(String[] testDataClasses) {
		try {
			parser = new ASMParser(testDataClasses);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	
	@BeforeEach
	protected abstract void initAnalyzerUUT();
}
