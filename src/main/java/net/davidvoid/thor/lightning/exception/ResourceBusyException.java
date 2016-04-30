package net.davidvoid.thor.lightning.exception;

/**
 * Created by david on 4/30/16.
 */
public class ResourceBusyException extends BadRequestException {
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
