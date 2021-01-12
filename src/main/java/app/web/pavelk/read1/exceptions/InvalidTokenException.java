package app.web.pavelk.read1.exceptions;


import org.springframework.security.core.AuthenticationException;

public class InvalidTokenException extends AuthenticationException {
    public InvalidTokenException(String msg) {
        super(msg);
    }
}
