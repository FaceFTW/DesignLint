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
        String[] classList = { "example/dry/CatExtendsAnimal", "example/dry/DogExtendsAnimal"};
        this.analyzer = new DryAnalyzer(classList);
        ReturnType returned = this.analyzer.getFeedback(classList);

        assertEquals("DryAnalyzer", returned.analyzerName);
        assertEquals(0, returned.errorsCaught.size());
    }
    @Test
    public void oneClassWithDuplicationTest(){
        //dog - should give two errors, one error for walk and one for run methods
        String[] classList = { "example/dry/Dog" };
        this.analyzer = new DryAnalyzer(classList);
        ReturnType returned = this.analyzer.getFeedback(classList);

        assertEquals("DryAnalyzer", returned.analyzerName);
        assertEquals(2, returned.errorsCaught.size());
        assertEquals("example/dry/Dog" , returned.errorsCaught.get(0).className);
        assertEquals("example/dry/Dog" , returned.errorsCaught.get(1).className);
        assertEquals(ErrType.WARNING , returned.errorsCaught.get(0).type);
        assertEquals(ErrType.WARNING , returned.errorsCaught.get(1).type);
        
        
    }
    @Test
    public void twoClassWithDuplicationTest(){
        // cat and dog -  should give 4 errors, one error for walk and one for run methods, one for each eat methods
        String[] classList = { "example/dry/Cat", "example/dry/Dog"};
        this.analyzer = new DryAnalyzer(classList);
        ReturnType returned = this.analyzer.getFeedback(classList);

        assertEquals("DryAnalyzer", returned.analyzerName);
        assertEquals(4, returned.errorsCaught.size());
        assertEquals("example/dry/Cat" , returned.errorsCaught.get(0).className);
        assertEquals("example/dry/Dog" , returned.errorsCaught.get(1).className);
        assertEquals("example/dry/Dog" , returned.errorsCaught.get(2).className);
        assertEquals("example/dry/Dog" , returned.errorsCaught.get(3).className);
        
        assertEquals(ErrType.WARNING , returned.errorsCaught.get(0).type);
        assertEquals(ErrType.WARNING , returned.errorsCaught.get(1).type);
        assertEquals(ErrType.WARNING , returned.errorsCaught.get(2).type);
        assertEquals(ErrType.WARNING , returned.errorsCaught.get(3).type);

    }
    @Test 
    public void allClassesTest(){
        // - should give 4 errors, one error for walk and one for run methods, one for each eat methods
        String[] classList = { "example/dry/Cat", "example/dry/Dog", "example/dry/CatExtendsAnimal", "example/dry/DogExtendsAnimal" };
        this.analyzer = new DryAnalyzer(classList);
        ReturnType returned = this.analyzer.getFeedback(classList);

        assertEquals("DryAnalyzer", returned.analyzerName);
        assertEquals(4, returned.errorsCaught.size());
        assertEquals("example/dry/Cat" , returned.errorsCaught.get(0).className);
        assertEquals("example/dry/Dog" , returned.errorsCaught.get(1).className);
        assertEquals("example/dry/Dog" , returned.errorsCaught.get(2).className);
        assertEquals("example/dry/Dog" , returned.errorsCaught.get(3).className);
        
        assertEquals(ErrType.WARNING , returned.errorsCaught.get(0).type);
        assertEquals(ErrType.WARNING , returned.errorsCaught.get(1).type);
        assertEquals(ErrType.WARNING , returned.errorsCaught.get(2).type);
        assertEquals(ErrType.WARNING , returned.errorsCaught.get(3).type);
    }

}
