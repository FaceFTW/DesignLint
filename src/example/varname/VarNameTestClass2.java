package example.varname;

public class VarNameTestClass2 {
    private String $BadVariable;
    private static final int GLOBAL_VARIABLE = 0;


    VarNameTestClass2(String testVariable) {
        $BadVariable = testVariable;
    }

    public int methodName(int a, int b, int C) {
        int d;
        d = a + b + C;
        return d;
    }
}

