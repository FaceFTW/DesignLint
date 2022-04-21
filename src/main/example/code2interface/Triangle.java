package example.code2interface;

public class Triangle implements Shape, Printable{
    double length;
    double width;

    Triangle(double length, double width) {
        this.length = length;
        this.width = width;
    }

    public Triangle() {
        this.length = 0.0;
        this.width = 0.0;
    }

    public double getArea() {
        return 0.5 * this.length * this.width;
    }

    public void printObject() { System.out.println("I'm a triangle!"); }

    public void uniqueTriangleFunc(int length) { System.out.println("Unique Triangle!" + length); }

    public boolean compareShape(Shape shape) { return true; }

    public boolean compareTriangle(Shape shape) { return true; }
}
