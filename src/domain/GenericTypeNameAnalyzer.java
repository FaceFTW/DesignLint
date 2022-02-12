package domain;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import datasource.ASMParser;

public class GenericTypeNameAnalyzer extends DomainAnalyzer {
	
	private Map<String,String> subclassToSuperclass;
	private Map<String, Set<String>> classToMethods;
	private Map<String, Set<String>> methodToAnnotations;
	
	public GenericTypeNameAnalyzer() {
		this.subclassToSuperclass = new HashMap<String,String>();
		this.classToMethods = new HashMap<String, Set<String>>();
		this.methodToAnnotations = new HashMap<String, Set<String>>();
	}
	
	@Override
	public void getRelevantData(String[] classList) {
		try {
			ASMParser parser = new ASMParser(classList);
			for(String className : classList) {
				className = className.replace('.', '/');
				this.subclassToSuperclass.put(className, parser.getSuperName(className));
				Set<String> methods = new HashSet<String>();
				methods.addAll(Arrays.asList(parser.getMethods(className)));
				this.classToMethods.put(className, methods);
				for(String method : methods) {
					this.methodToAnnotations.put(method, parser.getMethodCompilerAnnotations(className, method));
				}
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
					if(superclassMethods != null) {
						if(superclassMethods.contains(method)) {
							Set<String> annotations = this.methodToAnnotations.get(method);
							System.out.println(annotations.toString());
						}
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
