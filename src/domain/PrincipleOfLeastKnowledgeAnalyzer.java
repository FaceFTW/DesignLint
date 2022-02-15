package domain;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import datasource.ASMParser;

public class PrincipleOfLeastKnowledgeAnalyzer extends DomainAnalyzer {

	private Map<String, Set<String>> classToMethods;
	private Map<String, Set<String>> classToFields;
	private Map<String, Map<String, String>> methodToCalls;
	private Map<String, Set<String>> methodToParameters;
	private Map<String, String> fieldToType;
	
	public PrincipleOfLeastKnowledgeAnalyzer() {
		this.classToMethods = new HashMap<String, Set<String>>();
		this.classToFields = new HashMap<String, Set<String>>();
		this.methodToCalls = new HashMap<String, Map<String,String>>();
		this.methodToParameters = new HashMap<String, Set<String>>();
		this.fieldToType = new HashMap<String, String>();
	}
	
	@Override
	public void getRelevantData(String[] classList) {
		try {
			ASMParser parser = new ASMParser(classList);
			for(String className : classList) {
				className = className.replace('.', '/');
				Set<String> methods = new HashSet<String>();
				String [] methodArr = parser.getMethods(className);
				for(int i = 0; i < methodArr.length; i++) {
					methods.add(methodArr[i]);
					this.methodToCalls.put(methodArr[i], parser.getMethodCalls(className, methodArr[i]));
					Set<String> parameters = parser.getMethodParameters(className, methodArr[i]);
					this.methodToParameters.put(methodArr[i], parameters);
				}
				this.classToMethods.put(className, methods);
				Set<String> fields = new HashSet<String>();
				fields.addAll(parser.getClassFieldNames(className));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void analyzeData() {
		for(String className : this.classToMethods.keySet()) {
			Set<String> methods = this.classToMethods.get(className);
			for(String method : methods) {
				Map<String,String> calls = this.methodToCalls.get(method);
				for(String calledMethodName : calls.keySet()) {
					String calledMethodOwner = this.methodToCalls.get(method).get(calledMethodName);
					// Can always call your own methods
					if(this.classToMethods.get(className).contains(calledMethodName)) {
						continue;
						
					// Can call methods of your own fields
					} else if(this.classToFields.get(className).contains(calledMethodOwner)) {
						continue;
					}
					// Can call methods of method parameters
					//} else if(this.methodToParameters.get(method)
					//			  .contains(this.methodToCalls))
				}
			}
		}
	}

	@Override
	public ReturnType composeReturnType() {
		// TODO Auto-generated method stub
		return null;
	}

}
