package example.dry;

public class Cat {
    
    String name;
    public Cat(String name){
        this.name = name;
    }
    public void eatFood(){
        System.out.println(this.name + " is eating kibble!");
    }
    public void meow(){
        System.out.println(this.name + "is meowing!");
    }
}
