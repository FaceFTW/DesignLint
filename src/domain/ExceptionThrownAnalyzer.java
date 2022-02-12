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

	private Map<Entry<String, String>, List<ExceptionThrownIssue>> methodIssueMap;

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
		// TODO Auto-generated method stub

	}

	@Override
	public ReturnType composeReturnType() {
		// TODO Auto-generated method stub
		return null;
	}

	private void populateMethodIssueMapDefaults() {
		for (Entry<String, String> classMethodPair : this.methodExceptionThrownMap.keySet()) {
			// Create an entry for the new map

			List<ExceptionThrownIssue> entryIssues = new ArrayList<>();
			entryIssues.add(ExceptionThrownIssue.NO_VIOLATION);
			this.methodIssueMap.put(classMethodPair, entryIssues);

		}
	}

	public List<ExceptionThrownIssue> checkMethodCompliance(String className, String methodName) {

		return null;
	}

	// some internal structures for return types

	// * Fun fact, this is technically a strategy pattern! Important note for that
	// * analyzer
	public enum ExceptionThrownIssue {
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
