package net.davidvoid.thor.lightning.util;

import static org.springframework.util.Assert.isTrue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by david on 4/27/16.
 */
public abstract  class MapLiteral {
    public static Map<String, Object> map(Object... objs) {
        isTrue(objs.length % 2 == 0);

        Map<String, Object> ret = new HashMap<>();
        for (int i = 0; i < objs.length; i += 2) {
            ret.put((String) objs[i], objs[i + 1]);
        }

        return ret;
    }
}
