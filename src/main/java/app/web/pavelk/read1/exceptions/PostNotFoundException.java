package app.web.pavelk.read1.exceptions;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PostNotFoundException(String message) {
        super(message);
    }
}
