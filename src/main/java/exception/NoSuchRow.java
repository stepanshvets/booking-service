package exception;

public class NoSuchRow extends RuntimeException{
    public NoSuchRow() {
        super("You didn't select any row!");
    }
}
