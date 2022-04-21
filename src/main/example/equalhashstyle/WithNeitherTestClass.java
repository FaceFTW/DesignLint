package example.equalhashstyle;

import java.util.ArrayList;
import java.util.List;

public class WithNeitherTestClass {
    
    List<String> cats;
    public WithNeitherTestClass(){
        this.cats = new ArrayList<>();
    }
    public String getFirstCat(){
        return cats.get(0);
    }
    public List<String> getAllCats(){
        return cats;
    }
}
