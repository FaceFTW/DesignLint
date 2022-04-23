package example.equalhashstyle;

import java.util.ArrayList;
import java.util.List;

public class WithHashCodeOnlyTestClass {
    List<String> cats;

    public WithHashCodeOnlyTestClass(){
        this.cats = new ArrayList<>();
    }
    public String getFirstCat(){
        return cats.get(0);
    }
    public List<String> getAllCats(){
        return cats;
    }
    @Override
    public int hashCode(){
        return cats.size();
    }
}
