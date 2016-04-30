package net.davidvoid.thor.lightning.exception;

public class AuthenticationException extends ThorException {

    private static final long serialVersionUID = -9099285304104642354L;

    public AuthenticationException() {
        super();
    }

    public AuthenticationException(String msg) {
        super(msg);
    }

    public AuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
