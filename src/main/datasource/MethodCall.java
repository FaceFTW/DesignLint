package datasource;

public class MethodCall {
	private String calledMethodName;
	private Invoker invokerStatus;
	private String invokerName;
	private String invokedClass;
	
	public MethodCall(String calledMethodName, Invoker invokerStatus, String invokerName, String invokedClass) {
		this.calledMethodName = calledMethodName;
		this.invokerStatus = invokerStatus;
		this.invokerName = invokerName;
		this.invokedClass = invokedClass;
		
	}
	
	public Invoker getInvoker() {
		return this.invokerStatus;
	}
	
	public String getCalledMethodName() {
		return this.calledMethodName;
	}
	
	public String getInvokerName() {
		return this.invokerName;
	}
	
	public String getInvokedClass() {
		return this.invokedClass;
	}

	public String toString() {
		return "Called Method Name: " + this.calledMethodName + "\n" +
				"Invoker: " + this.invokerStatus + "\n" +
				"InvokerName: " + this.invokerName + "\n" +
				"InvokerClass: " + this.invokedClass;
	}
}
