import static org.junit.Assert.*;

import org.junit.Test;

import domain.ErrType;
import domain.ReturnType;
import domain.dryAnalyzer;

public class dryTest {
    private dryAnalyzer analyzer;

    @Test
    public void oneClassNoDuplicationTest(){
        //cat - shouldnt give an error
    }
    @Test
    public void twoClassNoDuplicationTest(){
        //cat and dog with interfaces - shouldn't give an error
    }
    @Test
    public void oneClassWithDuplicationTest(){
        //dog - should give two errors, one error for walk and one for run methods
    }
    @Test
    public void twoClassWithDuplicationTest(){
        // cat and dog -  should give 4 errors, one error for walk and one for run methods, one for each eat methods
        
    }
    @Test 
    public void allClassesTest(){
        // - should give 4 errors, one error for walk and one for run methods, one for each eat methods
    }

}
