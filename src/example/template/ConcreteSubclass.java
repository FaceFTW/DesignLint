package example.template;

public abstract class ConcreteSubclass extends AbstractNotTemplate{
    public void innerTestFunc() {
        System.out.println("Inner Func");
    }

    public void implementedAbstract() {
        System.out.println("I'm implemented now!");
    }

    public void notImplementedAbstract(int argument) {
        System.out.println(argument);
    }
}
