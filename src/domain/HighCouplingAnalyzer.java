package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import datasource.ASMParser;

public class HighCouplingAnalyzer extends DomainAnalyzer {

	// For all intensive purposes - Java Extensions (JavaX) and JDK will be excluded
	// JavaX is actually not included with the JRE by default since Java 11
	// JDK is for developer tool classes (such as compiler, etc.)
	public static final String JAVA_RUNTIME_CLASS_QUALIFIER_REGEX = "^java\\.";
	public static final String JAVA_RUNTIME_SUN_CLASS_QUALIFIER_REGEX = "^((com\\.)?sun\\.)";

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
	public List<String> countClassCoupling(String className) {
		List<String> coupledClasses = new ArrayList<>();

		return coupledClasses;
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
