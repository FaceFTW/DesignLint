package domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import datasource.ASMParser;

public class SingletonAnalyzer extends DomainAnalyzer {

    private ASMParser parser;
    private List<LinterError> errors;
    private Map<String, List<String>> validFields;
    private Map<String, String[]> methods;
    private String[] classList;
    public SingletonAnalyzer(String[] classNames){
        
        try {
			this.parser = new ASMParser(classNames);
		} catch (IOException e) {
			e.printStackTrace();
		}
        this.errors = new ArrayList<>();
        this.validFields = new HashMap<>();
        this.methods = new HashMap<>();
    }
    @Override
    public void getRelevantData(String[] classList) {
        this.classList = classList;
        for(String className : classList){
           List<String> classFields =  this.parser.getClassStaticPrivateFieldNames(className);
           validFields.put(className, classFields);
        }

        for(String className : classList){
            String[] classMethods =  this.parser.getMethods(className);
            methods.put(className, classMethods);
         }
    }

    @Override
    public void analyzeData() {
        // TODO: analyze fields for a private static singleton object for each class
        for(String className: classList){
           boolean hasStaticField =  analyzeFields(className);
           boolean hasPriCon = analyzeConstructor(className);
            boolean hasStaticMethod = analyzeMethods(className);

            if(hasPriCon && hasStaticField && hasStaticMethod){
                LinterError err = new LinterError(className, "Singleton Pattern detected!", ErrType.PATTERN);
                errors.add(err);
            }
        }
       
        // TODO: add to the errorList if a singleton is found
        
    }

    @Override
    public ReturnType composeReturnType() {
        // TODO: use the errorList created in analyzeData method to make the returnType
        return null;
    }

    private boolean analyzeFields(String className){

        return false;
    }
    private boolean analyzeConstructor(String className){
        // TODO: analyze constructor for a private access modifier
        return false;

    }
    private boolean analyzeMethods(String className){
        // TODO: analyze methods for a static getInstance method
        return false;
    }

    
}
