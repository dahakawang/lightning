package net.davidvoid.thor.lightning.exception;

public class ThorException extends RuntimeException {

    private static final long serialVersionUID = 8473037975359169939L;

    public ThorException() {
        super();
    }

    public ThorException(String msg) {
        super(msg);
    }

    public ThorException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
