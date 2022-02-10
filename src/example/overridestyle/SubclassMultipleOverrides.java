package example.overridestyle;

public class SubclassMultipleOverrides extends ConcreteSuperclassExample {
	
	@Override
	public void doSomethingOne() {
		System.out.println("Doing something different 1...");
	}
	
	@Override
	public int doSomethingTwo(int x) {
		System.out.println("Doing something 2...");
		return x + x + x;
	}
	
	@Override
	public String [] doSomethingThree(String a, String b) {
		System.out.println("Doing something different 3...");
		String [] arr = {b, a};
		return arr;
	}
}
