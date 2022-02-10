package example.overridestyle;

public class ConcreteSuperclassExample {
	
	public void doSomethingOne() {
		System.out.println("Doing something 1...");
	}
	
	public int doSomethingTwo(int x) {
		System.out.println("Doing something 2...");
		return x + x;
	}
	
	public String [] doSomethingThree(String a, String b) {
		System.out.println("Doing something 3...");
		String [] arr = {a, b};
		return arr;
	}
}
