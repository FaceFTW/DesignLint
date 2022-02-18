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
    private Map<String, List<String>> staticMethods;
    private String[] classList;
    public SingletonAnalyzer(String[] classNames){
        
        try {
			this.parser = new ASMParser(classNames);
		} catch (IOException e) {
			e.printStackTrace();
		}
        this.errors = new ArrayList<>();
        this.validFields = new HashMap<>();
        this.staticMethods = new HashMap<>();
    }
    @Override
    public void getRelevantData(String[] classList) {
        this.classList = classList;
        for(String className : classList){
           List<String> classFields =  this.parser.getClassStaticPrivateFieldNames(className);
           validFields.put(className, classFields);
        }

        for(String className : classList){
            List<String> classMethods =  this.parser.getStaticMethods(className);
            staticMethods.put(className, classMethods);
         }
    }

    @Override
    public void analyzeData() {
        for(String className: classList){
           boolean hasStaticField =  analyzeFields(className);
           boolean hasPriCon = parser.isClassConstructorPrivate(className);
            boolean hasStaticMethod = analyzeMethods(className);

            if(hasPriCon && hasStaticField && hasStaticMethod){
                LinterError err = new LinterError(className, "Singleton Pattern detected!", ErrType.PATTERN);
                errors.add(err);
            }
        }
    }

    @Override
    public ReturnType composeReturnType() {
        ReturnType toReturn = new ReturnType("SingletonAnalyzer", this.errors);
        return toReturn;
    }

    private boolean analyzeFields(String className){
        return (validFields.get(className).size() > 0);
    }
    
    private boolean analyzeMethods(String className){

        for(String method : this.staticMethods.get(className)){
            if(method.equals("getInstance")){
                return true;
            }
        }
        return false;
    }

    
}
