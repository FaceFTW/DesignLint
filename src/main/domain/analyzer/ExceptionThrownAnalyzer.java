package domain.analyzer;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import datasource.ASMParser;
import domain.DomainAnalyzer;
import domain.message.LinterMessage;
import domain.message.WarningLinterMessage;
import domain.AnalyzerReturn;

public class ExceptionThrownAnalyzer extends DomainAnalyzer {
	public static final String JAVA_EXCEPTION_INTERNAL_CLASS = "java/lang/Exception";
	public static final String JAVA_RUNTIMEEXCEPTION_INTERNAL_CLASS = "java/lang/RuntimeException";
	public static final String JAVA_ERROR_INTERNAL_CLASS = "java/lang/Error";
	public static final String JAVA_THROWABLE_INTERNAL_CLASS = "java/lang/Throwable";

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
		for (String className : classList) {
			className = className.replace('.', '/');
			String[] classMethods = parser.getMethods(className);
			classMethodMap.put(className, classMethods);
		}

		for (String className : classMethodMap.keySet()) {
			for (String methodName : classMethodMap.get(className)) {
				Entry<String, String> classMethodPair = new AbstractMap.SimpleEntry<>(className, methodName);

				String[] thrownExceptions = this.parser.getMethodExceptionSignature(className, methodName);
				this.methodExceptionThrownMap.put(classMethodPair, thrownExceptions);

				String[] caughtExceptions = this.parser.getMethodExceptionCaught(className, methodName);
				this.methodExceptionCatchMap.put(classMethodPair, caughtExceptions);
			}
		}

		this.populateMethodIssueMapDefaults();
	}

	@Override
	public void analyzeData() {
		for (Entry<String, String> classMethodPair : methodIssueMap.keySet()) {
			List<ExceptionLinterIssue> methodIssues = this.checkMethodCompliance(classMethodPair.getKey(),
					classMethodPair.getValue());
			this.methodIssueMap.put(classMethodPair, methodIssues);
		}

	}

	@Override
	public AnalyzerReturn composeReturnType() {
		List<LinterMessage> errorList = new ArrayList<>();
		for (Entry<Entry<String, String>, List<ExceptionLinterIssue>> linterError : methodIssueMap.entrySet()) {
			Entry<String, String> classMethodPair = linterError.getKey();
			List<ExceptionLinterIssue> issues = linterError.getValue();

			if (!issues.contains(ExceptionLinterIssue.NO_VIOLATION)) {
				for (ExceptionLinterIssue issue : issues) {
					String errString = String.format(LINTER_ERROR_FORMAT_STRING,
							classMethodPair.getKey().replace("/", "."),
							classMethodPair.getValue(), issue.getErrorString());
					LinterMessage err = new WarningLinterMessage(classMethodPair.getKey().replace("/", "."),
							classMethodPair.getValue(), errString);

					errorList.add(err);
				}
			}
		}

		AnalyzerReturn type = new AnalyzerReturn("Generic Exception Linter", errorList);
		return type;
	}

	private void populateMethodIssueMapDefaults() {
		for (Entry<String, String> classMethodPair : this.methodExceptionThrownMap.keySet()) {

			List<ExceptionLinterIssue> entryIssues = new ArrayList<>();
			entryIssues.add(ExceptionLinterIssue.NO_VIOLATION);
			this.methodIssueMap.put(classMethodPair, entryIssues);

		}
	}

	public List<ExceptionLinterIssue> checkMethodCompliance(String className, String methodName) {
		List<ExceptionLinterIssue> issueList = new ArrayList<>();
		className = className.replace('.', '/');
		Entry<String, String> classMethodPair = new AbstractMap.SimpleEntry<>(className, methodName);

		String[] thrownExceptions = this.methodExceptionThrownMap.get(classMethodPair);
		if (thrownExceptions.length > 0) {
			for (String exceptionName : thrownExceptions) {
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

		String[] caughtExceptions = this.methodExceptionCatchMap.get(classMethodPair);
		if (caughtExceptions.length > 0) {
			for (String exceptionName : caughtExceptions) {
				if (exceptionName.equals(JAVA_EXCEPTION_INTERNAL_CLASS)) {
					issueList.add(ExceptionLinterIssue.CATCH_EXCEPTION);
				} else if (exceptionName.equals(JAVA_RUNTIMEEXCEPTION_INTERNAL_CLASS)) {
					issueList.add(ExceptionLinterIssue.CATCH_RUNTIME_EXCEPTION);
				} else if (exceptionName.equals(JAVA_THROWABLE_INTERNAL_CLASS)) {
					issueList.add(ExceptionLinterIssue.CATCH_THROWABLE);
				}
			}
		}

		if (issueList.size() == 0) {
			issueList.add(ExceptionLinterIssue.NO_VIOLATION);
		}

		return issueList;
	}

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
		};

		public abstract String getErrorString();

	}

}
