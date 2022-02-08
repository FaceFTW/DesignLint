package domain;

//Abstract class that all analyzers should extend.
public abstract class DomainAnalyzer {
    public int getFeedback(String[] classList) {
        //TODO: Change return types to ReturnType/ParsedData, whatever that ends up being.
        getRelevantData(classList);
        analyzeData();
        int y = composeReturnType();
        return y;
    }

    public abstract void getRelevantData(String[] classList);

    //TODO: Change x to ParsedData type.
    public abstract void analyzeData();

    public abstract int composeReturnType();
}
