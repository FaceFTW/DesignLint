package datasource;

public class MethodCall {
	String calledMethodName;
	Invoker invokerStatus;
	String invokerName;
	
	public MethodCall(String calledMethodName, Invoker invokerStatus, String invokerName) {
		this.calledMethodName = calledMethodName;
		this.invokerStatus = invokerStatus;
		this.invokerName = invokerName;
	}
}
