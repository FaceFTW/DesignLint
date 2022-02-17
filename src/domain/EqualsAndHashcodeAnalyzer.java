package domain;

import datasource.ASMParser;

import java.io.IOException;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EqualsAndHashcodeAnalyzer extends DomainAnalyzer {

	ASMParser parser;
	public Map<String, String[]> classAndMethodNames;
	List<LinterError> errorList = new ArrayList<>();
	// public Map<String, Set<String>> methodAndAnnotations;
	// public Map<String, Map<String, Set<String>>> classMethodsAndAnnotations;

	public EqualsAndHashcodeAnalyzer(String[] classNames) {
		try {
			this.parser = new ASMParser(classNames);
			this.classAndMethodNames = new HashMap<String, String[]>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void getRelevantData(String[] classList) {
		
		for (String className : classList) {
			System.out.println(className);
			String[] methodNames = parser.getMethods(className);
			// Map<String, Set<String>> methodsAndAnnotations = new HashMap<>();
			// for (String methodName : methodNames) {
			// 	Set<String> methodAnnotations = parser.getMethodCompilerAnnotations(className, methodName);
			// 	methodsAndAnnotations.put(methodName, methodAnnotations);
			// }
			// classMethodsAndAnnotations.put(className, methodsAndAnnotations);
		classAndMethodNames.put(className, methodNames);
		}

	}

	public void analyzeData() {
		LinterError er = new LinterError("test error class name", "test error method name", "this is a test to verify what methods are in the class", ErrType.INFO);
				this.errorList.add(er);
		for (String className : classAndMethodNames.keySet()){
			String[] methodNames = classAndMethodNames.get(className);
			for(String methodName : methodNames){
				System.out.println("A method in " + className + " is " + methodName);
				LinterError err = new LinterError(className, methodName, "this is a test to verify what methods are in the class", ErrType.INFO);
				this.errorList.add(err);
			}
		}
	}

	@Override
	public ReturnType composeReturnType() {
		ReturnType type = new ReturnType("Equals And Hashcode Override Check", this.errorList);
		return type;
	}

}
