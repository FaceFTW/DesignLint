package example.template;

public abstract class Template1Abstract {
    public void testAbstract() {
        specialTest();
        concreteTest();
    }

    public abstract void specialTest();

    public void concreteTest() {
        System.out.println("This is a concrete Test");
    }
}
