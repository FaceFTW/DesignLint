package domain.analyzer;

import datasource.ASMParser;
import datasource.Invoker;
import datasource.MethodCall;
import domain.DomainAnalyzer;
import domain.message.ErrorLinterMessage;
import domain.message.LinterMessage;
import domain.message.WarningLinterMessage;
import domain.AnalyzerReturn;

import java.util.*;

public class CodeToInterfaceAnalyzer extends DomainAnalyzer {

	private ASMParser parser;

	// Parsed Data
	public Map<String, List<String>> fieldNames;
	public Map<String, List<String>> fieldTypes;
	public Map<String, Map<String, List<String>>> methodVarNames;
	public Map<String, Map<String, List<String>>> methodVarTypes;
	public Map<String, Map<String, Map<String, Map<String, List<String>>>>> possibleInterfaces;

	// Erroneous Values
	List<LinterMessage> foundErrors;

	public CodeToInterfaceAnalyzer(ASMParser parser) {
		this.parser = parser;
		this.fieldNames = new HashMap<>();
		this.fieldTypes = new HashMap<>();
		this.methodVarNames = new HashMap<>();
		this.methodVarTypes = new HashMap<>();
		this.possibleInterfaces = new HashMap<>();
		this.foundErrors = new ArrayList<>();
	}

	public void getRelevantData(String[] classList) {
		for (String className : classList) {
			this.fieldNames.put(className, parser.getClassFieldNames(className));
			this.fieldTypes.put(className, parser.getClassFieldTypes(className));
			this.methodVarNames.put(className, parser.findCorrectMethodInfo(className, true));
			this.methodVarTypes.put(className, parser.findCorrectMethodInfo(className, false));
		}

		for (String className : this.methodVarNames.keySet()) {
			for (String methodName : this.methodVarNames.get(className).keySet()) {
				checkMethodSignature(className, methodName);
			}
		}
	}

	public void analyzeData() {
		this.foundErrors = new ArrayList<>();
		findFieldShortCut();
		for (String className : this.possibleInterfaces.keySet()) {
			for (String methodName : this.possibleInterfaces.get(className).keySet()) {
				for (String varName : this.possibleInterfaces.get(className).get(methodName).keySet()) {

					analyzePotentialInterfaces(className, methodName, varName);
				}
			}
		}

	}

	public AnalyzerReturn composeReturnType() {
		return new AnalyzerReturn("CodeToInterfaceAnalyzer", this.foundErrors);
	}

	public void findFieldShortCut() {
		for (String className : this.fieldTypes.keySet()) {
			for (int i = 0; i < this.fieldTypes.get(className).size(); i++) {
				String violation = "";

				if (fieldTypes.get(className).get(i).compareTo("Ljava/util/ArrayList;") == 0) {
					violation = " should be of type List<>, not ArrayList<>";
				} else if (fieldTypes.get(className).get(i).compareTo("Ljava/util/HashMap;") == 0) {
					violation = " should be of type Map<>, not HashMap<>";
				} else if (fieldTypes.get(className).get(i).compareTo("Ljava/util/HashSet;") == 0) {
					violation = " should be of type Set<>, not HashSet<>";
				}

				if (!violation.isEmpty()) {
					this.foundErrors.add(new ErrorLinterMessage(className, null,
							"Field " + this.fieldNames.get(className).get(i)
									+ violation));
				}

			}
		}
	}

	public void analyzePotentialInterfaces(String className, String methodName, String varName) {
		if (!findShortCut(className, methodName, varName)) {
			Map<String, List<String>> interfaces = this.possibleInterfaces.get(className).get(methodName).get(varName);
			List<String> union = new ArrayList<>();
			boolean x = false;
			union.add("Init List");
			for (String methodKey : interfaces.keySet()) {
				if (union.get(0).compareTo("Init List") == 0) {
					union = interfaces.get(methodKey);
				}

				if (interfaces.get(methodKey).contains("X"))
					x = true;

				if (!x) {
					union = intersectLists(union, interfaces.get(methodKey));
				}
			}
			if (!x && union.get(0).compareTo("<init>") != 0) {
				for (int i = 0; i < union.size(); i++) {
					// int index =
					// this.methodVarNames.get(className).get(methodName).indexOf(varName);
					this.foundErrors.add(new WarningLinterMessage(className, methodName,
							"Potential Interface for " + varName + ": " + union.get(i)));
				}
			}
		}
	}

