package example.singleton;

public class PriConNoFieldNoMethod {
    
    public String cat;
    private PriConNoFieldNoMethod(){
        cat = "Turbo!";
    }

    public String getCat(){
        return this.cat;
    }
}
