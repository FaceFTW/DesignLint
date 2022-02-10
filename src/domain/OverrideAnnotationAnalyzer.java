package domain;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import datasource.ASMParser;

public class OverrideAnnotationAnalyzer extends DomainAnalyzer {
	
	private Map<String,String> subclassToSuperclass;
	private Map<String, Set<String>> classToMethods;
	private Map<String, Set<String>> methodToAnnotations;
	
	public OverrideAnnotationAnalyzer() {
		this.subclassToSuperclass = new HashMap<String,String>();
		this.classToMethods = new HashMap<String, Set<String>>();
		this.methodToAnnotations = new HashMap<String, Set<String>>();
	}
	
	@Override
	public void getRelevantData(String[] classList) {
		try {
			ASMParser parser = new ASMParser(classList);
			for(String className : classList) {
				this.subclassToSuperclass.put(className, parser.getSuperName(className));
				Set<String> methods = new HashSet<String>();
				methods.addAll(Arrays.asList(parser.getMethods(className)));
				this.classToMethods.put(className, methods);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void analyzeData() {
		for(String className : this.subclassToSuperclass.keySet()) {
			String superclass = this.subclassToSuperclass.get(className);
			Set<String> methods = this.classToMethods.get(className);
			Set<String> superclassMethods = this.classToMethods.get(superclass);
			if(methods != null) {
				for(String method : methods) {
					if(superclassMethods.contains(method)) {
						//if()
					}
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
