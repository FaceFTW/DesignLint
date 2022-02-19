package domain;

import datasource.ASMParser;
import datasource.Invoker;
import datasource.MethodCall;

import java.io.IOException;
import java.util.*;

public class CodeToInterfaceAnalyzer extends DomainAnalyzer {

    private ASMParser parser;

    //Parsed Data
    public Map<String, List<String>> fieldNames;
    public Map<String, List<String>> fieldTypes;
    public Map<String, Map<String, List<String>>> methodVarNames;
    public Map<String, Map<String, List<String>>> methodVarTypes;
    public Map<String, Map<String, Map<String, Map<String, List<String>>>>> possibleInterfaces;

    //Erroneous Values
    List<LinterError> foundErrors;

    public CodeToInterfaceAnalyzer(String[] classNames) {
        try {
            this.parser = new ASMParser(classNames);
            this.fieldNames = new HashMap<>();
            this.fieldTypes = new HashMap<>();
            this.methodVarNames = new HashMap<>();
            this.methodVarTypes = new HashMap<>();
            this.possibleInterfaces = new HashMap<>();
            this.foundErrors = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getRelevantData(String[] classList) {
        for (String className : classList) {
            this.fieldNames.put(className, parser.getClassFieldNames(className));
            this.fieldTypes.put(className, parser.getClassFieldTypes(className));
            this.methodVarNames.put(className, parser.getMethodNamesAndVariables(className));
            this.methodVarTypes.put(className, parser.getMethodVarTypes(className));
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
//                    System.out.println("Class: " + className);
//                    System.out.println("Method: " + methodName);
//                    System.out.println("VarName: " + varName);
                    //System.out.println(this.possibleInterfaces.get(className).get(methodName).get(varName));

                    analyzePotentialInterfaces(className, methodName, varName);
                }
            }
        }

    }

    public ReturnType composeReturnType() {
        return new ReturnType("CodeToInterfaceAnalyzer", this.foundErrors);
    }

    public void findFieldShortCut() {
        for (String className : this.fieldTypes.keySet()) {
            for (int i = 0; i < this.fieldTypes.get(className).size(); i++) {
                if (fieldTypes.get(className).get(i).compareTo("Ljava/util/ArrayList;") == 0) {
                    this.foundErrors.add(new LinterError(className, null,
                            "Field " + this.fieldNames.get(className).get(i) + " should be of type List<>, not ArrayList<>", ErrType.ERROR));
                } else if (fieldTypes.get(className).get(i).compareTo("Ljava/util/HashMap;") == 0) {
                    this.foundErrors.add(new LinterError(className, null,
                            "Field " + this.fieldNames.get(className).get(i) + " should be of type Map<>, not HashMap<>", ErrType.ERROR));
                } else if (fieldTypes.get(className).get(i).compareTo("Ljava/util/HashSet;") == 0) {
                    this.foundErrors.add(new LinterError(className, null,
                            "Field " + this.fieldNames.get(className).get(i) + " should be of type Set<>, not HashSet<>", ErrType.ERROR));
                }
            }
        }
    }

