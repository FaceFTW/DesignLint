package example.singleton;

public class PriConStaticFieldNoMethodOtherStaticMethods {
    public String cat;
    private static PriConStaticFieldNoMethodOtherStaticMethods instt = new PriConStaticFieldNoMethodOtherStaticMethods();
    private PriConStaticFieldNoMethodOtherStaticMethods(){
        cat = "Turbo!";
    }

    public String getCat(){
        return instt.getCat();
    }

    public static String getCatToy(){
        return "Cat Toy";
    }

}
