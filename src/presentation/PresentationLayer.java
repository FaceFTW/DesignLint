package presentation;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import datasource.ASMParser;
import domain.DomainAnalyzer;
import domain.EqualsAndHashcodeAnalyzer;
import domain.GenericTypeNameAnalyzer;
import domain.LinterError;
import domain.ObjectAdapterIdentifierAnalyzer;
import domain.PrincipleOfLeastKnowledgeAnalyzer;
import domain.ReturnType;
import domain.SingletonAnalyzer;
import domain.VarNameAnalyzer;
import domain.analyzer.ExceptionThrownAnalyzer;
import domain.analyzer.HighCouplingAnalyzer;

public class PresentationLayer {
	private List<DomainAnalyzer> analyzers;
	private List<ReturnType> linterReturns;

	private String[] classList;

	public PresentationLayer() {
		this.analyzers = new ArrayList<>();
		this.linterReturns = new ArrayList<>();
		this.classList = new String[1]; // Prevent null pointer bs
	}

	// caller *MUST* ensure that all strings point to valid files
	// We throw a hissyfit otherwise >:(
	public void setupAnalyzers(String[] fileList) {
		// Create an ASMParser to parse all of the files initially

		List<InputStream> fileStreams = new ArrayList<>();
		ASMParser parser = null;
		try {
			for (String path : fileList) {
				fileStreams.add(new FileInputStream(path));
			}

			InputStream[] streamList = new InputStream[fileStreams.size()];
			fileStreams.toArray(streamList);

			parser = new ASMParser(streamList);
			this.classList = parser.getParsedClassNames();

			// Add Analyzers to the list
			// Style Checks
			analyzers.add(new GenericTypeNameAnalyzer(parser));
			analyzers.add(new VarNameAnalyzer(parser));
			analyzers.add(new ExceptionThrownAnalyzer(parser));
			analyzers.add(new EqualsAndHashcodeAnalyzer(parser));
			// Principle Violations
			analyzers.add(new HighCouplingAnalyzer(parser));
			analyzers.add(new PrincipleOfLeastKnowledgeAnalyzer(parser));
			// Pattern Detection
			analyzers.add(new SingletonAnalyzer(parser));
			analyzers.add(new ObjectAdapterIdentifierAnalyzer(parser));

		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error reading from class files specified in arguments!");
			System.exit(1);
		}
	}

	// Nothing too complicated, just a couple of loops
	public void runAnalyzers() {
		for (DomainAnalyzer domainAnalyzer : analyzers) {
			domainAnalyzer.getRelevantData(this.classList);
			domainAnalyzer.analyzeData();
			this.linterReturns.add(domainAnalyzer.composeReturnType());
		}
	}

	// Technically, this allows for some degree of flexibility
	// This way you can change the actual LinterMain to output to a file if you
	// wanted
	public void vomitOutput(PrintStream stream) {
		for (ReturnType returnType : linterReturns) {
			stream.format("Linter Name - %s\n", returnType.analyzerName);
			stream.println("======================================================================");
			for (LinterError error : returnType.errorsCaught) {
				stream.format("Class Name - %s\n", error.className);
				stream.format("Method Name - %s\n", error.methodName);

				String errType = "";
				switch (error.type) {
					case ERROR:
						errType = "Error";
						break;
					case INFO:
						errType = "Info";
						break;
					case PATTERN:
						errType = "Pattern";
						break;
					case WARNING:
						errType = "Warning";
						break;
					default:
						throw new IllegalArgumentException("Error, We somehow got an unexpected enum value!");
				}
				stream.format("Type - %s\n", errType);
				stream.format("Message - %s\n", error.message);
				stream.println();
			}
		}
	}

}
