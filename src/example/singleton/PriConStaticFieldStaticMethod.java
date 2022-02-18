package example.singleton;

public class PriConStaticFieldStaticMethod {
    
    
    public String cat;
    private static PriConStaticFieldStaticMethod instt = new PriConStaticFieldStaticMethod();
    private PriConStaticFieldStaticMethod(){
        cat = "Turbo!";
    }

    public String getCat(){
        return instt.getCat();
    }
    public static PriConStaticFieldStaticMethod getInstance(){
        return instt;
    }

}
