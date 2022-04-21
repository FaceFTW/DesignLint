package example.template;

public class NotTemplateSubclass extends NotTemplate{
    @Override
    public void notTemplate() {
        test3();
        test1();
        test2();
    }
}
