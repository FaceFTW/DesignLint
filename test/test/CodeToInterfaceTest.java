package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import domain.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class CodeToInterfaceTest {
    private final String[] ourClass = { "example/code2Interface/code2InterfaceTest" };

    //Analyzer Class
    private CodeToInterfaceAnalyzer analyzer;

    //Instantiate the Analyzer Class
    public void setUpOurClass() { this.analyzer = new CodeToInterfaceAnalyzer(ourClass); }

    //Given no classes, all fields within the analyzer should be blank, and return no errors.
    @Test
    public void testFieldsGivenNoClasses() {
        this.analyzer = new CodeToInterfaceAnalyzer(new String[0]);

        ReturnType returned = this.analyzer.getFeedback(new String[0]);

        //Analyzer entries are null
        assertEquals(new HashMap<>(), this.analyzer.getFieldNames());
        assertEquals(new HashMap<>(), this.analyzer.getFieldTypes());
        assertEquals(new HashMap<>(), this.analyzer.getMethodVarNames());
        assertEquals(new HashMap<>(), this.analyzer.getMethodVarTypes());
        assertEquals(new HashMap<>(), this.analyzer.getPossibleInterfaces());

        assertEquals(new ArrayList<>(), returned.errorsCaught);
    }

    @Test
    public void testReturnedArrayListField() {
        this.analyzer = new CodeToInterfaceAnalyzer(ourClass);
        ReturnType returned = this.analyzer.getFeedback(ourClass);

        assertTrue(returned.errorsCaught.size() > 0);
        List<LinterError> errors = returned.errorsCaught;

        boolean found = false;
        LinterError foundErr = null;
        for (LinterError err : errors) {
            if (err.message.compareTo("Field fieldListTest should be of type List<>, not ArrayList<>") == 0) {
                found = true;
                foundErr = err;
            }
        }
        assertTrue(found);
        assertEquals(ourClass[0], foundErr.className);
        assertNull(foundErr.methodName);
        assertEquals(ErrType.ERROR, foundErr.type);
    }

    @Test
    public void testReturnedHashMapField() {
        this.analyzer = new CodeToInterfaceAnalyzer(ourClass);
        ReturnType returned = this.analyzer.getFeedback(ourClass);

        assertTrue(returned.errorsCaught.size() > 0);
        List<LinterError> errors = returned.errorsCaught;

        boolean found = false;
        LinterError foundErr = null;
        for (LinterError err : errors) {
            if (err.message.compareTo("Field fieldMapTest should be of type Map<>, not HashMap<>") == 0) {
                found = true;
                foundErr = err;
            }
        }
        assertTrue(found);
        assertEquals(ourClass[0], foundErr.className);
        assertNull(foundErr.methodName);
        assertEquals(ErrType.ERROR, foundErr.type);
    }

    @Test
    public void testReturnedHashSetField() {
        this.analyzer = new CodeToInterfaceAnalyzer(ourClass);
        ReturnType returned = this.analyzer.getFeedback(ourClass);

        assertTrue(returned.errorsCaught.size() > 0);
        List<LinterError> errors = returned.errorsCaught;

        boolean found = false;
        LinterError foundErr = null;
        for (LinterError err : errors) {
            if (err.message.compareTo("Field fieldSetTest should be of type Set<>, not HashSet<>") == 0) {
                found = true;
                foundErr = err;
            }
        }
        assertTrue(found);
        assertEquals(ourClass[0], foundErr.className);
        assertNull(foundErr.methodName);
        assertEquals(ErrType.ERROR, foundErr.type);
    }

    @Test
    public void testFieldWithNoInterfacesToImplement() {
        this.analyzer = new CodeToInterfaceAnalyzer(ourClass);
        ReturnType returned = this.analyzer.getFeedback(ourClass);

        assertTrue(returned.errorsCaught.size() > 0);
        List<LinterError> errors = returned.errorsCaught;

        boolean found = false;
        LinterError foundErr = null;
        for (LinterError err : errors) {
            if (err.message.compareTo("Possible Interface for triangleTest: example/code2interface/Shape") == 0 ||
                err.message.compareTo("Possible Interface for triangleTest: example/code2interface/Triangle") == 0) {
                found = true;
                foundErr = err;
            }
        }
        assertFalse(found);
    }

    //Tests that tri1 will detect being type Shape
    @Test
    public void testMethodVarOfType1() {
        this.analyzer = new CodeToInterfaceAnalyzer(ourClass);
        ReturnType returned = this.analyzer.getFeedback(ourClass);

        assertTrue(returned.errorsCaught.size() > 0);
        List<LinterError> errors = returned.errorsCaught;

        boolean found = false;
        LinterError foundErr = null;
        for (LinterError err : errors) {
            if (err.message.compareTo("Potential Interface for tri1: example/code2interface/Shape") == 0) {
                found = true;
                foundErr = err;
            }
        }
        assertTrue(found);
        assertEquals(ourClass[0], foundErr.className);
        assertEquals("testFunc", foundErr.methodName);
        assertEquals(ErrType.WARNING, foundErr.type);
    }

    @Test
    public void testMethodVarOfType2() {
        this.analyzer = new CodeToInterfaceAnalyzer(ourClass);
        ReturnType returned = this.analyzer.getFeedback(ourClass);

        assertTrue(returned.errorsCaught.size() > 0);
        List<LinterError> errors = returned.errorsCaught;

        boolean found = false;
        LinterError foundErr = null;
        for (LinterError err : errors) {
            if (err.message.compareTo("Potential Interface for tri2: example/code2interface/Printable") == 0) {
                found = true;
                foundErr = err;
            }
        }
        assertTrue(found);
        assertEquals(ourClass[0], foundErr.className);
        assertEquals("testFunc", foundErr.methodName);
        assertEquals(ErrType.WARNING, foundErr.type);
    }

    //Tri3 is called by two Shape methods, getArea and compareShape
    @Test
    public void testMethodVarOfType3() {
        this.analyzer = new CodeToInterfaceAnalyzer(ourClass);
        ReturnType returned = this.analyzer.getFeedback(ourClass);

        assertTrue(returned.errorsCaught.size() > 0);
        List<LinterError> errors = returned.errorsCaught;

        boolean found = false;
        LinterError foundErr = null;
        for (LinterError err : errors) {
            if (err.message.compareTo("Potential Interface for tri3: example/code2interface/Shape") == 0) {
                found = true;
                foundErr = err;
            }
        }
        assertTrue(found);
        assertEquals(ourClass[0], foundErr.className);
        assertEquals("testFunc", foundErr.methodName);
        assertEquals(ErrType.WARNING, foundErr.type);
    }

    //Tri4 called a Triangle method, and should not throw an error.
    @Test
    public void testMethodVarOfType4() {
        this.analyzer = new CodeToInterfaceAnalyzer(ourClass);
        ReturnType returned = this.analyzer.getFeedback(ourClass);

        assertTrue(returned.errorsCaught.size() > 0);
        List<LinterError> errors = returned.errorsCaught;

        boolean found = false;
        LinterError foundErr = null;
        for (LinterError err : errors) {
            if (err.message.compareTo("Potential Interface for tri4: example/code2interface/Shape") == 0) {
                found = true;
                foundErr = err;
            }
        }
        assertFalse(found);
    }

    //Tri5 calls another Triangle method, so it should not throw an error.
    @Test
    public void testMethodVarOfType5() {
        this.analyzer = new CodeToInterfaceAnalyzer(ourClass);
        ReturnType returned = this.analyzer.getFeedback(ourClass);

        assertTrue(returned.errorsCaught.size() > 0);
        List<LinterError> errors = returned.errorsCaught;

        boolean found = false;
        LinterError foundErr = null;
        for (LinterError err : errors) {
            if (err.message.compareTo("Potential Interface for tri5: example/code2interface/Shape") == 0) {
                found = true;
                foundErr = err;
            }
        }
        assertFalse(found);
    }

    //Tri6 already a Shape (even if a Triangle object), so it should not throw an error.
    @Test
    public void testMethodVarOfType6() {
        this.analyzer = new CodeToInterfaceAnalyzer(ourClass);
        ReturnType returned = this.analyzer.getFeedback(ourClass);

        assertTrue(returned.errorsCaught.size() > 0);
        List<LinterError> errors = returned.errorsCaught;

        boolean found = false;
        LinterError foundErr = null;
        for (LinterError err : errors) {
            if (err.message.compareTo("Potential Interface for tri6: example/code2interface/Shape") == 0) {
                found = true;
                foundErr = err;
            }
        }
        assertFalse(found);
    }

    @Test
    public void testMethodVarList() {
        this.analyzer = new CodeToInterfaceAnalyzer(ourClass);
        ReturnType returned = this.analyzer.getFeedback(ourClass);

        assertTrue(returned.errorsCaught.size() > 0);
        List<LinterError> errors = returned.errorsCaught;

        boolean found = false;
        LinterError foundErr = null;
        for (LinterError err : errors) {
            if (err.message.compareTo("Local Variable testList should be of type List<>, not ArrayList<>") == 0) {
                found = true;
                foundErr = err;
            }
        }
        assertTrue(found);
        assertEquals(ourClass[0], foundErr.className);
        assertEquals("anotherFunc", foundErr.methodName);
        assertEquals(ErrType.ERROR, foundErr.type);;
    }


    @Test
    public void testMethodVarMap() {
        this.analyzer = new CodeToInterfaceAnalyzer(ourClass);
        ReturnType returned = this.analyzer.getFeedback(ourClass);

        assertTrue(returned.errorsCaught.size() > 0);
        List<LinterError> errors = returned.errorsCaught;

        boolean found = false;
        LinterError foundErr = null;
        for (LinterError err : errors) {
            if (err.message.compareTo("Local Variable mapTest should be of type Map<>, not HashMap<>") == 0) {
                found = true;
                foundErr = err;
            }
        }
        assertTrue(found);
        assertEquals(ourClass[0], foundErr.className);
        assertEquals("anotherFunc", foundErr.methodName);
        assertEquals(ErrType.ERROR, foundErr.type);
    }

    @Test
    public void testMethodVarSet() {
        this.analyzer = new CodeToInterfaceAnalyzer(ourClass);
        ReturnType returned = this.analyzer.getFeedback(ourClass);

        assertTrue(returned.errorsCaught.size() > 0);
        List<LinterError> errors = returned.errorsCaught;

        boolean found = false;
        LinterError foundErr = null;
        for (LinterError err : errors) {
            if (err.message.compareTo("Local Variable setTest should be of type Set<>, not HashSet<>") == 0) {
                found = true;
                foundErr = err;
            }
        }
        assertTrue(found);
        assertEquals(ourClass[0], foundErr.className);
        assertEquals("anotherFunc", foundErr.methodName);
        assertEquals(ErrType.ERROR, foundErr.type);
    }
}

