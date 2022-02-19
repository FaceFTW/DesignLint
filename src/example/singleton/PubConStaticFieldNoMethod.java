package example.singleton;

public class PubConStaticFieldNoMethod {
    
    
    public String cat;
    private static PubConStaticFieldNoMethod instt = new PubConStaticFieldNoMethod();
    public PubConStaticFieldNoMethod(){
        cat = "Turbo!";
    }

    public String getCat(){
        return instt.getCat();
    }
}
