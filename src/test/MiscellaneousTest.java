
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import datasource.Invoker;
import datasource.MethodCall;
import domain.ErrType;
import domain.LinterError;

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
