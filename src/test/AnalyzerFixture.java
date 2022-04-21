import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;

import datasource.ASMParser;
import domain.DomainAnalyzer;

public abstract class AnalyzerFixture<T extends DomainAnalyzer> {

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
