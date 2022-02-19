package example.objectadapter;

public class AdapterInterfaceIncorrectAbstractClassCorrect extends TargetAbstractClass
														   implements TargetInterface {
	// Never calls the Adaptee for Interface - Supports no functionality for it
	private Adaptee adaptee;
	
	public AdapterInterfaceIncorrectAbstractClassCorrect() {
		this.adaptee = new Adaptee();
	}

	@Override
	public boolean doReallyCoolThing() {
		System.out.println("Do nothing...");
		return false;
	}

	@Override
	public String doQuestionableThing(String numberString) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void doImpossibleThing() {
		throw new UnsupportedOperationException();
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
