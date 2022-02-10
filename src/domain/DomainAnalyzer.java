package domain;

//Abstract class that all analyzers should extend.
public abstract class DomainAnalyzer {
    public ReturnType getFeedback(String[] classList) {
        getRelevantData(classList);
        analyzeData();
        return composeReturnType();
    }

    public abstract void getRelevantData(String[] classList);

    public abstract void analyzeData();

    public abstract ReturnType composeReturnType();
}
