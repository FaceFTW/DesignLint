package example.coupling;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentLinkedDeque;

@SuppressWarnings("unused")
public class HighCouplingNightmareClass {
	// I swear this is just an example
	// This has mentally scarred me with the amount of random BS
	// I had to write to generate this for testing :(
	List<String> list1;
	ArrayList<String> list2;
	Stack<Double> list3;
	Queue<Long> list4;
	Map<String, String> map1;
	Set<Character> list5;

	public HighCouplingNightmareClass(ZeroCouplingDataStruct struct0) {
		list1 = new LinkedList<>();
		list2 = new ArrayList<>();
		list3 = new Stack<>();
		list4 = new ConcurrentLinkedDeque<>(); // sure why not?????
		list5 = new TreeSet<>();
		map1 = new HashMap<>();
	}

	private void nightmareCouplingMethod(LowCouplingDataStruct struct0, LowCouplingObject obj1,
			ZeroCouplingObject[] objArr) {
		// screw it make some static calls
		ZeroCouplingStaticClass.emptyFunction();
		LowCouplingStaticClass.doStuffOnZeroCoupledObject(objArr[0]);

	}

	// Man it'd be real funny if we ran this method
	private int funny(Integer funnyInt) {
		return this.funny(funnyInt.intValue() + 1);
	}

	public static void invokeStaticLowCouplingStaticMethod() {
		LowCouplingStaticClass.doStuffOnZeroCoupledObject(new ZeroCouplingObject());
	}

	public static void invokeZeroCouplingStaticMethod() {
		ZeroCouplingStaticClass.getDoubleNumber();
		ZeroCouplingStaticClass.emptyFunction();
	}

	public static Random makeANewRandomIdk() {
		return new Random();
	}

	public static List<String> makeANewScannerIdk() {
		return new ArrayList<String>();
	}

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

	public static void printStuff(PrintStream stream) {
		stream.print("this is getting out of hand");
	}

	// You know its bad design when I've gotten this far down and I can't figure out
	// how to make it worse

	public static void aBunchOfStaticPrimitiveWrapperCalls() {
		Integer.parseInt("21");
		Double.parseDouble("00.15");
		Character.toUpperCase('l');
		Byte.valueOf((byte) 0b00);
		Long.valueOf(5l);
		Short.parseShort("3");
	}
}
