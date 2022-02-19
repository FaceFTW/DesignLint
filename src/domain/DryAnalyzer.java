package domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import datasource.ASMParser;
import datasource.MethodCall;

public class DryAnalyzer extends DomainAnalyzer{

    private ASMParser parser;
	private List<LinterError> errors;
    private String[] classList;
    private Map<String, List<Method>> classToMethods;

    public DryAnalyzer(String[] classList){
        try {
			this.parser = new ASMParser(classList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.errors = new ArrayList<>();
        this.classToMethods = new HashMap<>();
    }

    @Override
    public void getRelevantData(String[] classList) {
        this.classList = classList;
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

    @Override
    public void analyzeData() {
        for(String className : this.classToMethods.keySet()) {
			for(Method method : this.classToMethods.get(className)) {
				for(MethodCall methodCall : method.getMethodCalls()) {
                    checkForDuplication(className, method, methodCall);
				}
			}
        }
    }

    public void checkForDuplication(String classNameToCheck, Method methodToCheck, MethodCall methodCallToCheck){

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
                        
                        LinterError e = new LinterError(classNameToCheck, methodToCheck.getName(), 
                        "Duplication in method " + method.getName() + " from class " + className, ErrType.WARNING);
                        boolean addE = true;
                        for(LinterError err : this.errors){
                            if(err.className.equals(e.className) && err.methodName.equals(e.methodName)){
                                addE = false;
                            }
                        }
                        if(addE){
                            if(!e.methodName.equals("<init>")){
                            errors.add(e);
                            }
                        }
                        continue;
                    }
                }
            }
        }
        }
    }

    @Override
    public ReturnType composeReturnType() {

        
        return new ReturnType("DryAnalyzer", this.errors);
    }
    
}
