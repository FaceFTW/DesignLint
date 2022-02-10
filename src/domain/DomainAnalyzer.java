package domain;

//Abstract class that all analyzers should extend.
public abstract class DomainAnalyzer {
    public ReturnType getFeedback(String[] classList) {
        //TODO: Change return types to ReturnType/ParsedData, whatever that ends up being.
        getRelevantData(classList);
        analyzeData();
        ReturnType returnedMessages = composeReturnType();
        return returnedMessages;
    }

    public abstract void getRelevantData(String[] classList);

    //TODO: Change x to ParsedData type.
    public abstract void analyzeData();

    public abstract ReturnType composeReturnType();
}
