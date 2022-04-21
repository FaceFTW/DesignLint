package example.dry;

public abstract class Animal {
    String name;

    public void eatFood(){
        System.out.println(this.name + "is eating kibble!");
    }
}
