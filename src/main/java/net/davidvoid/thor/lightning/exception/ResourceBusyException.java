package net.davidvoid.thor.lightning.exception;

/**
 * Created by david on 4/30/16.
 */
public class ResourceBusyException extends BadRequestException {
    private static final long serialVersionUID = -6219299782402603354L;

    public ResourceBusyException() {
        super();
    }

    public ResourceBusyException(String msg) {
        super(msg);
    }

    public ResourceBusyException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
