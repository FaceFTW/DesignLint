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
    private Map<Entry<String, String>, String> methodExceptionThrownMap;
    private Map<Entry<String, String>, String[]> methodExceptionCatchMap;

    private Map<Entry<String, String>, List<ExceptionThrownIssue>> methodIssueMap;

    public ExceptionThrownAnalyzer(ASMParser parser) {
        super();

        this.parser = parser;
        this.classMethodMap = new HashMap<>();
        this.methodExceptionThrownMap = new HashMap<>();
        this.methodExceptionCatchMap = new HashMap<>();
    }

    @Override
    public void getRelevantData(String[] classList) {
        // TODO Auto-generated method stub

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
        for (Entry<String, String[]> entry : classMethodMap.entrySet()) {
            String className = entry.getKey(); // Store this for later
            String[] methods = entry.getValue();
            for (String method : methods) {
                // Create an entry for the new map
                Entry<String, String> newEntry = new AbstractMap.SimpleEntry<>(className, method);
                List<ExceptionThrownIssue> entryIssues = new ArrayList<>();
                entryIssues.add(ExceptionThrownIssue.NO_VIOLATION);
                this.methodIssueMap.put(newEntry, entryIssues);
            }
        }
    }

    public boolean checkMethodThrowsCompliance(String className, String methodName) {

        return false;
    }

    public boolean checkMethodCatchCompliance(String className, String methodName) {

        return false;
    }

    // some internal structures for return types

    // * Fun fact, this is technically a strategy pattern! Important note for that
    // * analyzer
    enum ExceptionThrownIssue {
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
