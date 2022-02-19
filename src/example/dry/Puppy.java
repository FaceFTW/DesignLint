package example.dry;

public class Puppy {
    String name;
    boolean playing;
    public Puppy(String name){
        this.name = name;
        this.playing = false;
    }
    public void play(){
        this.playing = true;

    }
}
