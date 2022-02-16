package example.coupling;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

//This should have 9 project dependencies, 10 total dependencies
@SuppressWarnings("unused")
public class HighCouplingStaticClassProjectCount {

	// Coupling count from this : 3
	public static void makeZeroCouplingObjects() {
		ZeroCouplingDataStruct struct1 = new ZeroCouplingDataStruct();
		ZeroCouplingObject object1 = new ZeroCouplingObject();
		ZeroCouplingStaticClass static1 = new ZeroCouplingStaticClass(); // This is possible, name is a misnomer tbh
	}

	// Coupling Count from this: 7
	public static void makeLowCouplingObjects() {
		LowCouplingDataStruct struct3 = new LowCouplingDataStruct();
		LowCouplingDataStruct2 struct2 = new LowCouplingDataStruct2();
		LowCouplingObject object2 = new LowCouplingObject();
		LowCouplingStaticClass static2 = new LowCouplingStaticClass();
		String string1 = "This adds another coupled class to the mix";
		InputStream stream = new ByteArrayInputStream(new byte[100]);

	}

	// Coupling count from this: 3-1 =2 (ZeroCouplingDataStruct should have been
	// accounted for)
	public static void makeHighCouplingObjects(ZeroCouplingDataStruct struct0) {
		if (struct0.exampleBoolean = true) {
			HighCouplingDataStructProjectCount yikes1 = new HighCouplingDataStructProjectCount();
			HighCouplingDataStructTotalCount yikes2 = new HighCouplingDataStructTotalCount();
		}
	}

}
