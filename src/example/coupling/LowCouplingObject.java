package example.coupling;

//EXPECTED CLASS COUPLING: 3
@SuppressWarnings("unused")
public class LowCouplingObject {
	private int noCouplingInt;
	private String coupledString;

	//NOTE System is just referencing the java.lang.System
	public void printThings() {
		System.out.println("this actually is coupled to three classes if you think about it");
	}

	public void parameterMethod(ZeroCouplingObject obj){
		obj.setExampleBoolean(false);
	}


}
