package example.coupling;

//EXPECTED CLASS COUPLING: 4
@SuppressWarnings("unused")
public class LowCouplingObject {
	private int noCouplingInt;
	private String coupledString;

	// Referencing System.Out is just referencing the System and PrintStream Class
	// Therefore this class has two dependencies to function
	// But we add a string literal and because strings are not primitive in Java,
	// now its 3
	public void printThings() {
		System.out.println("this actually is coupled to three classes if you think about it");
	}

	public void parameterMethod(ZeroCouplingObject obj){
		obj.setExampleBoolean(false);
	}


}
