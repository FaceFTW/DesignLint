package example.objectadapter;

public class AdapterAdaptsInterface implements TargetInterface {
	
	private InterfaceAdaptee adaptee;
	
	public AdapterAdaptsInterface() {
		this.adaptee = new InterfaceAdapteeConcrete();
	}
	
	@Override
	public boolean doReallyCoolThing() {
		this.adaptee.doSomethingAmazing();
		return false;
	}

	@Override
	public String doQuestionableThing(String numberString) {
		return this.adaptee.doSomethingIncredible(Integer.parseInt(numberString) > 5);
	}

	@Override
	public void doImpossibleThing() {
		throw new UnsupportedOperationException();
	}

}
