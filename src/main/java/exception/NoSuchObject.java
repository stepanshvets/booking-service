package exception;

public class NoSuchObject extends RuntimeException {
    public NoSuchObject(String message) {
        super(message);
    }
}
