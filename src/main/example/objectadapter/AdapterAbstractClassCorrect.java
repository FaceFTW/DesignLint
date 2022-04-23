package example.objectadapter;

public class AdapterAbstractClassCorrect extends TargetAbstractClass {
	
	private Adaptee adaptee;
	
	public AdapterAbstractClassCorrect() {
		this.adaptee = new Adaptee();
	}
	
	@Override
	public void doInterestingThing() {
		this.adaptee.doSomething();
	}

	@Override
	public String doReallyBoringThing(char[] numberCharacters) {
		String numberString = numberCharacters.toString();
		return this.adaptee.doSomethingElse(Integer.parseInt(numberString));
	}

	@Override
	public double doHardThing(int number) {
		throw new UnsupportedOperationException();
	}

}
