package example.singleton;

public class PriConStaticFieldNoMethod {

    public String cat;
    private static PriConStaticFieldNoMethod instt = new PriConStaticFieldNoMethod();
    private PriConStaticFieldNoMethod(){
        cat = "Turbo!";
    }

    public String getCat(){
        return instt.getCat();
    }
}
