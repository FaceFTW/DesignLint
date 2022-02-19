package example.code2interface;

import java.util.*;

public class Code2InterfaceTest {
    public ArrayList<String> fieldListTest;
    public HashMap<String, String> fieldMapTest;
    public HashSet<Double> fieldSetTest;
    public Triangle triangleTest;
    public Triangle shapeTest;

    public void testFunc() {
        Triangle tri1 = new Triangle(1.0, 1.0);
        Triangle tri2 = new Triangle(2.0, 2.0);
        Triangle tri3 = new Triangle(3.0, 3.0);
        Triangle tri4 = new Triangle(4.0, 4.0);
        Triangle tri5 = new Triangle(5.0, 5.0);
        Shape tri6 = new Triangle(6.0, 6.0);

        tri1.getArea();
        tri2.printObject();
        tri3.getArea();
        tri4.uniqueTriangleFunc(1);
        tri5.compareTriangle(this.triangleTest);
        tri6.getArea();

        tri3.compareShape(this.triangleTest);

        tri4.compareTriangle(tri1);
    }

    public void anotherFunc() {
        ArrayList<String> testList = new ArrayList<>();
        testList.get(0);

//        ArrayList<Triangle> innerList = new ArrayList<>();
//        for (int i = 0; i < innerList.size(); i++){
//            System.out.println(innerList.get(i));
//        }


        HashMap<String, String> mapTest = new HashMap<>();
        mapTest.put("1", "2");

        HashSet<String> setTest = new HashSet<>();
        setTest.add("1");
    }
}


