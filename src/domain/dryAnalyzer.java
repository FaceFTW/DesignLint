package domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import datasource.ASMParser;

public class dryAnalyzer extends DomainAnalyzer{

    private ASMParser parser;
	private List<LinterError> errors;
    private String[] classList;

    public dryAnalyzer(String[] classList){
        try {
			this.parser = new ASMParser(classList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.errors = new ArrayList<>();
    }

    @Override
    public void getRelevantData(String[] classList) {
        this.classList = classList;
        // TODO: Need to get methods from all classes 
    }

    @Override
    public void analyzeData() {
        // TODO: Need to check methods for duplication and 
        // trigger an error if two methods have duplication

        
    }

    @Override
    public ReturnType composeReturnType() {
        return new ReturnType("dryAnalyzer", this.errors);
    }
    
}
