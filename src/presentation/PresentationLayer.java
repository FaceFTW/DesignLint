package presentation;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import datasource.ASMParser;
import domain.*;
import domain.analyzer.CodeToInterfaceAnalyzer;
import domain.analyzer.DryAnalyzer;
import domain.analyzer.EqualsAndHashcodeAnalyzer;
import domain.analyzer.ExceptionThrownAnalyzer;
import domain.analyzer.GenericTypeNameAnalyzer;
import domain.analyzer.HighCouplingAnalyzer;
import domain.analyzer.ObjectAdapterIdentifierAnalyzer;
import domain.analyzer.PrincipleOfLeastKnowledgeAnalyzer;
import domain.analyzer.SingletonAnalyzer;
import domain.analyzer.StrategyAnalyzer;
import domain.analyzer.TemplateMethodAnalyzer;
import domain.analyzer.VarNameAnalyzer;

public class PresentationLayer {
	private List<DomainAnalyzer> analyzers;
	private List<ReturnType> linterReturns;

	private String[] classList;

	public PresentationLayer() {
		this.analyzers = new ArrayList<>();
		this.linterReturns = new ArrayList<>();
		this.classList = new String[1];
	}

	public void setupAnalyzers(String[] fileList) {

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
			analyzers.add(new DryAnalyzer(parser));
			analyzers.add(new CodeToInterfaceAnalyzer(parser));
			// Pattern Detection
			analyzers.add(new SingletonAnalyzer(parser));
			analyzers.add(new ObjectAdapterIdentifierAnalyzer(parser));
			analyzers.add(new StrategyAnalyzer(parser));
			analyzers.add(new TemplateMethodAnalyzer(parser));


		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error reading from class files specified in arguments!");
			System.exit(1);
		}
	}

	public void runAnalyzers() {
		for (DomainAnalyzer domainAnalyzer : analyzers) {
			domainAnalyzer.getRelevantData(this.classList);
			domainAnalyzer.analyzeData();
			this.linterReturns.add(domainAnalyzer.composeReturnType());
		}
	}

	public void vomitOutput(PrintStream stream) {
		int returnNum = 0;
		int errNum = 0;
		int warnNum = 0;
		int patternNum = 0;
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
						errNum++; returnNum++;
						break;
					case INFO:
						errType = "Info";
						returnNum++;
						break;
					case PATTERN:
						errType = "Pattern";
						patternNum++; returnNum++;
						break;
					case WARNING:
						errType = "Warning";
						warnNum++; returnNum++;
						break;
					default:
						throw new IllegalArgumentException("Error, We somehow got an unexpected enum value!");
				}
				stream.format("Type - %s\n", errType);
				stream.format("Message - %s\n", error.message);
				stream.println();
			}

		}
		stream.println("Summary:");
		stream.println("======================================================================");
		stream.println("Errors Found : " + errNum);
		stream.println("Warnings Found: " + warnNum);
		stream.println("Total Patterns Found : " + patternNum);
		stream.println("Total Linter Findings : " + returnNum);
	}
}
