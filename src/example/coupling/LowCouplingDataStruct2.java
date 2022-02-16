package example.coupling;

import java.util.List;

//EXPECTED CLASS COUPLING COUNT: 3
public class LowCouplingDataStruct2 {
	// This class should have low coupling like the first version
	// But we now add references in the examples folder for testing
	public LowCouplingDataStruct lowCouplingDataStruct;
	public ZeroCouplingDataStruct zeroCouplingDataStruct;

	public List<LowCouplingDataStruct> lowCouplingDataStructList;
	public List<ZeroCouplingDataStruct> zeroCouplingDataStructList;

	//Implicit Ctor also fine here
}
