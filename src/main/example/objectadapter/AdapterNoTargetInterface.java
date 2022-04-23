package example.objectadapter;

public class AdapterNoTargetInterface {
	
	private Adaptee adaptee;
	
	public AdapterNoTargetInterface() {
		this.adaptee = new Adaptee();
	}

	public boolean doReallyCoolThing() {
		this.adaptee.doSomething();
		return false;
	}

	public String doQuestionableThing(String numberString) {
		return this.adaptee.doSomethingElse(Integer.parseInt(numberString));
	}

	public void doImpossibleThing() {
		throw new UnsupportedOperationException();
	}
}
