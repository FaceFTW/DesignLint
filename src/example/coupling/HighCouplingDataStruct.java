package example.coupling;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.Map.Entry;

//EXPECTED CLASS COUPLING: 
public class HighCouplingDataStruct {
	// THis is just an absurd datastructre that uses soooo many types

	// This line is just an atrocity, but it is intentional
	// I don't think there will ever be an example that uses this much generic BS
	// BTW this counts as 6 separate classes
	public HashMap<ArrayList<String>, Queue<Stack<PrintStream>>> dumbstructure;
	
	//This one is also really dumb, but not as dumb for using interface types
	//We *are* using 
	public Map<List<Double>, Entry<Integer, Long>> dumbstructure2;
}
