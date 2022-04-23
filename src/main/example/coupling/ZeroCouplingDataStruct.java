package example.coupling;

//EXPECTED CLASS COUPLING COUNT: 0
public class ZeroCouplingDataStruct {
	// This class is a data structure with purely primitive data types (including
	// Arrays)
	public int exampleInt;
	public boolean exampleBoolean;
	public double exampleDouble;
	public float exampleFloat;
	public byte exampleByte;
	public int[] exampleIntArray;

	public short exampleShort;
	public long exampleLong;
	public char exampleChar;
	public char[] exampleString; // This is how we used to make strings :)

	public int[][] exampleMultiDimensional; // Mainly to ensure multi-dimensional arrays don't cause duplicates
	// No ctor really needed, implicit is fine
}
