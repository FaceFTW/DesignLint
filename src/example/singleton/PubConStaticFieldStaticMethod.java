package example.singleton;

public class PubConStaticFieldStaticMethod {
    public String cat;
    private static PubConStaticFieldStaticMethod instt = new PubConStaticFieldStaticMethod();
    public PubConStaticFieldStaticMethod(){
        cat = "Turbo!";
    }

    public String getCat(){
        return instt.getCat();
    }
    public static PubConStaticFieldStaticMethod getInstance(){
        return instt;
    }

}
