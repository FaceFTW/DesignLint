package example.coupling;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Map.Entry;

//EXPECTED CLASS COUPLING: 21 (because funny)
@SuppressWarnings("unused")
public class HighCouplingObjectTotalCount {
	// THis is just an absurd object that uses soooo many types

	public HashMap<String, String> dumbstructure;
	public Map<String, String> dumbstructure2;
	public List<String> dumbList;
	public ArrayList<String> dumbList2;
	public Stack<String> dumbStack;
	public Queue<String> dumbQueue;
	public Set<String> dumbSet;
	public Scanner dumbScannerThatIWillNeverActuallyUse;
	public StringBuilder dumbStringBuilder;
	public TreeSet<String> dumbSet2;
	public LinkedList<String> dumbList3;
	String dumbString;
	Integer dumbStaticInteger;

	ZeroCouplingDataStruct struct0;
	ZeroCouplingObject obj0;

	LowCouplingDataStruct struct1;
	LowCouplingDataStruct2 struct2;
	LowCouplingObject obj1;
	LowCouplingStaticClass static1;

	public static void staticCoupledMethod() {
		double dummy = ZeroCouplingStaticClass.getDoubleNumber();
	}

	public static void parameterCoupledMethod(PrintStream stream) {
		stream.println("At least I'm using an abstraction, not a concrete class (I think)");
	}

	// Ironically, this should not contribute to the coupling count because it
	// depends on it self and other existing dependencies
	public static void selfDependentMethod() {
		HighCouplingObjectTotalCount.staticCoupledMethod();
	}

	public static void parameterCoupledMethod2(Random rand) {
		// Honestly, this is just a stub that also doesn't really do anything
		rand.nextInt();
	}

}
