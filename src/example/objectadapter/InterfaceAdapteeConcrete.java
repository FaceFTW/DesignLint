package example.objectadapter;

public class InterfaceAdapteeConcrete implements InterfaceAdaptee {

	@Override
	public void doSomethingAmazing() {
		System.out.println("Something Amazing");
	}

	@Override
	public String doSomethingIncredible(boolean x) {
		return Boolean.toString(x);
	}

}
