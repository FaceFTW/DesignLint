package example.objectadapter;

public class AdapterAdaptsAbstractClass extends TargetAbstractClass {
	
	private AbstractAdaptee adaptee;
	
	public AdapterAdaptsAbstractClass() {
		this.adaptee = new AbstractAdapteeConcrete();
	}

	@Override
	public void doInterestingThing() {
		this.adaptee.doSomethingMundane();
	}

	@Override
	public String doReallyBoringThing(char[] numberCharacters) {
		throw new UnsupportedOperationException();
	}

	@Override
	public double doHardThing(int number) {
		this.adaptee.doSomethingMundane();
		return 999;
	}
	
}
