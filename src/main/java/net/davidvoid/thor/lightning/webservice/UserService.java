package net.davidvoid.thor.lightning.webservice;

import net.davidvoid.thor.lightning.service.Auth;
import net.davidvoid.thor.lightning.service.security.JwtAuthenticationService;
import net.davidvoid.thor.lightning.util.HttpRequestAssertion;
import net.davidvoid.thor.lightning.util.MapLiteral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static net.davidvoid.thor.lightning.util.HttpRequestAssertion.assertStringNotEmpty;
import static net.davidvoid.thor.lightning.util.HttpRequestAssertion.isTrue;

/**
 * Created by david on 3/20/16.
 */
@RestController
@RequestMapping("/users")
public class UserService {

    @Autowired
    Auth auth = null;

    @Autowired
    JwtAuthenticationService jwtService = null;

    @RequestMapping(method = RequestMethod.GET)
    public String welcome(HttpServletRequest req) {
        return "hello world";
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.PUT)
    public String register() {
        return null;
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public String getUser() {
        return null;
    }

    @RequestMapping(value = "/{username}/login", method = RequestMethod.POST)
    public Map<String, Object> login(@PathVariable String username, @RequestBody Map<String, Object> user) {
        isTrue(user.size() == 1, "malformed model");
        assertStringNotEmpty(user.get("password"), "password should be given as string");

        String token = jwtService.getToken(username);
        return MapLiteral.map("jwt-token", token);
    }

    @RequestMapping(value = "/{username}/logout", method = RequestMethod.POST)
    public String logout() {
        return null;
    }

}
