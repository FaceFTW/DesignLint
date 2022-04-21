package domain;

import java.util.*;

public class ReturnType {
	public String analyzerName;
	public List<LinterError> errorsCaught;

	public ReturnType(String analyzerName, List<LinterError> errorsCaught) {
		this.analyzerName = analyzerName;
		this.errorsCaught = errorsCaught;
	}
}
