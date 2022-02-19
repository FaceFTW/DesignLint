package example.template;

public abstract class AbstractNotTemplate {
    public void testFunc() {
        innerTestFunc();
        concreteMethod();
        implementedAbstract();
        notImplementedAbstract();
    }

    public abstract void innerTestFunc();

    public void concreteMethod() {
        System.out.println("I'm concrete!");
    }

    public abstract void implementedAbstract();

    public abstract void notImplementedAbstract();
}
