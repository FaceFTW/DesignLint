package domain.analyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import datasource.ASMParser;
import domain.AnalyzerReturn;
import domain.DomainAnalyzer;
import domain.message.LinterMessage;
import domain.message.PatternLinterMessage;

public class SingletonAnalyzer extends DomainAnalyzer {

	private ASMParser parser;
	private List<LinterMessage> errors;
	private Map<String, String[]> validFields;
	private Map<String, String[]> staticMethods;
	private String[] classList;

	public SingletonAnalyzer(String[] classNames) {

		try {
			this.parser = new ASMParser(classNames);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.errors = new ArrayList<>();
		this.validFields = new HashMap<>();
		this.staticMethods = new HashMap<>();
	}

	public SingletonAnalyzer(ASMParser parser) {
		this.parser = parser;
		this.errors = new ArrayList<>();
		this.validFields = new HashMap<>();
		this.staticMethods = new HashMap<>();
	}

	@Override
	public void getRelevantData(String[] classList) {
		this.classList = classList;
		for (String className : classList) {
			String[] classFields = this.parser.getClassStaticPrivateFieldNames(className);
			validFields.put(className, classFields);
		}
		for (String className : classList) {
			String[] classMethods = this.parser.getStaticMethods(className);
			staticMethods.put(className, classMethods);
		}
	}

	@Override
	public void analyzeData() {
		for (String className : classList) {
			boolean hasStaticField = analyzeFields(className);
			boolean hasPriCon = parser.isClassConstructorPrivate(className);
			boolean hasStaticMethod = analyzeMethods(className);
			if (hasPriCon && hasStaticField && hasStaticMethod) {
				LinterMessage err = new PatternLinterMessage(className, "Singleton Pattern detected!");
				errors.add(err);
			}
		}
	}

	@Override
	public AnalyzerReturn composeReturnType() {
		AnalyzerReturn toReturn = new AnalyzerReturn("SingletonAnalyzer", this.errors);
		return toReturn;
	}

	private boolean analyzeFields(String className) {
		return (validFields.get(className).length > 0);
	}

	private boolean analyzeMethods(String className) {
		for (String method : this.staticMethods.get(className)) {
			if (method.equals("getInstance")) {
				return true;
			}
		}
		return false;
	}

}
