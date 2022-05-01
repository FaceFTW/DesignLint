package domain.message;

public final class ErrorLinterMessage extends LinterMessage {

	public ErrorLinterMessage(String className, String methodName, String message) {
		super(className, methodName, message);
	}

	public ErrorLinterMessage(String className, String message) {
		super(className, message);
	}

	@Override
	public String getMessageType() {
		return "ERROR";
	}

}