package example.coupling;

//Expected Class Coupling: 3
public class LowCouplingStaticClass {
	public static void doStuffOnZeroCoupledObject(ZeroCouplingObject obj) {
		obj.setExampleInt(1337);
	}

	public static void doStuffonZeroCoupledStruct(ZeroCouplingDataStruct struct) {
		struct.exampleBoolean = false;
	}

	public static void runZeroCoupledStaticMethod() {
		ZeroCouplingStaticClass.getDoubleNumber();
	}

}
