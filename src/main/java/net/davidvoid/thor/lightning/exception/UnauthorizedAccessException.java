package net.davidvoid.thor.lightning.exception;

public class UnauthorizedAccessException extends ThorException {

    private static final long serialVersionUID = -9099285304104642354L;

    public UnauthorizedAccessException(String msg) {
        super(msg);
    }

    public UnauthorizedAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
