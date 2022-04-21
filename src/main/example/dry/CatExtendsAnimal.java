package example.dry;

public class CatExtendsAnimal extends Animal{
    public CatExtendsAnimal(String name){
        this.name = name;
    }
    public void meow(){
        System.out.println(this.name + "is meowing!");
    }
}
