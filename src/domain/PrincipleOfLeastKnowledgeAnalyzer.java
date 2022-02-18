package domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import datasource.ASMParser;
import datasource.MethodCall;

public class PrincipleOfLeastKnowledgeAnalyzer extends DomainAnalyzer {

	private Map<String, Set<Method>> classToMethods;
	private List<LinterError> demeterViolations;
	private ASMParser parser;
	
	private class Method {
		String name;
		List<MethodCall> methodCalls;
		
		public Method(String name, List<MethodCall> methodCalls) {
			this.name = name;
			this.methodCalls = methodCalls;
		}
	}
	
	public PrincipleOfLeastKnowledgeAnalyzer(ASMParser parser) {
		this.classToMethods = new HashMap<String, Set<Method>>();
		this.demeterViolations = new ArrayList<LinterError>();
		this.parser = parser;
	}
	
	@Override
	public void getRelevantData(String[] classList) {
		for(String className : classList) {
			className = className.replace('.', '/');
			Set<Method> methods = new HashSet<Method>();
			String [] methodArr = parser.getMethods(className);
			for(int i = 0; i < methodArr.length; i++) {
				List<MethodCall> methodCalls = parser.getMethodCalls(className, methodArr[i]);
				Method method = new Method(methodArr[i], methodCalls);
				methods.add(method);
			}
			this.classToMethods.put(className, methods);
		}
		
	}

	@Override
	public void analyzeData() {
		for(String className : this.classToMethods.keySet()) {
			for(Method method : this.classToMethods.get(className)) {
				for(MethodCall methodCall : method.methodCalls) {
					String errorMessage = "Principle of Least Knowledge Violation in Class '" + className + "', Method '" + method.name + "'\n";
					switch(methodCall.getInvoker()) {
						case FIELD:
							break;
						
						case PARAMETER:
							break;
							
						case CONSTRUCTED:
							break;
						
						case RETURNED:
							if(!methodCall.getInvokedClass().equals(className)) {
								errorMessage += "Reached method '" + methodCall.getCalledMethodName() + "' with an illegal access.";
								this.demeterViolations.add(new LinterError(className, method.name, errorMessage, ErrType.WARNING));
							}
							break;
					}
				}
			}
		}
	}

	@Override
	public ReturnType composeReturnType() {
		return new ReturnType("PrincipleOfLeastKnowledgeAnalyzer", this.demeterViolations);
	}
	
}
