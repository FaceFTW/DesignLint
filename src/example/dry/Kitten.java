package example.dry;

public class Kitten {
    String name;
    public Kitten(String name){
        this.name = name;
    }
    public void play(){
        System.out.println(this.name + " is playing with a yarnball!");
    }
}
