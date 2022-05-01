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
import domain.message.LinterMessage;

public class PresentationLayer {
	// The following represent flags that the wrapper can pass
	// to the presentation layer to do various things.
	// We have a max of 32 different options, but shouldn't be a problem
	public static final int HELP_FLAG = 0x01; // Placeholder flag for Wrapper to use for help
	public static final int VERBOSE_FLAG = 0x01 << 1; // Shows Analyzer-specifc summary
	public static final int SUPER_VERBOSE_FLAG = 0x01 << 2; // Will Cause Errors to be displayed
	public static final int ULTRA_VERBOSE_FLAG = 0x01 << 3; // Shows All analyzer output

	// Use the upper bits for analyzer toggles;
	public static final int GENERIC_NAME_ANALYZER_FLAG = 0x01 << 20; // Enables the Generic Type Name Analyzer
	public static final int VAR_NAME_ANALYZER_FLAG = 0x01 << 21; // Enables the Variable Name Analyzer
	public static final int EXCEPTION_THROWN_ANALYZER_FLAG = 0x01 << 22; // Enables the Exception Thrown Analyzer
	public static final int EQUALS_HASHCODE_ANALYZER_FLAG = 0x01 << 23; // Enables the Equals and Hashcode Analyzer
	public static final int HIGH_COUPLING_ANALYZER_FLAG = 0x01 << 24; // Enables the High Coupling Analyzer
	public static final int PRINCIPLE_OF_LEAST_KNOWLEDGE_ANALYZER_FLAG = 0x01 << 25; // Enables the Principle of Least
																						// Knowledge Analyzer
	public static final int DRY_ANALYZER_FLAG = 0x01 << 26; // Enables the DRY (Don't Repeat Yourself) Analyzer
	public static final int CODE_TO_INTERFACE_ANALYZER_FLAG = 0x01 << 27; // Enables the Code To Interface Analyzer
	public static final int SINGLETON_ANALYZER_FLAG = 0x01 << 28; // Enables the Singleton Pattern Detector
	public static final int OBJECT_ADAPTER_ANALYZER_FLAG = 0x01 << 29; // Enables the Object Adapter Pattern Detector
	public static final int STRATEGY_ANALYZER_FLAG = 0x01 << 30; // Enables the Strategy Pattern Detector
	public static final int TEMPLATE_METHOD_ANALYZER_FLAG = 0x01 << 31; // Enables the Template Method Pattern Detector

	// Mask to enable all analyzers
	public static final int ALL_ANALYZERS = 0xFFFF << 20;

	private List<DomainAnalyzer> analyzers;
	private List<AnalyzerReturn> linterReturns;
	private String[] classList;
	private int flags;

	public PresentationLayer(int flags) {

		this.flags = flags;
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
			this.initAnalyzers(parser, flags);

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

		for (AnalyzerReturn returnType : linterReturns) {
			returnNum += returnType.getInfoCount() + returnType.getUnknownCount();
			errNum += returnType.getErrorCount();
			warnNum += returnType.getWarningCount();
			patternNum += returnType.getPatternCount();

			if ((flags & VERBOSE_FLAG) == VERBOSE_FLAG) {
				stream.format("Linter Name - %s\n", returnType.analyzerName);
				stream.println("======================================================================");
				stream.println("Errors Found : " + returnType.getErrorCount());
				stream.println("Warnings Found: " + returnType.getWarningCount());
				stream.println("Total Patterns Found : " + returnType.getPatternCount());
				stream.println("Total Linter Findings : " + returnType.getTotalCount());
				stream.println();
			}

			for (LinterMessage error : returnType.errorsCaught) {
				if (((flags & SUPER_VERBOSE_FLAG) == SUPER_VERBOSE_FLAG
						&& error.getMessageType().equals("ERROR"))
						|| (flags & ULTRA_VERBOSE_FLAG) == ULTRA_VERBOSE_FLAG) {
					stream.format("Type - %s\n", error.getMessageType());
					stream.format("Class Name - %s\n", error.className);
					stream.format("Method Name - %s\n", error.methodName);
					stream.format("Message - %s\n", error.message);
					stream.println();
				}

			}

		}

		returnNum = errNum + warnNum + patternNum;

		stream.println("Summary:");
		stream.println("======================================================================");
		stream.println("Errors Found : " + errNum);
		stream.println("Warnings Found: " + warnNum);
		stream.println("Total Patterns Found : " + patternNum);
		stream.println("Total Linter Findings : " + returnNum);
	}

	private void initAnalyzers(ASMParser parser, int flags) {
		if ((flags & GENERIC_NAME_ANALYZER_FLAG) == GENERIC_NAME_ANALYZER_FLAG) {
			analyzers.add(new GenericTypeNameAnalyzer(parser));
		}
		if ((flags & VAR_NAME_ANALYZER_FLAG) == VAR_NAME_ANALYZER_FLAG) {
			analyzers.add(new VarNameAnalyzer(parser));
		}
		if ((flags & EXCEPTION_THROWN_ANALYZER_FLAG) == EXCEPTION_THROWN_ANALYZER_FLAG) {
			analyzers.add(new ExceptionThrownAnalyzer(parser));
		}
		if ((flags & EQUALS_HASHCODE_ANALYZER_FLAG) == EQUALS_HASHCODE_ANALYZER_FLAG) {
			analyzers.add(new EqualsAndHashcodeAnalyzer(parser));
		}

		// Principle Violations
		if ((flags & HIGH_COUPLING_ANALYZER_FLAG) == HIGH_COUPLING_ANALYZER_FLAG) {
			analyzers.add(new HighCouplingAnalyzer(parser));
		}
		if ((flags & PRINCIPLE_OF_LEAST_KNOWLEDGE_ANALYZER_FLAG) == PRINCIPLE_OF_LEAST_KNOWLEDGE_ANALYZER_FLAG) {
			analyzers.add(new PrincipleOfLeastKnowledgeAnalyzer(parser));
		}
		if ((flags & DRY_ANALYZER_FLAG) == DRY_ANALYZER_FLAG) {
			analyzers.add(new DryAnalyzer(parser));
		}
		if ((flags & CODE_TO_INTERFACE_ANALYZER_FLAG) == CODE_TO_INTERFACE_ANALYZER_FLAG) {
			analyzers.add(new CodeToInterfaceAnalyzer(parser));
		}

		// Pattern Detection
		if ((flags & SINGLETON_ANALYZER_FLAG) == SINGLETON_ANALYZER_FLAG) {
			analyzers.add(new SingletonAnalyzer(parser));
		}
		if ((flags & OBJECT_ADAPTER_ANALYZER_FLAG) == OBJECT_ADAPTER_ANALYZER_FLAG) {
			analyzers.add(new ObjectAdapterIdentifierAnalyzer(parser));
		}
		if ((flags & STRATEGY_ANALYZER_FLAG) == STRATEGY_ANALYZER_FLAG) {
			analyzers.add(new StrategyAnalyzer(parser));
		}
		if ((flags & TEMPLATE_METHOD_ANALYZER_FLAG) == TEMPLATE_METHOD_ANALYZER_FLAG) {
			analyzers.add(new TemplateMethodAnalyzer(parser));
		}
	}
}
