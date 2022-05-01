package domain.message;

public abstract class LinterMessage {
	public String className;
	public String methodName;
	public String message;

	public LinterMessage(String className, String methodName, String message) {
		this.className = className;
		this.methodName = methodName;
		this.message = message;
	}

	public LinterMessage(String className, String message) {
		this.className = className;
		this.methodName = null;
		this.message = message;
	}

	public abstract String getMessageType();

	public String toString() {
		return "Class Name: " + this.className + "\n" +
				"Method Name: " + this.methodName + "\n" +
				"Message: " + this.message + "\n";
	}

}

