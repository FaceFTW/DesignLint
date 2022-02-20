package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import datasource.ASMParser;
import datasource.Invoker;
import datasource.MethodCall;
import domain.*;
import org.junit.Test;
import static org.junit.Assert.*;


public class MiscellaneousTest {

    @Test
    public void testLinterErrorToStringReturnsString() {
        LinterError err = new LinterError("testClass", "testMethod", "testMessage", ErrType.INFO);
        assertNotNull(err.toString());
        assertNotEquals("", err.toString());
    }

    @Test
    public void testMethodCallToStringReturnsString() {
        MethodCall call = new MethodCall("testMethod", Invoker.CONSTRUCTED,
                "invokerName", "className");
        assertNotNull(call.toString());
        assertNotEquals("", call.toString());

    }
}
