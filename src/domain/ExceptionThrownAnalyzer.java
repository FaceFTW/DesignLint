package domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import datasource.ASMParser;

public class ExceptionThrownAnalyzer extends DomainAnalyzer {
    private ASMParser parser;
    private Map<String, String[]> classMethodMap;
    private Map<Entry<String, String>, String> methodExceptionThrownMap;
    private Map<Entry<String, String>, String[]> methodExceptionCatchMap;

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

    protected boolean checkMethodCompliance(String className, String methodName) {

        return false;
    }

}
