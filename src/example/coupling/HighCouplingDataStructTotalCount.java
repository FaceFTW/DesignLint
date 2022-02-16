package example.coupling;

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


public class HighCouplingDataStructTotalCount {
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
	public Random dumbRandom;

	ZeroCouplingDataStruct struct0;
	ZeroCouplingObject obj0;

	LowCouplingDataStruct struct1;
	LowCouplingDataStruct2 struct2;
	LowCouplingObject obj1;
	LowCouplingStaticClass static1;
}
