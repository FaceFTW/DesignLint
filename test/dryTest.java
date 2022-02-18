import static org.junit.Assert.*;

import org.junit.Test;

import domain.ErrType;
import domain.ReturnType;
import domain.DryAnalyzer;

public class DryTest {
    private DryAnalyzer analyzer;

    @Test
    public void oneClassNoDuplicationTest(){
        //cat - shouldnt give an error
        String[] classList = { "example/dry/Cat" };
        this.analyzer = new DryAnalyzer(classList);
        ReturnType returned = this.analyzer.getFeedback(classList);

        assertEquals("DryAnalyzer", returned.analyzerName);
        assertEquals(0, returned.errorsCaught.size());
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
