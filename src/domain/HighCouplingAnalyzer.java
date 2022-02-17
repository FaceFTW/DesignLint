package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import datasource.ASMParser;

public class HighCouplingAnalyzer extends DomainAnalyzer {

	// For all intensive purposes - Java Extensions (JavaX) and JDK will be excluded
	// JavaX is actually not included with the JRE by default since Java 11
	// JDK is for developer tool classes (such as compiler, etc.)
	public static final String JAVA_RUNTIME_CLASS_QUALIFIER_REGEX = "^java\\/.*";
	public static final String JAVA_RUNTIME_SUN_CLASS_QUALIFIER_REGEX = "^((com\\/)?sun\\/).*";

	private ASMParser parser;

	private Map<String, List<String>> classCouplingMap;

	public HighCouplingAnalyzer(ASMParser parser) {
		super();

		this.parser = parser;
		this.classCouplingMap = new HashMap<>();
	}

	@Override
	public void getRelevantData(String[] classList) {
		// This method will generally prepare the list of classes that will be parsed in
		// the map.

	}

	@Override
	public void analyzeData() {
		// TODO Auto-generated method stub

	}

	@Override
	public ReturnType composeReturnType() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Counts the number of classes that the class to anaylze depends on, therein
	 * counting coupling
	 * 
	 * @param className The name of the class to parse
	 * @return A list of all classes that are coupled to the specified class
	 */
	public String[] countClassCoupling(String className) {
		Set<String> coupledClasses = new HashSet<>();

		String[] typesFromFields = parser.getFieldTypeNames(className);
		for (String fieldType : typesFromFields) {
			coupledClasses.add(fieldType);

		}

		String[] typesFromMethodReturns = parser.getAllMethodReturnTypes(className);
		for (String methodReturnType : typesFromMethodReturns) {
			coupledClasses.add(methodReturnType);
		}

		String[] typesFromMethodParams = parser.getAllMethodParameterTypes(className);
		for (String methodParamType : typesFromMethodParams) {
			coupledClasses.add(methodParamType);
		}

		String[] typesFromMethodLocalVars = parser.getAllMethodLocalTypes(className);
		for (String methodLocalVarType : typesFromMethodLocalVars) {
			coupledClasses.add(methodLocalVarType);
		}

		String[] typesFromMethodBody = parser.getAllMethodBodyTypes(className);
		for (String methodBodyType : typesFromMethodBody) {
			coupledClasses.add(methodBodyType);
		}

		String[] typesFromInterfaceDeclaration = parser.getExtendsImplementsTypes(className);
		for (String interfaceType : typesFromInterfaceDeclaration) {
			coupledClasses.add(interfaceType);
		}

		// Remove any self instances (Classes can't really depend on themselves)
		if (coupledClasses.contains(className)) {
			coupledClasses.remove(className);
		}

		// Remove java/lang/Ojbect as all non-primitives are of type java/lang/Object
		if (coupledClasses.contains("java/lang/Object")) {
			coupledClasses.remove("java/lang/Object");
		}

		String[] result = new String[coupledClasses.size()];
		coupledClasses.toArray(result);
		return result;
	}

	/**
	 * Given a list of coupled classes, determines how many of them are from the
	 * java runtime
	 * 
	 * @param coupled A list of internal fully qualified classname strings
	 * @return The number of Java runtime classes that are in the list of coupled
	 *         classes
	 */
	public int determineJavaCoupling(String[] coupled) {
		int count = 0;

		for (String string : coupled) {
			if (string.matches(JAVA_RUNTIME_CLASS_QUALIFIER_REGEX)
					|| string.matches(JAVA_RUNTIME_SUN_CLASS_QUALIFIER_REGEX)) {
				count++;
			}
		}

		return count;
	}

}
