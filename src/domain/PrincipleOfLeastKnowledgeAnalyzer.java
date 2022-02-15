package domain;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import datasource.ASMParser;

public class PrincipleOfLeastKnowledgeAnalyzer extends DomainAnalyzer {

	private Map<String,Set<String>> classToMethods;
	private Map<String,Map<String,String>> methodToCalls;
	
	public PrincipleOfLeastKnowledgeAnalyzer() {
		this.classToMethods = new HashMap<String,Set<String>>();
		this.methodToCalls = new HashMap<String, Map<String,String>>();
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
				}
				this.classToMethods.put(className, methods);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void analyzeData() {
		System.out.println("");
	}

	@Override
	public ReturnType composeReturnType() {
		// TODO Auto-generated method stub
		return null;
	}

}
