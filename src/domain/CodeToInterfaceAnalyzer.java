package domain;

import datasource.ASMParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeToInterfaceAnalyzer extends DomainAnalyzer{

    private ASMParser parser;

    //Parsed Data
    public Map<String, List<String>> fieldNames;
    public Map<String, List<String>> fieldTypes;
    public Map<String, Map<String, List<String>>> methodVarNames;
    public Map<String, Map<String, List<String>>> methodVarTypes;

    //Erroneous Values
    List<LinterError> foundErrors = null;

    public CodeToInterfaceAnalyzer(String[] classNames) {
        try {
            this.parser = new ASMParser(classNames);
            this.fieldNames = new HashMap<>();
            this.fieldTypes = new HashMap<>();
            this.methodVarNames = new HashMap<>();
            this.methodVarTypes = new HashMap<>();
            this.foundErrors = new ArrayList<LinterError>();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getRelevantData(String[] classList) {
        for (String className : classList) {
            this.fieldNames.put(className, parser.getClassFieldNames(className));
            this.fieldTypes.put(className, parser.getClassFieldTypes(className));
            this.methodVarNames.put(className, parser.getMethodNames(className));
            this.methodVarTypes.put(className, parser.getMethodVarTypes(className));
        }
    }

    public void analyzeData() {
        for (String className : this.fieldNames.keySet()) {
            checkForPrimitive(this.fieldNames.get(className), this.fieldTypes.get(className), className, null);
        }
        for (String className : this.methodVarNames.keySet()) {
            for (String methodName : this.methodVarNames.get(className).keySet()) {
                checkForPrimitive(this.methodVarNames.get(className).get(methodName),
                        this.methodVarTypes.get(className).get(methodName), className, methodName);
            }
        }
    }

    public ReturnType composeReturnType() {
        return new ReturnType("CodeToInterfaceAnalyzer", null);
    }

    public void checkForPrimitive(List<String> varNameArr, List<String> varTypeArr, String className, String methodName) {
        for (int i = 0; i < varNameArr.size(); i++) {
            String varType = varTypeArr.get(i);
            //In ASM, non-primitive types begin with 'L'.
            //We are going to consider String as a "primitive" type.
            if (varType.charAt(0) == 'L' && varType.compareTo("Ljava/lang/String;") != 0) {
                checkForInterface(varNameArr.get(i), varTypeArr.get(i), className, methodName);
            }
        }
    }

    public void checkForInterface(String varName, String varType, String className, String methodName) {
        varType = varType.substring(1, varType.length() - 1);

        //Common interfaces that we want to utilize. Simplifies the searching.
        if (varType.compareTo("java/util/ArrayList") == 0) {
            this.foundErrors.add(new LinterError(className, methodName,
                    "Variable " + varName + " should be of type List<> (instead of type ArrayList<>).", ErrType.ERROR));
        }
        else if (varType.compareTo("java/util/HashMap") == 0) {
            this.foundErrors.add(new LinterError(className, methodName,
                    "Variable " + varName + " should be of type Map<> (instead of type HashMap<>).", ErrType.ERROR));
        }
        else if (varType.compareTo("java/util/HashSet") == 0) {
            this.foundErrors.add(new LinterError(className, methodName,
                    "Variable " + varName + " should be of type Set<> (instead of type HashSet<>).", ErrType.ERROR));
        }
        else {
            List<String> interfaces = this.parser.getInterfacesWithoutMap(varType);

            if (interfaces.size() > 0) {
                int size = interfaces.size();
                String message = "Variable " + varName + " (Type: " + varType + ") is currently implementing interfaces: ";

                if (interfaces.size() > 3) {
                    size = 3;
                }

                //List 3 potential interface you could write to. We don't know enough about their code to say for certain which one.
                for (int i = 0; i < size; i++) {
                    message += interfaces.get(i);
                    if (i != size-1) {
                        message += ", ";
                    }
                }
                //Errors of type Warning, as we don't know if coding to the interface is correct.
                this.foundErrors.add(new LinterError(className, methodName, message, ErrType.WARNING));
            }
            //Else, move to next entry.
        }
    }
}
