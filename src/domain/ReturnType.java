package domain;

import java.util.*;

public class ReturnType {
    public String analyzerName;
    public ArrayList<LinterError> errorsCaught;

    ReturnType(String analyzerName, ArrayList<LinterError> errorsCaught) {
        this.analyzerName = analyzerName;
        this.errorsCaught = errorsCaught;
    }
}

