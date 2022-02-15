package example.coupling;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;
import java.util.Map.Entry;

//EXPECTED CLASS COUPLING: 20
@SuppressWarnings("unused")
public class HighCouplingDataStructTotalCount {
	// THis is just an absurd datastructre that uses soooo many types

	// This line is just an atrocity, but it is intentional
	// I don't think there will ever be an example that uses this much generic BS
	// BTW this counts as 6 separate classes
	public HashMap<ArrayList<String>, Queue<Stack<PrintStream>>> dumbstructure;

	// This one is also really dumb, but not as dumb for using interface types
	// We *are* using 6 more types
	public Map<List<Double>, Entry<Integer, Long>> dumbstructure2;

	public LowCouplingDataStruct struct1;
	public LowCouplingDataStruct2 struct2;
	public LowCouplingObject object1;
	public ZeroCouplingDataStruct zero;
	public ZeroCouplingObject one;

	public static void staticCoupledMethod() {
		double dummy = ZeroCouplingStaticClass.getDoubleNumber();
	}

	public static void parameterCoupledMethod(PrintStream stream) {
		stream.println("At least I'm using an abstraction, not a concrete class (I think)");
	}

	// Ironically, this should not contribute to the coupling count because it
	// depends on it self and other existing dependencies
	public static void selfDependentMethod() {
		HighCouplingDataStructTotalCount.staticCoupledMethod();
	}

	public static void parameterCoupledMethod2(Random rand) {
		// Honestly, this is just a stub that also doesn't really do anything
		rand.nextInt();
	}

}
