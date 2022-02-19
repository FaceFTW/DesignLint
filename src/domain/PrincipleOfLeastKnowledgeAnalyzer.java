package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import datasource.ASMParser;
import datasource.MethodCall;

public class PrincipleOfLeastKnowledgeAnalyzer extends DomainAnalyzer {
	
	private Set<String> consideredClasses;
	private Map<String, Set<Method>> classToMethods;
	private List<LinterError> demeterViolations;
	private ASMParser parser;
	
	public PrincipleOfLeastKnowledgeAnalyzer(ASMParser parser) {
		this.consideredClasses = new HashSet<String>();
		this.classToMethods = new HashMap<String, Set<Method>>();
		this.demeterViolations = new ArrayList<LinterError>();
		this.parser = parser;
	}
	
	@Override
	public void getRelevantData(String[] classList) {
		for (String className : classList) {
			this.consideredClasses.add(className.replace('.', '/'));
		}
		
		for(String className : this.consideredClasses) {
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
				for(MethodCall methodCall : method.getMethodCalls()) {
					String errorMessage = "Principle of Least Knowledge Violation in Class '" + className + "', Method '" + method.getName() + "'\n";
					if(this.consideredClasses.contains(methodCall.getInvokedClass())
					   && !methodCall.getCalledMethodName().equals("<init>")
					   && !methodCall.getInvokerName().equals("this")) {
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
									this.demeterViolations.add(new LinterError(className, method.getName(), errorMessage, ErrType.WARNING));
								}
								break;
						}
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
