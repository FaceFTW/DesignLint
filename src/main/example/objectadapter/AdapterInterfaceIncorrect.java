package example.objectadapter;

@SuppressWarnings("unused")
public class AdapterInterfaceIncorrect implements TargetInterface {
	
	// Never calls the Adaptee - Supports no functionality
	private Adaptee adaptee;
	
	public AdapterInterfaceIncorrect() {
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
	
	
}
