package net.davidvoid.thor.lightning.exception;

public class ResourceNotFoundException extends ThorException {

    private static final long serialVersionUID = 9049690769685739269L;

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ResourceNotFoundException(String string) {
        super(string);
    }

}
