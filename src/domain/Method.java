package domain;

import java.util.List;

import datasource.MethodCall;

public class Method {
	private String name;
	private List<MethodCall> methodCalls;
	
	public Method(String name, List<MethodCall> methodCalls) {
		this.name = name;
		this.methodCalls = methodCalls;
	}
	
	public String getName() {
		return this.name;
	}
	
	public List<MethodCall> getMethodCalls() {
		return this.methodCalls;
	}
}
