package net.davidvoid.thor.lightning.exception;

public class DuplicateUserException extends ThorException {

    private static final long serialVersionUID = -3178235349146011139L;

    public DuplicateUserException(String msg) {
        super(msg);
    }

}
