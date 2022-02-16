package example.code2interface;

import java.util.*;

public class Code2InterfaceTest {
//    public boolean boolTest;
//    public char charTest;
//    public byte byteTest;
//    public short shortTest;
//    public int intTest;
//    public float floatTest;
//    public long longTest;
//    public double doubleTest;
//    public int[] intArrTest;
//    public Triangle[] triArrTest;
//    public String stringTest;
//    public ArrayList<Integer> listTest;
//    public HashMap<Integer, String> mapTest;
//    public HashSet<Double> setTest;
    public Triangle triangleTest;
    public Shape shapeTest;

    public void testFunc() {
        Triangle tri1 = new Triangle(1.0, 1.0);
        Triangle tri2 = new Triangle(2.0, 2.0);
        Triangle tri3 = new Triangle(3.0, 3.0);

        double special = tri1.getArea();
        tri2.printObject();
        tri3.uniqueTriangleFunc();
    }
}


