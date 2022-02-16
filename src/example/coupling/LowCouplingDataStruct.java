package example.coupling;

import java.util.ArrayList;
import java.util.List;

//EXPECTED COUPLING COUNT: 2+1
public class LowCouplingDataStruct {
	// This will be a simple data structure that should have effectively low
	// coupling
	// The coupling will only be on other data structures (such as java.util.List)

	public List<String> example1;
	public String exampleString; // String is not a primitive, so it counts toward the coupling
	public int examplePrimitive;
	public boolean examplePrimitive2;

	public LowCouplingDataStruct() {
		example1 = new ArrayList<>(); // Technically speaking, we should include this in the count
	}
}
