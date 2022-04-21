package example.objectadapter;

public class AdapterBothIncorrect extends TargetAbstractClass
								  implements TargetInterface {
	
	// Calls Adaptee methods without Adaptee composition
	
	@Override
	public void doInterestingThing() {
		(new Adaptee()).doSomething();
	}

	@Override
	public String doReallyBoringThing(char[] numberCharacters) {
		String numberString = numberCharacters.toString();
		return (new Adaptee()).doSomethingElse(Integer.parseInt(numberString));
	}

	@Override
	public double doHardThing(int number) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean doReallyCoolThing() {
		(new Adaptee()).doSomething();
		return false;
	}

	@Override
	public String doQuestionableThing(String numberString) {
		return (new Adaptee()).doSomethingElse(Integer.parseInt(numberString));
	}

	@Override
	public void doImpossibleThing() {
		throw new UnsupportedOperationException();
	}
	
}
