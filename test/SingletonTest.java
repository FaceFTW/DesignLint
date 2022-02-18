import org.junit.Test;

public class SingletonTest {
    
    @Test
    public void hasPublicConstructorStaticFieldStaticMethodTest(){
        //This test shouldn't detect a singleton
    }
    @Test
    public void hasPublicConstructorNoFieldStaticMethodTest(){
        //this test shouldn't detect a singleton
    }
    @Test
    public void hasPublicConstructorStaticFieldNoStaticMethodTest(){
        //This test shouldn't detect a singleton
    }
    @Test
    public void hasPublicConstructorNoFieldNoStaticMethodTest(){
        //this test shouldn't detect a singleton

    }
    @Test
    public void hasPrivateConstructorNoStaticFieldStaticMethodTest(){
        //This test shouldn't detect a singleton
    }
    @Test
    public void hasPrivateConstructorNoStaticFieldNoStaticMethodTest(){
        //This test shouldn't detect a singleton
    }
    @Test
    public void hasPrivateConstructorStaticFieldNoStaticMethodTest(){
        //This test shouldn't detect a singleton
    }
    @Test
    public void hasPrivateConstructorStaticFieldStaticMethodTest(){
        //This test SHOULD detect a singleton
    }

    
}
