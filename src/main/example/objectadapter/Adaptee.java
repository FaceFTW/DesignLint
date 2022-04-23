package example.objectadapter;

public class Adaptee {
	
	public void doSomething() {
		System.out.println("Adaptee doing something...");
	}
	
	public String doSomethingElse(int x) {
		return "" + x;
	}
}
