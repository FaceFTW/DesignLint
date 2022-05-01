package domain;

import java.util.List;

import domain.message.LinterMessage;

public class AnalyzerReturn {
	public String analyzerName;
	public List<LinterMessage> errorsCaught;

	private int infoCount = 0;
	private int warningCount = 0;
	private int patternCount = 0;
	private int errorCount = 0;
	private int unknownCount = 0;

	public AnalyzerReturn(String analyzerName, List<LinterMessage> errorsCaught) {
		this.analyzerName = analyzerName;
		this.errorsCaught = errorsCaught;
		countMessageTypes();
	}

	public int getUnknownCount() {
		return unknownCount;
	}

	public int getErrorCount() {
		return errorCount;
	}

	public int getPatternCount() {
		return patternCount;
	}

	public int getWarningCount() {
		return warningCount;
	}

	public int getInfoCount() {
		return infoCount;
	}

	private void countMessageTypes() {
		for (LinterMessage message : errorsCaught) {
			if (message.getMessageType().equals("INFO")) {
				infoCount++;
				continue;
			}
			if (message.getMessageType().equals("WARNING")) {
				warningCount++;
				continue;
			}
			if (message.getMessageType().equals("PATTERN")) {
				patternCount++;
				continue;
			}
			if (message.getMessageType().equals("ERROR")) {
				errorCount++;
				continue;
			}
			unknownCount++;
		}
	}

}
