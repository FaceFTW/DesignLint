package domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import datasource.ASMParser;
import datasource.MethodCall;

/**
 *  This class uses the given ASMParser (or creates one from the given classList) to check for possible violations
 * of the DRY Principle. 
 * 
 * @author Emily Hart (rhit-boatmaee)
 */
public class DryAnalyzer extends DomainAnalyzer{

    private ASMParser parser;
	private List<LinterError> errors;
    private Map<String, List<Method>> classToMethods;


    public DryAnalyzer(ASMParser parser){
        this.parser = parser;
        this.errors = new ArrayList<>();
        this.classToMethods = new HashMap<>();
    }
    public DryAnalyzer(String[] classList){
        try {
			this.parser = new ASMParser(classList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.errors = new ArrayList<>();
        this.classToMethods = new HashMap<>();
    }

    /**
     * This method gets the relevant data from the classes ASMParser, and stores it to be used later for analysis.
     */
    @Override
    public void getRelevantData(String[] classList) {
        for(String className : classList) {
			className = className.replace('.', '/');
			List<Method> methods = new ArrayList<Method>();
			String [] methodArr = parser.getMethods(className);
			for(int i = 0; i < methodArr.length; i++) {
				List<MethodCall> methodCalls = parser.getMethodCalls(className, methodArr[i]);
				Method method = new Method(methodArr[i], methodCalls);
				methods.add(method);
			}
			this.classToMethods.put(className, methods);
		}
    }

    /**
     * This method analyzes the given classes, and checks all of them against each other. A helper method is used.
     */
    @Override
    public void analyzeData() {
        for(String className : this.classToMethods.keySet()) {
			for(Method method : this.classToMethods.get(className)) {
                if(method.getName() != "<init>"){
				for(MethodCall methodCall : method.getMethodCalls()) {

                    checkForDuplication(className, method, methodCall);
				}
            }
			}
        }
    }

    /**
     * This method is a helper method for the analyzeData() method. If a duplicate method call is found, then 
     * a LinterError is added to the list to be returned.
     * @param classNameToCheck 
     * @param methodToCheck
     * @param methodCallToCheck
     * 
     */
    public void checkForDuplication(String classNameToCheck, Method methodToCheck, MethodCall methodCallToCheck){
        if(methodToCheck.getName().equals("<init>")){
            return;
        }
        int duplicateCount = 0;
        boolean addE = true;
        String message = "Possible Duplication with methods in the classes " ;
        
        for(String className: this.classToMethods.keySet()) {
        
            for(Method method : this.classToMethods.get(className)) {
                if(method.getName().equals("<init>")){
                    continue;
                }
                if(className.equals(classNameToCheck) && methodToCheck.equals(method)){
                    continue;
                } else{
                    for(MethodCall methodCall : method.getMethodCalls()) {  
                        if(methodCall.getCalledMethodName().equals(methodCallToCheck.getCalledMethodName())){
                            duplicateCount++;
                            for(LinterError err : this.errors){

                                if(err.className.equals(classNameToCheck) && err.methodName.equals(methodToCheck.getName())){
                                    addE = false;
                                }
                            }
                        }
                    }
                }
            
            String classNameToAddToMessage = "[" + className + "] ";
            message += classNameToAddToMessage;
        }{
            
        }
    }

    if(addE && duplicateCount >= 1){
        
            LinterError e = new LinterError(classNameToCheck, methodToCheck.getName(), 
        message, ErrType.WARNING);
        if(!methodToCheck.equals("<init>")){
            errors.add(e);
            }
}
}

    @Override
    public ReturnType composeReturnType() {
        return new ReturnType("DryAnalyzer", this.errors);
    }
    
}
