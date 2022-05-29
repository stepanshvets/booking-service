package exception;

public class NoSuchEntity extends RuntimeException {
    public NoSuchEntity() {
        super("You didn't select any entities!");
    }
}
