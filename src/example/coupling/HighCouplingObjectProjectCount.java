package example.coupling;

public class HighCouplingObjectProjectCount {

	ZeroCouplingDataStruct struct0;
	ZeroCouplingObject obj0;

	LowCouplingDataStruct struct1;
	LowCouplingDataStruct2 struct2;

	public HighCouplingObjectProjectCount() {
		ZeroCouplingStaticClass.emptyFunction();
	}

	public HighCouplingObjectProjectCount makeHighCouplingObjectClass() {
		return new HighCouplingObjectProjectCount();
	}

	public HighCouplingNightmareClass makeNightmareClass() {
		return new HighCouplingNightmareClass(struct0);
	}

	public HighCouplingDataStructTotalCount makeHighCouplingDataStructTotalCount() {
		return new HighCouplingDataStructTotalCount();
	}

	public HighCouplingObjectTotalCount makeHighCouplingObjectTotalCount() {
		return new HighCouplingObjectTotalCount();
	}
}