	public List<String> intersectLists(List<String> l1, List<String> l2) {
		List<String> list = new ArrayList<>();

		for (String s : l1) {
			if (l2.contains(s)) {
				list.add(s);
			}
		}

		List<String> nonDupedList = new ArrayList<>();
		for (String s : list) {
			if (!nonDupedList.contains(s)) {
				nonDupedList.add(s);
			}
		}
		return nonDupedList;
	}

	public boolean findShortCut(String className, String methodName, String varName) {
		if (!this.methodVarNames.get(className).get(methodName).contains(varName)) {
			return false;
		}
		int index = this.methodVarNames.get(className).get(methodName).indexOf(varName);
		String type = this.methodVarTypes.get(className).get(methodName).get(index);
		String violation = "";

		if (type.compareTo("Ljava/util/ArrayList;") == 0) {
			violation = " should be of type List<>, not ArrayList<>";
		} else if (type.compareTo("Ljava/util/HashMap;") == 0) {
			violation = " should be of type Map<>, not HashMap<>";
		} else if (type.compareTo("Ljava/util/HashSet;") == 0) {
			violation = " should be of type Set<>, not HashSet<>";
		}

		if (!violation.isEmpty()) {
			this.foundErrors.add(new ErrorLinterMessage(className, methodName,
					"Local Variable " + varName + violation));
			return true;
		}
		return false;
	}

	public void checkMethodSignature(String className, String methodName) {
		List<MethodCall> methodCalls = this.parser.getMethodCalls(className, methodName);

		for (MethodCall method : methodCalls) {
			if (method.getInvoker() == Invoker.FIELD) {
				continue;
			}
			List<String> interfaces = parser.getInterfacesList(method.getInvokedClass());
			if (interfaces == null) {
				continue;
			} else if (interfaces.size() > 0) {
				boolean foundInInterface = false;
				for (String interf : interfaces) {
					boolean foundinCurrentInterface = false;
					if (parser.compareMethodFromInterface(method.getInvokedClass(), method.getCalledMethodName(),
							interf)) {
						foundInInterface = true;
						foundinCurrentInterface = true;
					}
					// Put into our possible interface map.
					// Class
					if (!this.possibleInterfaces.keySet().contains(className)) {
						this.possibleInterfaces.put(className, new HashMap<>());
					}
					// Method
					if (!this.possibleInterfaces.get(className).keySet().contains(methodName)) {
						this.possibleInterfaces.get(className).put(methodName, new HashMap<>());
					}
					// VariableName
					if (!this.possibleInterfaces.get(className).get(methodName).keySet()
							.contains(method.getInvokerName())) {
						this.possibleInterfaces.get(className).get(methodName).put(method.getInvokerName(),
								new HashMap<>());
					}
					// MethodCalled on Variable
					if (!this.possibleInterfaces.get(className).get(methodName).get(method.getInvokerName()).keySet()
							.contains(method.getCalledMethodName())) {
						this.possibleInterfaces.get(className).get(methodName).get(method.getInvokerName())
								.put(method.getCalledMethodName(), new ArrayList<>());
					}
					// Input into our Map
					if (foundinCurrentInterface) {

						this.possibleInterfaces.get(className).get(methodName).get(method.getInvokerName())
								.get(method.getCalledMethodName()).add(interf);
					}
				}
				if (!foundInInterface) {
					this.possibleInterfaces.get(className).get(methodName).get(method.getInvokerName())
							.get(method.getCalledMethodName()).add("X");
				}
			}
		}
	}

	public Map<String, List<String>> getFieldNames() {
		return this.fieldNames;
	}

	public Map<String, List<String>> getFieldTypes() {
		return this.fieldTypes;
	}

	public Map<String, Map<String, List<String>>> getMethodVarNames() {
		return this.methodVarNames;
	}

	public Map<String, Map<String, List<String>>> getMethodVarTypes() {
		return this.methodVarTypes;
	}

	public Map<String, Map<String, Map<String, Map<String, List<String>>>>> getPossibleInterfaces() {
		return this.possibleInterfaces;
	}
}