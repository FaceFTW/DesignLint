package domain.message;

public final class InfoLinterMessage extends LinterMessage {

	public InfoLinterMessage(String className, String methodName, String message) {
		super(className, methodName, message);
	}

	public InfoLinterMessage(String className, String message) {
		super(className, message);
	}

	@Override
	public String getMessageType() {
		return "INFO";
	}

}