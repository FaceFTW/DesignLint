package example.coupling;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

//20
@SuppressWarnings("unused")
public class HighCouplingStaticClassTotalCount {
	// Basically a copy of a couple of methods from the other
	// HighCouplingStaticClass
	// And some new ones using JRE stuff

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
