package example.coupling;

public class HighCouplingDataStructProjectCount {
	ZeroCouplingDataStruct struct0;
	ZeroCouplingObject obj0;
	ZeroCouplingStaticClass static0;

	LowCouplingDataStruct struct1;
	LowCouplingDataStruct2 struct2;
	LowCouplingObject obj1;
	LowCouplingStaticClass static1;
	// Wow, at this point we already have 7 project dependencies

	HighCouplingObjectTotalCount struct3;
	HighCouplingObjectProjectCount obj2;
	//And this should be enough to trigger a warning
}
