package example.exceptionstyle;

public class ExceptionStyleExamples {
    // These are a bunch of examples of compliant and non-compliant methods against
    // the Throw generic exception linter

    // Compliant Methods

    public static void compliantMethodThrowsException() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public static void compliantMethodCallThrowsException() throws UnsupportedOperationException {
        ExceptionStyleExamples.compliantMethodThrowsException();
    }

    public static void compliantMethodThrowsMultipleExceptions()
            throws UnsupportedOperationException, IllegalStateException {
        if (System.currentTimeMillis() % 2 == 1) {
            throw new UnsupportedOperationException();
        } else {
            throw new IllegalStateException();
        }
    }

    public static void compliantMethodCatchException() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void compliantMethodCatchMultipleException() {
        try {
            Thread.sleep(1000);
            // screw it, we dividing by zero
            int ohNo = 1 / 0;
        } catch (InterruptedException | ArithmeticException e) {
            e.printStackTrace();
        }
    }

    public static void compliantMethodCallCatchMultipleException() {
        try {
            ExceptionStyleExamples.compliantMethodThrowsMultipleExceptions();
        } catch (UnsupportedOperationException | IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public static void compliantMethodCallCatchException() {
        try {
            ExceptionStyleExamples.compliantMethodThrowsException();
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        }
    }

    // Non-Compliant Mehtods

    public static void nonCompliantMethodThrowsException() throws Exception {
        throw new Exception();
    }

    public static void nonCompliantMethodThrowsRuntimeException() throws RuntimeException {
        throw new RuntimeException();
    }

    public static void nonCompliantMethodThrowsError() throws Error {
        throw new Error();
    }

    public static void nonCompliantMethodThrowsThrowable() throws Throwable {
        throw new Exception(); // Throwable is actually an interface
    }

    public static void nonCompliantMethodCallsCompliantButThrowsException() throws Exception {
        ExceptionStyleExamples.compliantMethodThrowsException();
    }

    public static void nonCompliantMethodCallsCompliantWithMultipleThrowsButThrowsException() throws Exception {
        ExceptionStyleExamples.compliantMethodThrowsMultipleExceptions();
    }

    public static void nonCompliantMethodCatchesException() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void nonCompliantMethodCallsCompliantButCatchesException() {
        try {
            ExceptionStyleExamples.compliantMethodThrowsException();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void nonCompliantMethodCallsCompliantWithMultipleThrowsButCatchesException() {
        try {
            ExceptionStyleExamples.compliantMethodThrowsMultipleExceptions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void nonCompliantMethodCatchMultipleExceptions() {
        try {
            Thread.sleep(1000); // InterruptedException
            int ohNo = 1 / 0; // ArithmeticException
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        

}
