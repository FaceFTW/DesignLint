package example.coupling;

//EXPECTED CLASS COUPLING: 3
public class LowCouplingObject {
	// Referencing System.Out is just referencing the System and PrintStream Class
	// Therefore this class has two dependencies to function
	// But we add a string literal and because strings are not primitive in Java,
	// now its 3
	public static void printThings() {
		System.out.println("this actually is coupled to three classes if you think about it");
	}
}
