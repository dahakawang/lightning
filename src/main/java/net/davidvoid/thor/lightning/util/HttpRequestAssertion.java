package net.davidvoid.thor.lightning.util;

import org.springframework.security.core.context.SecurityContextHolder;

import net.davidvoid.thor.lightning.exception.AuthorizationException;
import net.davidvoid.thor.lightning.exception.BadRequestException;

/**
 * Created by david on 4/27/16.
 */
public class HttpRequestAssertion {
    public static void assertNotNull(Object obj, String msg) {
        if (obj == null) throw new BadRequestException(msg);
    }

    public static void assertNotNull(Object obj) {
        assertNotNull(obj, "");
    }

    public static void assertNotEmpty(String str, String msg) {
        if (str.isEmpty()) throw new BadRequestException(msg);
    }

    public static void assertNotEmpty(String str) {
        assertNotEmpty(str, "");
    }

    public static void assertStringNotEmpty(Object str, String msg) {
        if (str == null || !(str instanceof String) || ((String) str).isEmpty()) {
            throw new BadRequestException(msg);
        }
    }

    public static void assertStringNotEmpty(Object str) {
        assertStringNotEmpty(str, "");
    }

    public static void isTrue(boolean predicate, String msg) {
        if (!predicate) throw new BadRequestException(msg);
    }

    public static void isTrue(boolean predicate) {
        isTrue(predicate, "");
    }

    public static void assertCurrentUserAs(String username, String msg) {
        if (!SecurityContextHolder.getContext().getAuthentication().getName()
                .equals(username))
            throw new AuthorizationException();
    }

    public static void assertCurrentUserAs(String username) {
        assertCurrentUserAs(username, "");
    }
}
