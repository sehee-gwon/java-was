package was.application.exception;

public class HttpSecurityException extends RuntimeException {
    public HttpSecurityException() {
        super("Http security Error");
    }

    public HttpSecurityException(String message) {
        super(message);
    }
}
