package example.objectadapter;

public class AdapterBothCorrect extends TargetAbstractClass
								implements TargetInterface {
	
	private Adaptee adaptee;
	
	public AdapterBothCorrect() {
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
	
	@Override
	public boolean doReallyCoolThing() {
		this.adaptee.doSomething();
		return false;
	}

	@Override
	public String doQuestionableThing(String numberString) {
		return this.adaptee.doSomethingElse(Integer.parseInt(numberString));
	}

	@Override
	public void doImpossibleThing() {
		throw new UnsupportedOperationException();
	}

}
