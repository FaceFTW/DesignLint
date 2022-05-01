package domain.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import datasource.ASMParser;
import domain.DomainAnalyzer;
import domain.message.LinterMessage;
import domain.message.WarningLinterMessage;
import domain.AnalyzerReturn;

public class GenericTypeNameAnalyzer extends DomainAnalyzer {

	private Map<String, String> classToSignature;
	private List<LinterMessage> namingViolations;
	private ASMParser parser;

	public GenericTypeNameAnalyzer(ASMParser parser) {
		this.classToSignature = new HashMap<String, String>();
		this.namingViolations = new ArrayList<LinterMessage>();
		this.parser = parser;
	}

	@Override
	public void getRelevantData(String[] classList) {
		for (String className : classList) {
			className = className.replace('.', '/');
			String signature = parser.getSignatureNonEnum(className);
			if (signature != null) {
				this.classToSignature.put(className, signature);
			}
		}
	}

	@Override
	public void analyzeData() {
		for (String className : classToSignature.keySet()) {
			String classSignature = this.classToSignature.get(className);
			classSignature = classSignature.substring(1, classSignature.indexOf('>'));
			while (classSignature.indexOf(';') != -1) {
				String typeName = classSignature.substring(0, classSignature.indexOf(':'));
				classSignature = classSignature.substring(classSignature.indexOf(';') + 1);
				String errorMessage = "Generic Type: '" + typeName + "' ";
				if (!Character.isUpperCase(typeName.charAt(0))) {
					errorMessage += "should be capitalized.";
				} else if (typeName.length() == 2 && !Character.isDigit(typeName.charAt(1))) {
					errorMessage += "is of length 2 and starts with a capital character - second character should be a single numeric.";
				} else if (typeName.length() > 2 && (typeName.charAt(typeName.length() - 1) != 'T')) {
					errorMessage += "is of the class name form - should end in a capital 'T'";
				} else {
					continue;
				}
				namingViolations.add(new WarningLinterMessage(className, errorMessage));
			}
		}
	}

	@Override
	public AnalyzerReturn composeReturnType() {
		return new AnalyzerReturn("GenericTypeNameAnalyzer", namingViolations);
	}

}
