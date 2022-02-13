package domain;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import datasource.ASMParser;

public class ExceptionThrownAnalyzer extends DomainAnalyzer {
	public static final String JAVA_EXCEPTION_INTERNAL_CLASS = "java/lang/Exception";
	public static final String JAVA_RUNTIMEEXCEPTION_INTERNAL_CLASS = "java/lang/RuntimeException";
	public static final String JAVA_ERROR_INTERNAL_CLASS = "java/lang/Error";
	public static final String JAVA_THROWABLE_INTERNAL_CLASS = "java/lang/Throwable";

	// Format String parameters: Fully Qualified Class name (User friendly), Method
	// Name, Error String
	public static final String LINTER_ERROR_FORMAT_STRING = "%s.%s() %s";

	private ASMParser parser;
	private Map<String, String[]> classMethodMap;
	private Map<Entry<String, String>, String[]> methodExceptionThrownMap;
	private Map<Entry<String, String>, String[]> methodExceptionCatchMap;

	private Map<Entry<String, String>, List<ExceptionLinterIssue>> methodIssueMap;

	public ExceptionThrownAnalyzer(ASMParser parser) {
		super();

		this.parser = parser;
		this.classMethodMap = new HashMap<>();
		this.methodExceptionThrownMap = new HashMap<>();
		this.methodExceptionCatchMap = new HashMap<>();

		this.methodIssueMap = new HashMap<>();
	}

	@Override
	public void getRelevantData(String[] classList) {
		// First get all the methods in each class
		for (String className : classList) {
			className = className.replace('.', '/');
			String[] classMethods = parser.getMethods(className);
			classMethodMap.put(className, classMethods);
		}

		// Now we make entries for all every class-method pair, then get
		// Exceptions thrown and caught

		for (String className : classMethodMap.keySet()) {
			for (String methodName : classMethodMap.get(className)) {
				Entry<String, String> classMethodPair = new AbstractMap.SimpleEntry<>(className, methodName);

				// Get the Thrown Exceptions
				String[] thrownExceptions = this.parser.getMethodExceptionSignature(className, methodName);
				this.methodExceptionThrownMap.put(classMethodPair, thrownExceptions);

				String[] caughtExceptions = this.parser.getMethodExceptionCaught(className, methodName);
				this.methodExceptionCatchMap.put(classMethodPair, caughtExceptions);
			}
		}

		// After we get all of the data, prepare the result array for analysis
		this.populateMethodIssueMapDefaults();
	}

	@Override
	public void analyzeData() {
		// Just call checkMethodCompliance repeatedly, replace the lists in the map as
		// we go
		for (Entry<String, String> classMethodPair : methodIssueMap.keySet()) {
			List<ExceptionLinterIssue> methodIssues = this.checkMethodCompliance(classMethodPair.getKey(),
					classMethodPair.getValue());

			this.methodIssueMap.put(classMethodPair, methodIssues);
		}

	}

	@Override
	public ReturnType composeReturnType() {
		// Make the list of linter errors first
		List<LinterError> errorList = new ArrayList<>();
		for (Entry<Entry<String, String>, List<ExceptionLinterIssue>> linterError : methodIssueMap.entrySet()) {
			Entry<String, String> classMethodPair = linterError.getKey();
			List<ExceptionLinterIssue> issues = linterError.getValue();

			// We only want to log violations, skip if NO_VIOLATION is present
			if (!issues.contains(ExceptionLinterIssue.NO_VIOLATION)) {
				for (ExceptionLinterIssue issue : issues) {
					String errString = String.format(LINTER_ERROR_FORMAT_STRING,
							classMethodPair.getKey().replace("/", "."),
							classMethodPair.getValue(), issue.getErrorString());
					LinterError err = new LinterError(classMethodPair.getKey().replace("/", "."),
							classMethodPair.getValue(), errString,
							ErrType.WARNING);

					errorList.add(err);
				}
			}
		}

		ReturnType type = new ReturnType("Generic Exception Linter", errorList);
		return type;
	}

	private void populateMethodIssueMapDefaults() {
		for (Entry<String, String> classMethodPair : this.methodExceptionThrownMap.keySet()) {
			// Create an entry for the new map

			List<ExceptionLinterIssue> entryIssues = new ArrayList<>();
			entryIssues.add(ExceptionLinterIssue.NO_VIOLATION);
			this.methodIssueMap.put(classMethodPair, entryIssues);

		}
	}

