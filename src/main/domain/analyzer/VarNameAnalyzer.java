package domain.analyzer;

import datasource.ASMParser;
import domain.DomainAnalyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import domain.ErrType;
import domain.LinterError;
import domain.ReturnType;

public class VarNameAnalyzer extends DomainAnalyzer {

	private ASMParser parser;
	// Parsed Values
	private Map<String, List<String>> fieldNames;
	private Map<String, List<String>> globalNames;
	private Map<String, Map<String, List<String>>> methodNames;

	// Erroneous Values
	List<LinterError> foundErrors;

	public VarNameAnalyzer(ASMParser parser) {
		this.parser = parser;
		this.fieldNames = new HashMap<String, List<String>>();
		this.globalNames = new HashMap<String, List<String>>();
		this.methodNames = new HashMap<String, Map<String, List<String>>>();
		this.foundErrors = null;
	}

	public void getRelevantData(String[] classList) {
		for (String className : classList) {
			this.fieldNames.put(className, parser.getClassFieldNames(className));
			this.methodNames.put(className, parser.findCorrectMethodInfo(className, true));
			this.globalNames.put(className, parser.getGlobalNames(className));
		}
	}

	public void analyzeData() {
		this.foundErrors = new ArrayList<>();

		for (String classKey : this.fieldNames.keySet()) {
			analyzeGeneralErrors(this.fieldNames.get(classKey), classKey, null);
			analyzeFieldNames(this.fieldNames.get(classKey), classKey);
		}
		for (String classKey : this.globalNames.keySet()) {
			analyzeGeneralErrors(this.globalNames.get(classKey), classKey, null);
			analyzeGlobalNames(this.globalNames.get(classKey), classKey);
		}
		for (String classKey : this.methodNames.keySet()) {
			for (String methodKey : this.methodNames.get(classKey).keySet()) {
				analyzeGeneralErrors(this.methodNames.get(classKey).get(methodKey), classKey, methodKey);
				analyzeMethodNames(this.methodNames.get(classKey).get(methodKey), classKey, methodKey);
			}
		}
	}

	public ReturnType composeReturnType() {
		return new ReturnType("VarNameAnalyzer", this.foundErrors);
	}

	public void analyzeGeneralErrors(List<String> varNames, String className, String methodName) {
		for (String var : varNames) {
			if (var.charAt(0) == '_') {
				this.foundErrors.add(new LinterError(className, methodName, var + " begins with _", ErrType.ERROR));
			}

			if (var.charAt(0) == '$') {
				this.foundErrors.add(new LinterError(className, methodName, var + " begins with $", ErrType.ERROR));
			}
		}
	}

	public void analyzeFieldNames(List<String> varNames, String className) {
		for (String var : varNames) {
			if (Character.isLetter(var.charAt(0)) &&
					var.charAt(0) != Character.toLowerCase(var.charAt(0))) {
				this.foundErrors
						.add(new LinterError(className, null, "Field " + var + " begins with capital letter",
								ErrType.ERROR));
			}

			if (var.length() > 30) {
				this.foundErrors
						.add(new LinterError(className, null, "Field " + var + " too long (>30 characters)",
								ErrType.WARNING));
			}

			if (var.length() <= 2) {
				this.foundErrors
						.add(new LinterError(className, null, "Field " + var + " too short (<=2 characters)",
								ErrType.WARNING));
			}
		}

	}

	public void analyzeGlobalNames(List<String> varNames, String className) {
		for (String var : varNames) {
			if (var.toUpperCase().compareTo(var) != 0) {
				this.foundErrors
						.add(new LinterError(className, null,
								"Global Variable " + var + " must only be capital letters", ErrType.ERROR));
			}

			if (var.length() <= 2) {
				this.foundErrors
						.add(new LinterError(className, null, "Global Variable " + var + " too short (<=2 characters)",
								ErrType.WARNING));
			}
		}
	}

	public void analyzeMethodNames(List<String> varNames, String className, String methodName) {
		for (String var : varNames) {
			if (Character.isLetter(var.charAt(0)) &&
					var.charAt(0) != Character.toLowerCase(var.charAt(0))) {
				this.foundErrors.add(
						new LinterError(className, methodName, "Local Variable " + var + " begins with capital letter",
								ErrType.ERROR));
			}

			if (var.length() > 30) {
				this.foundErrors.add(
						new LinterError(className, methodName, "Local Variable " + var + " too long (>30 characters)",
								ErrType.WARNING));
			}
		}
	}

	public Map<String, List<String>> getFieldNames() {
		return this.fieldNames;
	}

	public Map<String, List<String>> getGlobalNames() {
		return this.globalNames;
	}

	public Map<String, Map<String, List<String>>> getMethodNames() {
		return this.methodNames;
	}
}
