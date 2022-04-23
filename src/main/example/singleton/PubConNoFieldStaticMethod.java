package example.singleton;

public class PubConNoFieldStaticMethod {
    public String cat;
    public PubConNoFieldStaticMethod(){
        cat = "Turbo!";
    }


    public String getCat(){
        return this.cat;
    }
    public static PubConNoFieldStaticMethod getInstance(){
        return null;
    }
}
