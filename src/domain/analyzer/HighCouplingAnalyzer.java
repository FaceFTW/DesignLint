package domain.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import datasource.ASMParser;
import domain.DomainAnalyzer;
import domain.ErrType;
import domain.LinterError;
import domain.ReturnType;

public class HighCouplingAnalyzer extends DomainAnalyzer {

	public static final String JAVA_RUNTIME_CLASS_QUALIFIER_REGEX = "^java\\/.*";
	public static final String JAVA_RUNTIME_SUN_CLASS_QUALIFIER_REGEX = "^((com\\/)?sun\\/).*";

	public static final String LINTER_ERROR_PROJECT_FORMAT_STRING = "Class has excessive coupling to project classes! (Total Coupling - %d, JRE Coupling - %d)";
	public static final String LINTER_ERROR_TOTAL_FORMAT_STRING = "Class has excessive coupling to classes overall! (Total Coupling - %d, JRE Coupling - %d)";

	private ASMParser parser;

	private Map<String, String[]> classCouplingMap;

	private List<LinterError> foundErrors;

	public HighCouplingAnalyzer(ASMParser parser) {
		super();

		this.parser = parser;
		this.classCouplingMap = new HashMap<>();
		this.foundErrors = new ArrayList<>();
	}

	@Override
	public void getRelevantData(String[] classList) {
		for (String className : classList) {
			this.classCouplingMap.put(className, this.countClassCoupling(className));
		}

	}

	@Override
	public void analyzeData() {
		for (String className : this.classCouplingMap.keySet()) {
			String[] coupled = this.countClassCoupling(className);
			int jreDepCount = this.determineJavaCoupling(coupled);

			String errString = "";
			if (coupled.length >= 20) {
				errString = String.format(LINTER_ERROR_TOTAL_FORMAT_STRING, coupled.length, jreDepCount);
			} else if ((coupled.length - jreDepCount) >= 9) {
				errString = String.format(LINTER_ERROR_PROJECT_FORMAT_STRING, coupled.length, jreDepCount);
			}

			if (errString.length() > 0) {
				foundErrors.add(new LinterError(className.replace("/", "."), errString, ErrType.WARNING));
			}
		}
	}

	@Override
	public ReturnType composeReturnType() {
		return new ReturnType("High Coupling Linter", foundErrors);
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

		if (coupledClasses.contains(className)) {
			coupledClasses.remove(className);
		}

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
