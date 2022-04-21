package domain;

public class LinterError {
	public String className;
	public String methodName;
	public String message;
	public ErrType type;
	
	public LinterError(String className, String methodName, String message, ErrType type) {
		this.className = className;
		this.methodName = methodName;
		this.message = message;
		this.type = type;
	}
	
	public LinterError(String className, String message, ErrType type) {
		this.className = className;
		this.methodName = null;
		this.message = message;
		this.type = type;
	}
	
	public String toString() {
		return "Class Name: " + this.className + "\n" +
				"Method Name: " + this.methodName + "\n" +
				"Message: " + this.message + "\n" +
				"Error Type: " + this.type;
	}
}
