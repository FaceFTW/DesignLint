package example.equalhashstyle;

import java.util.ArrayList;
import java.util.List;

public class WithEqualsOnlyTestClass {
    List<String> cats;

    public WithEqualsOnlyTestClass(){
        this.cats = new ArrayList<>();
    }
    public String getFirstCat(){
        return cats.get(0);
    }
    public List<String> getAllCats(){
        return cats;
    }
    @Override
    public boolean equals(Object o){
        if (cats == o){
            return true;
        }
        return false;
    }
    
}
