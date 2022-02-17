package example.coupling;

import java.io.PrintStream;

@SuppressWarnings({ "unused", "deprecation" })
public class CoupledToInterfaceExample implements CouplingInterfaceExample {

	@Override
	public ZeroCouplingDataStruct exampleMethodToImplement() {
		Integer nonPrimitiveInt = new Integer("nuts");
		return new ZeroCouplingDataStruct();
	}

	@Override
	public void exampleMethodToImplementWithParams(String string) {
		PrintStream stream = System.out;
		stream.println(string);

	}

}
