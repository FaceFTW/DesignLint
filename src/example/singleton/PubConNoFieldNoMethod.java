package example.singleton;

public class PubConNoFieldNoMethod {
    public String cat;
    public PubConNoFieldNoMethod(){
        cat = "Turbo!";
    }

    public String getCat(){
        return this.cat;
    }
}
