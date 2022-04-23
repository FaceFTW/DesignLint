package example.objectadapter;

public class AdapterAbstractClassIncorrect extends TargetAbstractClass {
	
	// Missing composition of Adaptee
	
	@Override
	public void doInterestingThing() {
		System.out.println("Nope");
	}

	@Override
	public String doReallyBoringThing(char[] numberCharacters) {
		String numberString = numberCharacters.toString();
		numberString.concat("nope");
		return numberString;
	}

	@Override
	public double doHardThing(int number) {
		throw new UnsupportedOperationException();
	}
}
