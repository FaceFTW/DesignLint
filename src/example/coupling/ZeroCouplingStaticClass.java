package example.coupling;

//EXPECTED CLASS COUPLING COUNT: 0
@SuppressWarnings("unused")
public class ZeroCouplingStaticClass {
	// This class is just static methods that are based on primitive types
	// Therefore there should be no coupling counted in this file

	public static int getNumber() {
		return 0;
	}

	// This should still not require any other classes
	public static void emptyFunction() {
		int i = 0;
		i++;
	}

	public static double getDoubleNumber() {
		return 2.0;
	}

}
