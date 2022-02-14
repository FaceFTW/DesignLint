package example.coupling;

//EXPECTED CLASS COUPLING: 0
public class ZeroCouplingObject {
	// Another example of a class that only uses primitives and methods, therefore
	// no coupling should be here
	private int exampleInt;

	public int getExampleInt() {
		return exampleInt;
	}

	public void setExampleInt(int exampleInt) {
		this.exampleInt = exampleInt;
	}

	public boolean isExampleBoolean() {
		return exampleBoolean;
	}

	public void setExampleBoolean(boolean exampleBoolean) {
		this.exampleBoolean = exampleBoolean;
	}

	private boolean exampleBoolean;

}
