package domain;

import datasource.ASMParser;

import java.io.IOException;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

public class EqualsAndHashcodeAnalyzer extends DomainAnalyzer {

	ASMParser parser;
	public Map<String, String[]> classAndMethodNames;
	public Map<String, Set<String>> methodAndAnnotations;
	public Map<String, Map<String, Set<String>>> classMethodsAndAnnotations;

	public EqualsAndHashcodeAnalyzer(String[] classNames) {
		try {
			this.parser = new ASMParser(classNames);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void getRelevantData(String[] classList) {
		for (String className : classList) {
			String[] methodNames = parser.getMethods(className);
			Map<String, Set<String>> methodsAndAnnotations = new HashMap<>();
			for (String methodName : methodNames) {
				Set<String> methodAnnotations = parser.getMethodCompilerAnnotations(className, methodName);
				methodsAndAnnotations.put(methodName, methodAnnotations);
			}
			classMethodsAndAnnotations.put(className, methodsAndAnnotations);
		}

	}

	public void analyzeData() {

	}

	@Override
	public ReturnType composeReturnType() {
		return null;
	}

}
