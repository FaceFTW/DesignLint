package example.overridestyle;

public class SubclassMultipleOverridesNotUsed extends ConcreteSuperclassExample {
	
	public void doSomethingOne() {
		System.out.println("Doing something different 1...");
	}
	
	public int doSomethingTwo(int x) {
		System.out.println("Doing something 2...");
		return x + x + x;
	}
	
	public String [] doSomethingThree(String a, String b) {
		System.out.println("Doing something different 3...");
		String [] arr = {b, a};
		return arr;
	}
}
