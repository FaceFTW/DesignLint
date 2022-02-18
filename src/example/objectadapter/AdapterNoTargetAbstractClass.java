package example.objectadapter;

public class AdapterNoTargetAbstractClass {
	
	private Adaptee adaptee;
	
	public AdapterNoTargetAbstractClass() {
		this.adaptee = new Adaptee();
	}
	
	public void doInterestingThing() {
		this.adaptee.doSomething();
	}

	public String doReallyBoringThing(char[] numberCharacters) {
		String numberString = numberCharacters.toString();
		return this.adaptee.doSomethingElse(Integer.parseInt(numberString));
	}

	public double doHardThing(int number) {
		throw new UnsupportedOperationException();
	}
}
