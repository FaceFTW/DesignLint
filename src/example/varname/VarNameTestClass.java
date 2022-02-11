package example.varname;

public class VarNameTestClass {
    private int testVariable;
    public char _underscoreField;
    public char $dollarField;
    private String BadVariable;
    private double reallyLongFieldForTestingPurposes;
    protected long fV;
    private static final int GLOBAL_VARIABLE = 0;
    public static final int bad_Global = 1;
    public static final int $DOLLAR_GLOBAL = 10;
    public static final int _UNDERSCORE_GLOBAL = 1;
    public static int BG = 2;

    VarNameTestClass(int testVariable, int superLongUnnecessaryVariableName) {
        this.testVariable = testVariable;
    }

    public int methodName(int _x, int $y) {
        int Z;
        Z = _x + $y;
        return Z;
    }
}
