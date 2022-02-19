package example.dry;

public class Dog {
    
    String name;
    boolean walking;
    public Dog(String name){
        this.name = name;
        this.walking = false;
    }
    public void eatFood(){
        System.out.println(this.name + " is eating kibble!");
    }

    public void bark(){
        System.out.println(this.name + "is barking!");
    }
    public void run(){
        this.walking = true;
    }
    public void walk(){
        this.walking = true;
    }
}