	public List<ExceptionLinterIssue> checkMethodCompliance(String className, String methodName) {
		List<ExceptionLinterIssue> issueList = new ArrayList<>();
		className = className.replace('.', '/'); // Just in case (mainly for testing)
		Entry<String, String> classMethodPair = new AbstractMap.SimpleEntry<>(className, methodName);

		// First, check throws, add issues to the list as needed
		String[] thrownExceptions = this.methodExceptionThrownMap.get(classMethodPair);
		if (thrownExceptions.length > 0) {
			for (String exceptionName : thrownExceptions) {
				// We have to use If/Else Here ,can't switch a string comparison in Java
				if (exceptionName.equals(JAVA_EXCEPTION_INTERNAL_CLASS)) {
					issueList.add(ExceptionLinterIssue.THROW_EXCEPTION);
				} else if (exceptionName.equals(JAVA_RUNTIMEEXCEPTION_INTERNAL_CLASS)) {
					issueList.add(ExceptionLinterIssue.THROW_RUNTIME_EXCEPTION);
				} else if (exceptionName.equals(JAVA_ERROR_INTERNAL_CLASS)) {
					issueList.add(ExceptionLinterIssue.THROW_ERROR);
				} else if (exceptionName.equals(JAVA_THROWABLE_INTERNAL_CLASS)) {
					issueList.add(ExceptionLinterIssue.THROW_THROWABLE);
				}
			}
		}

		// Then do the same thing with caught exceptions
		String[] caughtExceptions = this.methodExceptionCatchMap.get(classMethodPair);
		if (caughtExceptions.length > 0) {
			for (String exceptionName : caughtExceptions) {
				// We have to use If/Else Here ,can't switch a string comparison in Java
				if (exceptionName.equals(JAVA_EXCEPTION_INTERNAL_CLASS)) {
					issueList.add(ExceptionLinterIssue.CATCH_EXCEPTION);
				} else if (exceptionName.equals(JAVA_RUNTIMEEXCEPTION_INTERNAL_CLASS)) {
					issueList.add(ExceptionLinterIssue.CATCH_RUNTIME_EXCEPTION);
				} else if (exceptionName.equals(JAVA_THROWABLE_INTERNAL_CLASS)) {
					issueList.add(ExceptionLinterIssue.CATCH_THROWABLE);
				}
			}
		}

		// If we found no issues, add a NO_VIOLATION to the list and return it
		if (issueList.size() == 0) {
			issueList.add(ExceptionLinterIssue.NO_VIOLATION);
		}

		return issueList;
	}

	// some internal structures for return types

	// * Fun fact, this is technically a strategy pattern! Important note for that
	// * analyzer
	public enum ExceptionLinterIssue {
		NO_VIOLATION {
			@Override
			public String getErrorString() {
				return "does not contain any violations";
			}
		},
		THROW_EXCEPTION {
			@Override
			public String getErrorString() {
				return "throws java.lang.Exception in its method signature instead of a specific exception class";

			}
		},
		CATCH_EXCEPTION {
			@Override
			public String getErrorString() {
				return "has at least one catch block that catches java.lang.Exception instead of a specific exception class";
			}
		},
		THROW_RUNTIME_EXCEPTION {
			@Override
			public String getErrorString() {
				return "throws java.lang.RuntimeException in its method signature instead of a specific exception class";
			}
		},
		CATCH_RUNTIME_EXCEPTION {
			@Override
			public String getErrorString() {
				return "has at least one catch block that catches java.lang.RuntimeException instead of a specific exception class";
			}
		},
		THROW_ERROR {
			@Override
			public String getErrorString() {
				return "throws java.lang.Error in its method signature instead of a specific exception class";
			}
		},
		// CATCH_ERROR, --May not be possible, I *think* Errors actually crash the VM
		THROW_THROWABLE {
			@Override
			public String getErrorString() {
				return "throws java.lang.Throwable in its method signature instead of a specific exception class";
			}
		},
		CATCH_THROWABLE {
			@Override
			public String getErrorString() {
				return "has at least one catch block that catches java.lang.Throwable instead of a specific exception class";
			}
		}; // Usually not common practice AFAIK, still placing it just in case

		public abstract String getErrorString();

	}

}
