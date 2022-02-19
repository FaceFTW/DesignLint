package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import domain.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class TemplateMethodTest {
    private final String[] correctClasses = { "example/template/CaffeineBeverage", "example/template/Tea",
        "example/template/Coffee" };
    private final String[] wrongClasses = { "example/template/NotTemplate", "example/template/NotTemplateSubclass" };
    private final String[] sortOfWrongClasses = { "example/template/AbstractNotTemplate", "example/template/AbstractSubclass",
    "example/template/ConcreteSubclass" };
    private final String[] allClasses = { "example/template/CaffeineBeverage", "example/template/Tea",
            "example/template/Coffee", "example/template/NotTemplate", "example/template/NotTemplateSubclass",
            "example/template/AbstractNotTemplate", "example/template/AbstractSubclass",
            "example/template/ConcreteSubclass" };

    //Analyzer Class
    private TemplateMethodAnalyzer analyzer;

    //Instantiate the Analyzer Class
    public void setUpCorrectClasses() { this.analyzer = new TemplateMethodAnalyzer(correctClasses); }
    public void setUpWrongClasses() { this.analyzer = new TemplateMethodAnalyzer(wrongClasses); }
    public void setUpSortOfWrongClasses() { this.analyzer = new TemplateMethodAnalyzer(sortOfWrongClasses); }
    public void setUpAllClasses() { this.analyzer = new TemplateMethodAnalyzer(allClasses); }

    @Test
    public void testFieldsGivenNoClasses() {
        this.analyzer = new TemplateMethodAnalyzer(new String[0]);

        ReturnType returned = this.analyzer.getFeedback(new String[0]);

        //Analyzer entries are null
        assertEquals(new HashMap<>(), this.analyzer.getExtendedClasses());
        assertEquals(new HashMap<>(), this.analyzer.getAbstractMethods());
        assertEquals(new HashMap<>(), this.analyzer.getConcreteMethods());
        assertEquals(new HashMap<>(), this.analyzer.getAbstractInsideConcreteMethods());

        assertEquals(new ArrayList<>(), returned.errorsCaught);
    }

    @Test
    public void testGetSuperClasses() {
        setUpCorrectClasses();
        this.analyzer.getRelevantData(correctClasses);

        for (String className : this.analyzer.getExtendedClasses().keySet()) {
            System.out.println(className + ":  " + this.analyzer.getExtendedClasses().get(className));
        }

        assertNotNull(this.analyzer.getExtendedClasses());
    }

    @Test
    public void testGetAbstractMethod() {
        setUpCorrectClasses();
        this.analyzer.getRelevantData(correctClasses);

        System.out.println(this.analyzer.getAbstractMethods().get("example/template/CaffeineBeverage"));

        assertTrue(this.analyzer.getAbstractMethods().get("example/template/CaffeineBeverage").contains("brew"));
        assertTrue(this.analyzer.getAbstractMethods().get("example/template/CaffeineBeverage").contains("addCondiments"));

    }

    @Test
    public void testGetConcreteMethod() {
        setUpCorrectClasses();
        this.analyzer.getRelevantData(correctClasses);

        System.out.println(this.analyzer.getConcreteMethods().get("example/template/CaffeineBeverage"));

        assertTrue(this.analyzer.getConcreteMethods().get("example/template/CaffeineBeverage").contains("prepareRecipe"));
        assertTrue(this.analyzer.getConcreteMethods().get("example/template/CaffeineBeverage").contains("boilWater"));
        assertTrue(this.analyzer.getConcreteMethods().get("example/template/CaffeineBeverage").contains("pourInCup"));
        assertTrue(this.analyzer.getConcreteMethods().get("example/template/Tea").contains("brew"));
        assertTrue(this.analyzer.getConcreteMethods().get("example/template/Tea").contains("addCondiments"));

    }

    @Test
    public void testGetAbstractInConcrete() {
        setUpAllClasses();
        this.analyzer.getRelevantData(allClasses);

        for (String className : this.analyzer.getAbstractInsideConcreteMethods().keySet()) {
            for (String methodName : this.analyzer.getAbstractInsideConcreteMethods().get(className).keySet()) {
                System.out.println(className + " " + methodName + " " +
                        this.analyzer.getAbstractInsideConcreteMethods().get(className).get(methodName));
            }
        }
        //System.out.println(this.analyzer.getMethodsInsideAbstractMethods());

        assertNotNull(this.analyzer.getAbstractInsideConcreteMethods().get("example/template/CaffeineBeverage").
                get("prepareRecipe"));
        assertNotNull(this.analyzer.getAbstractInsideConcreteMethods().get("example/template/AbstractNotTemplate").
                get("testFunc"));
    }

    @Test
    public void testTemplateMethodFound1() {
        setUpCorrectClasses();
        ReturnType returned = this.analyzer.getFeedback(correctClasses);

        List<LinterError> patterns = returned.errorsCaught;

        boolean found = false;
        LinterError foundErr = null;
        for (LinterError err : patterns) {
            if (err.message.compareTo("Template Method Pattern Found: prepareRecipe (Subclass: example/template/Coffee)") == 0) {
                found = true;
                foundErr = err;
            }
        }
        assertTrue(found);
        assertEquals("example/template/CaffeineBeverage", foundErr.className);
        assertEquals("prepareRecipe", foundErr.methodName);
        assertEquals(ErrType.PATTERN, foundErr.type);
    }

    @Test
    public void testTemplateMethodFound2() {
        setUpCorrectClasses();
        ReturnType returned = this.analyzer.getFeedback(correctClasses);

        List<LinterError> patterns = returned.errorsCaught;

        boolean found = false;
        LinterError foundErr = null;
        for (LinterError err : patterns) {
            if (err.message.compareTo("Template Method Pattern Found: prepareRecipe (Subclass: example/template/Tea)") == 0) {
                found = true;
                foundErr = err;
            }
        }
        assertTrue(found);
        assertEquals("example/template/CaffeineBeverage", foundErr.className);
        assertEquals("prepareRecipe", foundErr.methodName);
        assertEquals(ErrType.PATTERN, foundErr.type);
    }

    @Test
    public void testTemplateMethodNotFound() {
        setUpCorrectClasses();
        ReturnType returned = this.analyzer.getFeedback(correctClasses);

        List<LinterError> patterns = returned.errorsCaught;

        boolean found = false;
        for (LinterError err : patterns) {
            if (err.message.compareTo("Template Method Pattern Found: testFunc (Subclass: example/template/ConcreteSubclass)") == 0) {
                found = true;
            }
        }
        for (LinterError err : patterns) {
            System.out.println(err);
        }
        assertFalse(found);
    }

    @Test
    public void testTemplateMethodNotFound2() {
        setUpCorrectClasses();
        ReturnType returned = this.analyzer.getFeedback(correctClasses);

        List<LinterError> patterns = returned.errorsCaught;

        boolean found = false;
        for (LinterError err : patterns) {
            if (err.message.compareTo("Template Method Pattern Found: prepareRecipe (Subclass: example/template/CaffeineBeverage)") == 0) {
                found = true;
            }
        }
        for (LinterError err : patterns) {
            System.out.println(err);
        }
        assertFalse(found);
    }

    @Test
    public void testTemplateMethodNotFound3() {
        setUpAllClasses();
        ReturnType returned = this.analyzer.getFeedback(allClasses);

        List<LinterError> patterns = returned.errorsCaught;

        boolean found = false;
        for (LinterError err : patterns) {
            if (err.message.compareTo("Template Method Pattern Found: notTemplate (Subclass: example/template/NotTemplate)") == 0) {
                found = true;
            }
        }
        for (LinterError err : patterns) {
            System.out.println(err);
        }
        assertFalse(found);
    }
}
