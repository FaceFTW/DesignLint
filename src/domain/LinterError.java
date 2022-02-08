package domain;

public class LinterError {
    public String message;
    public ErrType type;

    LinterError(String message, ErrType type) {
        this.message = message;
        this.type = type;
    }
}

