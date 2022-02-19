package example.template;

public abstract class AbstractSubclass extends AbstractNotTemplate{
    public void extraFunc() {
        innerTestFunc();
        concreteMethod();
        notImplementedAbstract();
    }
}
