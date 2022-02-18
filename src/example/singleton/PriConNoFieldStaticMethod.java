package example.singleton;

public class PriConNoFieldStaticMethod {
    
    public String cat;
    private PriConNoFieldStaticMethod(){
        cat = "Turbo!";
    }


    public String getCat(){
        return this.cat;
    }
    public static PriConNoFieldStaticMethod getInstance(){
        return null;
    }
}
