
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import datasource.Invoker;
import datasource.MethodCall;
import domain.message.InfoLinterMessage;
import domain.message.LinterMessage;;

public class MiscellaneousTest {

	@Test
	public void testLinterMessageToStringReturnsString() {
		LinterMessage err = new InfoLinterMessage("testClass", "testMethod", "testMessage");
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
