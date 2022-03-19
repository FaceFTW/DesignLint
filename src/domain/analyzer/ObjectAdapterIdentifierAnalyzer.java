package domain.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import datasource.ASMParser;
import datasource.Invoker;
import datasource.MethodCall;
import domain.DomainAnalyzer;
import domain.ErrType;
import domain.LinterError;
import domain.Method;
import domain.ReturnType;

public class ObjectAdapterIdentifierAnalyzer extends DomainAnalyzer {

	private ASMParser parser;
	private List<LinterError> adapterIdentifications;
	private Map<String, Set<String>> classToPotentialTargets;
	private Map<String, Set<Method>> classToMethods;
	private Set<String> consideredClasses;

	public ObjectAdapterIdentifierAnalyzer(ASMParser parser) {
		this.parser = parser;
		this.adapterIdentifications = new ArrayList<LinterError>();
		this.classToPotentialTargets = new HashMap<String, Set<String>>();
		this.classToMethods = new HashMap<String, Set<Method>>();
		this.consideredClasses = new HashSet<String>();
	}

	@Override
	public void getRelevantData(String[] classList) {

		for (String className : classList) {
			this.consideredClasses.add(className.replace('.', '/'));
		}

		for (String className : this.consideredClasses) {

			Set<Method> methods = new HashSet<Method>();
			String[] methodArr = this.parser.getMethods(className);
			for (int i = 0; i < methodArr.length; i++) {
				List<MethodCall> methodCalls = this.parser.getMethodCalls(className, methodArr[i]);
				Method method = new Method(methodArr[i], methodCalls);
				methods.add(method);
			}
			this.classToMethods.put(className, methods);

			String[] classImplements = this.parser.getInterfaces(className);
			String superType = this.parser.getSuperName(className);
			Set<String> potentialTargets = new HashSet<String>();
			if (this.consideredClasses.contains(superType)) {
				potentialTargets.add(superType);
			}
			for (String interfaceName : classImplements) {
				if (this.consideredClasses.contains(interfaceName)) {
					potentialTargets.add(interfaceName);
				}
			}
			this.classToPotentialTargets.put(className, potentialTargets);
		}
	}

	@Override
	public void analyzeData() {
		for (String className : this.classToPotentialTargets.keySet()) {
			targets: for (String potentialTarget : this.classToPotentialTargets.get(className)) {
				Set<String> interfaceMethodNames = new HashSet<String>();
				for (Method interfaceMethod : this.classToMethods.get(potentialTarget)) {
					interfaceMethodNames.add(interfaceMethod.getName());
				}
				for (Method method : this.classToMethods.get(className)) {
					if (interfaceMethodNames.contains(method.getName())) {
						for (MethodCall methodCall : method.getMethodCalls()) {
							if (methodCall.getInvoker() == Invoker.FIELD
									&& this.consideredClasses.contains(methodCall.getInvokedClass())) {
								String result = "Object Adapter Pattern Recognized:\n";
								result += "Target: " + potentialTarget.replace('/', '.') + "\n";
								result += "Adaptee: " + methodCall.getInvokedClass().replace('/', '.') + "\n";
								result += "Adapter: " + className.replace('/', '.') + "\n";
								this.adapterIdentifications.add(new LinterError(className, result, ErrType.PATTERN));
								continue targets;
							}
						}
					}
				}
			}
		}
	}

	@Override
	public ReturnType composeReturnType() {
		return new ReturnType("ObjectAdapterIdentifierAnalyzer", this.adapterIdentifications);
	}

}
