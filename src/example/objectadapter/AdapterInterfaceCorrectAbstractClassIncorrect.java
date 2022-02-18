package example.objectadapter;

public class AdapterInterfaceCorrectAbstractClassIncorrect extends TargetAbstractClass
														   implements TargetInterface {
	
	// Never calls the Adaptee for Abstract Class - Supports no functionality for it
	private Adaptee adaptee;
	
	public AdapterInterfaceCorrectAbstractClassIncorrect() {
		this.adaptee = new Adaptee();
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

	@Override
	public void doInterestingThing() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String doReallyBoringThing(char[] numberCharacters) {
		return null;
	}

	@Override
	public double doHardThing(int number) {
		throw new UnsupportedOperationException();
	}

}
