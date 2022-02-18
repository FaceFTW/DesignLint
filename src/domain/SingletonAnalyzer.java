package domain;

import java.util.List;

import datasource.ASMParser;

public class SingletonAnalyzer extends DomainAnalyzer {

    private ASMParser parser;
    private List<LinterError> errors;
    public SingletonAnalyzer(ASMParser parser){
        this.parser = parser;
        //TODO: create the errorList for the returnType
    }
    @Override
    public void getRelevantData(String[] classList) {
        // TODO:  get fields for each class
        // TODO: get constructor for each class
        // TODO: get methods for each class 
        
        
    }

    @Override
    public void analyzeData() {
        // TODO: analyze fields for a private static singleton object for each class
        // TODO: analyze constructor for a private access modifier
        // TODO: analyze methods for a static getInstance method
        // TODO: add to the errorList if a singleton is found
        
    }

    @Override
    public ReturnType composeReturnType() {
        // TODO: use the errorList created in analyzeData method to make the returnType
        return null;
    }
    
}
