package domain;

import datasource.ASMParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VarNameAnalyzer extends DomainAnalyzer {

    private ASMParser parser;
    //Parsed Values
    Map<String, ArrayList<String>> fieldNames;
    Map<String, ArrayList<String>> globalNames;
    Map<String, Map<String, ArrayList<String>>> methodNames;

    //Erroneous Values
    ArrayList<LinterError> foundErrors;

    public VarNameAnalyzer(String[] classNames) {
        try {
            this.parser = new ASMParser(classNames);
            this.fieldNames = new HashMap<String, ArrayList<String>>();
            this.globalNames = new HashMap<String, ArrayList<String>>();
            this.methodNames = new HashMap<String, Map<String, ArrayList<String>>>();
            this.foundErrors = null;
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void getRelevantData(String[] classList) {
        for (String className : classList) {
            this.fieldNames.put(className, parser.getClassFieldNames(className));
            this.methodNames.put(className, parser.getMethodNames(className));
            this.globalNames.put(className, parser.getGlobalNames(className));
        }
    }

    public void analyzeData() {
        //Instantiate the ArrayList now our parse is successful
        this.foundErrors = new ArrayList<>();

        //Grab any general errors for each variable type, along with any type-specific errors
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

    //Grabs any style errors that apply to every type of variable.
    public void analyzeGeneralErrors(ArrayList<String> varNames, String className, String methodName) {
        for (String var : varNames) {
            //Begins with _ - Error
            if (var.charAt(0) == '_') {
                this.foundErrors.add(new LinterError(className, methodName, var + " begins with _", ErrType.ERROR));
            }

            //Begins with $ - Error
            if (var.charAt(0) == '$') {
                this.foundErrors.add(new LinterError(className, methodName, var + " begins with $", ErrType.ERROR));
            }
        }
    }

    public void analyzeFieldNames(ArrayList<String> varNames, String className) {
        for (String var : varNames) {
            //First character must be lowercase - Error
            if (Character.isLetter(var.charAt(0)) &&
                    var.charAt(0) != Character.toLowerCase(var.charAt(0))) {
                this.foundErrors.add(new LinterError(className, null, var + " begins with capital letter", ErrType.ERROR));
            }

            //Too long (>30 characters) - Warning
            if (var.length() > 30) {
                this.foundErrors.add(new LinterError(className, null, var + " too long (>30 characters)", ErrType.WARNING));
            }

            //Too short (1-2 characters) - Warning
            if (var.length() <= 2) {
                this.foundErrors.add(new LinterError(className, null, var + " too short (<=2 characters)", ErrType.WARNING));
            }
        }

    }

    public void analyzeGlobalNames(ArrayList<String> varNames, String className) {
        for (String var : varNames) {
            //Entire name must be capitalized - Error
            if (var.toUpperCase().compareTo(var) != 0) {
                this.foundErrors.add(new LinterError(className, null, var + " must only be capital letters", ErrType.ERROR));
            }

            //Too short (1-2 characters) - Warning
            if (var.length() <= 2) {
                this.foundErrors.add(new LinterError(className, null, var + " too short (<=2 characters)", ErrType.WARNING));
            }
        }
    }

    public void analyzeMethodNames(ArrayList<String> varNames, String className, String methodName) {
        for (String var : varNames) {
            //Ignore "this"
            if (var.compareTo("this") == 0) {
                continue;
            }

            //First character must be lowercase - ERROR
            if (Character.isLetter(var.charAt(0)) &&
                    var.charAt(0) != Character.toLowerCase(var.charAt(0))) {
                this.foundErrors.add(new LinterError(className, methodName, var + " begins with capital letter", ErrType.ERROR));
            }

            //Too long (>30 characters) - WARNING
            if (var.length() > 30) {
                this.foundErrors.add(new LinterError(className, methodName, var + " too long (>30 characters)", ErrType.WARNING));
            }
        }
    }
}
