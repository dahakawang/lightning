package net.davidvoid.thor.lightning.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNAUTHORIZED)
public class AuthorizationException extends ThorException {

    private static final long serialVersionUID = -3171503406944629760L;

    public AuthorizationException() {
        super();
    }

    public AuthorizationException(String msg) {
        super(msg);
    }

    public AuthorizationException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
