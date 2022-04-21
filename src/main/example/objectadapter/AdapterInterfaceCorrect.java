package example.objectadapter;

public class AdapterInterfaceCorrect implements TargetInterface {
	
	private Adaptee adaptee;
	
	public AdapterInterfaceCorrect() {
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
}
