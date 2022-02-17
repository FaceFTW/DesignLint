package domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import datasource.ASMParser;

public class GenericTypeNameAnalyzer extends DomainAnalyzer {
	
	private Map<String,String> classToSignature;
	private List<LinterError> namingViolations;
	
	public GenericTypeNameAnalyzer() {
		this.classToSignature = new HashMap<String,String>();
		this.namingViolations = new ArrayList<LinterError>();
	}
	
	@Override
	public void getRelevantData(String[] classList) {
		try {
			ASMParser parser = new ASMParser(classList);
			for(String className : classList) {
				className = className.replace('.', '/');
				String signature = parser.getSignature(className);
				if(signature != null) {
					this.classToSignature.put(className, signature);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void analyzeData() {
		for(String className : classToSignature.keySet()) {
			String classSignature = this.classToSignature.get(className);
			classSignature = classSignature.substring(1, classSignature.indexOf('>'));
			while(classSignature.indexOf(';') > 0) {
				String typeName = classSignature.substring(0, classSignature.indexOf(':'));
				classSignature = classSignature.substring(classSignature.indexOf(';') + 1);
				String errorMessage = "Generic Type: '" + typeName + "' ";
				if(!Character.isUpperCase(typeName.charAt(0))) {
					errorMessage += "should be capitalized.";
				} else if(typeName.length() == 2 && !Character.isDigit(typeName.charAt(1))) {
					errorMessage += "is of length 2 and starts with a capital character - second character should be a single numeric.";
				} else if(typeName.length() > 2 && (typeName.charAt(typeName.length() - 1) != 'T')) {
					errorMessage += "is of the class name form - should end in a capital 'T'";
				} else {
					continue;
				}
				namingViolations.add(new LinterError(className, errorMessage, ErrType.WARNING));
			}	
		}
	}

	@Override
	public ReturnType composeReturnType() {
		return new ReturnType("GenericTypeNameAnalyzer", namingViolations);
	}

}
