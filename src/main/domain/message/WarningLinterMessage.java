package domain.message;

public final class WarningLinterMessage extends LinterMessage {

	public WarningLinterMessage(String className, String methodName, String message) {
		super(className, methodName, message);
	}

	public WarningLinterMessage(String className, String message) {
		super(className, message);
	}

	@Override
	public String getMessageType() {
		return "WARNING";
	}

}