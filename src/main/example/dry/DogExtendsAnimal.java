package example.dry;

public class DogExtendsAnimal extends Animal {
    public DogExtendsAnimal(String name){
        this.name = name;
    }
    public void bark(){
        System.out.println(this.name + "is barking!");
    }
}
