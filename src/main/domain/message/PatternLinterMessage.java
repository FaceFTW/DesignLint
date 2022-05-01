package domain.message;

public final class PatternLinterMessage extends LinterMessage {

	public PatternLinterMessage(String className, String methodName, String message) {
		super(className, methodName, message);
	}

	public PatternLinterMessage(String className, String message) {
		super(className, message);
	}

	@Override
	public String getMessageType() {
		return "PATTERN";
	}

}