package example.template;

public abstract class CaffeineBeverage {
    final void prepareRecipe() {
        boilWater();
        brew();
        pourInCup();
        addCondiments();
    }

    final void notQuiteTemplate() {
        boilWater();
        pourInCup();
        //Congrats on your hot water.
    }

    abstract void brew();
    abstract void addCondiments();

    public void boilWater() {
        System.out.println("Boiling water");
    }

    public void pourInCup() {
        System.out.println("Pouring in cup");
    }
}