    public void analyzePotentialInterfaces(String className, String methodName, String varName) {
        if (!findShortCut(className, methodName, varName)) {
            Map<String, List<String>> interfaces = this.possibleInterfaces.get(className).get(methodName).get(varName);
            //System.out.println(varName + " " + interfaces);
            List<String> union = new ArrayList<>();
            boolean x = false;
            union.add("Init List");
            for (String methodKey : interfaces.keySet()) {
                //System.out.println(methodKey + " " + union);
                if (union.get(0).compareTo("Init List") == 0) {
                    union = interfaces.get(methodKey);
                }

                if (interfaces.get(methodKey).contains("X"))
                    x = true;
                else if (!x) {
                    union = intersectLists(union, interfaces.get(methodKey));
                }
            }
//            System.out.println(union);
//            System.out.println(x);
            if (!x && union.size() > 0 && union.get(0).compareTo("<init>") != 0) {
                //System.out.println("Error for " + className + " " + methodName + " " + varName);
                for (int i = 0; i < union.size(); i++) {
                    //Finally, if the types in the union match the type of the variable, do not throw an error.
                    int index = this.methodVarNames.get(className).get(methodName).indexOf(varName);
                    if (index == -1) {
                        index = this.fieldNames.get(className).indexOf(varName);
                        if (this.fieldTypes.get(className).get(index).compareTo("L" + union.get(i) + ";") != 0) {
                            this.foundErrors.add(new LinterError(className, methodName,
                                    "Potential Interface for " + varName + ": " + union.get(i), ErrType.WARNING));
                        }
                    }
                    else if (this.methodVarTypes.get(className).get(methodName).get(index).compareTo("L" + union.get(i) + ";") != 0) {
                        this.foundErrors.add(new LinterError(className, methodName,
                                "Potential Interface for " + varName + ": " + union.get(i), ErrType.WARNING));
                    }
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
        if (type.compareTo("Ljava/util/ArrayList;") == 0) {
            this.foundErrors.add(new LinterError(className, methodName,
                    "Local Variable " + varName + " should be of type List<>, not ArrayList<>", ErrType.ERROR));
            return true;
        } else if (type.compareTo("Ljava/util/HashMap;") == 0) {
            this.foundErrors.add(new LinterError(className, methodName,
                    "Local Variable " + varName + " should be of type Map<>, not HashMap<>", ErrType.ERROR));
            return true;
        } else if (type.compareTo("Ljava/util/HashSet;") == 0) {
            this.foundErrors.add(new LinterError(className, methodName,
                    "Local Variable " + varName + " should be of type Set<>, not HashSet<>", ErrType.ERROR));
            return true;
        } else {
            return false;
        }
    }

    public void checkMethodSignature(String className, String methodName) {
        //System.out.println(className + ": " + methodName);
        List<MethodCall> methodCalls = this.parser.removeThis(this.parser.getMethodCalls(className, methodName),
                this.methodVarNames.get(className).get(methodName));
//        for (MethodCall m : methodCalls) {
//            System.out.println(m);
//        }
        for (MethodCall method : methodCalls) {
            if (method.getInvoker() == Invoker.FIELD) {
                continue;
            }
            List<String> interfaces = parser.getInterfacesWithoutMap(method.getInvokedClass());
            if (interfaces.size() > 0) {
                //System.out.println(interfaces);
                boolean foundInInterface = false;
                for (String interf : interfaces) {
                    boolean foundinCurrentInterface = false;
                    if (parser.compareMethodFromInterface(method.getInvokedClass(), method.getCalledMethodName(), interf)) {
                        foundInInterface = true;
                        foundinCurrentInterface = true;
                    }
                    //Put into our possible interface map.
                    //Class
                    if (!this.possibleInterfaces.keySet().contains(className)) {
                        this.possibleInterfaces.put(className, new HashMap<>());
                    }
                    //Method
                    if (!this.possibleInterfaces.get(className).keySet().contains(methodName)) {
                        this.possibleInterfaces.get(className).put(methodName, new HashMap<>());
                    }
                    //VariableName
                    if (!this.possibleInterfaces.get(className).get(methodName).keySet().contains(method.getInvokerName())) {
                        this.possibleInterfaces.get(className).get(methodName).put(method.getInvokerName(), new HashMap<>());
                    }
                    //MethodCalled on Variable
                    if (!this.possibleInterfaces.get(className).get(methodName).get(method.getInvokerName()).
                            keySet().contains(method.getCalledMethodName())) {
                        this.possibleInterfaces.get(className).get(methodName).get(method.getInvokerName()).
                                put(method.getCalledMethodName(), new ArrayList<>());
                    }
                    //Input into our Map
                    if (foundinCurrentInterface) {
//                        System.out.println(method.getInvokerName());
//                        System.out.println(method.getCalledMethodName());
//                        System.out.println(interf);
//                        System.out.println();


                        this.possibleInterfaces.get(className).get(methodName).get(method.getInvokerName()).
                                get(method.getCalledMethodName()).add(interf);
                    }
                }
                if (!foundInInterface) {
                    this.possibleInterfaces.get(className).get(methodName).get(method.getInvokerName()).
                            get(method.getCalledMethodName()).add("X");
                }
            }
//            else {
//                //System.out.println("No interfaces found");
//            }
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

//    public void checkForPrimitive(List<String> varNameArr, List<String> varTypeArr, String className, String methodName) {
//        for (int i = 0; i < varNameArr.size(); i++) {
//            String varType = varTypeArr.get(i);
//            //In ASM, non-primitive types begin with 'L'.
//            //We are going to consider String as a "primitive" type.
//            if (varType.charAt(0) == 'L' && varType.compareTo("Ljava/lang/String;") != 0) {
//                checkForInterface(varNameArr.get(i), varTypeArr.get(i), className, methodName);
//            }
//        }
//    }
//
//    public void checkForInterface(String varName, String varType, String className, String methodName) {
//        varType = varType.substring(1, varType.length() - 1);
//
//        //Common interfaces that we want to utilize. Simplifies the searching.
//        if (varType.compareTo("java/util/ArrayList") == 0) {
//            this.foundErrors.add(new LinterError(className, methodName,
//                    "Variable " + varName + " should be of type List<> (instead of type ArrayList<>).", ErrType.ERROR));
//        }
//        else if (varType.compareTo("java/util/HashMap") == 0) {
//            this.foundErrors.add(new LinterError(className, methodName,
//                    "Variable " + varName + " should be of type Map<> (instead of type HashMap<>).", ErrType.ERROR));
//        }
//        else if (varType.compareTo("java/util/HashSet") == 0) {
//            this.foundErrors.add(new LinterError(className, methodName,
//                    "Variable " + varName + " should be of type Set<> (instead of type HashSet<>).", ErrType.ERROR));
//        }
//        else {
//            List<String> interfaces = this.parser.getInterfacesWithoutMap(varType);
//
//            if (interfaces.size() > 0) {
//                int size = interfaces.size();
//                String message = "Variable " + varName + " (Type: " + varType + ") is currently implementing interfaces: ";
//
//                if (interfaces.size() > 3) {
//                    size = 3;
//                }
//
//                //List 3 potential interface you could write to. We don't know enough about their code to say for certain which one.
//                for (int i = 0; i < size; i++) {
//                    message += interfaces.get(i);
//                    if (i != size-1) {
//                        message += ", ";
//                    }
//                }
//                //Errors of type Warning, as we don't know if coding to the interface is correct.
//                this.foundErrors.add(new LinterError(className, methodName, message, ErrType.WARNING));
//            }
//            //Else, move to next entry.
//        }
//    }
//}
