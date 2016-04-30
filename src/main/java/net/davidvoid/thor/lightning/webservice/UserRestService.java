package net.davidvoid.thor.lightning.webservice;

import static net.davidvoid.thor.lightning.util.HttpRequestAssertion.assertCurrentUserAs;
import static net.davidvoid.thor.lightning.util.HttpRequestAssertion.assertStringNotEmpty;
import static net.davidvoid.thor.lightning.util.HttpRequestAssertion.isTrue;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.davidvoid.thor.lightning.entity.User;
import net.davidvoid.thor.lightning.service.Auth;
import net.davidvoid.thor.lightning.service.security.JwtAuthenticationService;
import net.davidvoid.thor.lightning.util.SessionUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by david on 3/20/16.
 */
@RestController
@RequestMapping("/users")
public class UserRestService {

    @Autowired
    Auth auth = null;
    @Autowired
    JwtAuthenticationService jwtService = null;

    @RequestMapping(method = RequestMethod.GET)
    public String welcome(HttpServletRequest req) {
        return "hello world";
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.PUT)
    public void registerOrUpdate(HttpServletResponse response,
            @PathVariable String username, @RequestBody Map<String, Object> data) {
        isTrue(data.size() == 1, "malformed model");
        assertStringNotEmpty(data.get("password"), "password should be give as string");

        String password = (String) data.get("password");
        User user = null; 
        if (SessionUser.get().isAnonymous()) {
            user = auth.register(username, password);
        } else {
            assertCurrentUserAs(username);
            user = auth.update(username, password);
        }

        String token = jwtService.getToken(user);
        addToCookie(response, token);
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public User getUser(@PathVariable String username) {
        assertCurrentUserAs(username);

        User user = auth.getUser(username);
        return user;
    }


    @RequestMapping(value = "/{username}/login", method = RequestMethod.POST)
    public void login(HttpServletResponse response,
            @PathVariable String username, @RequestBody Map<String, Object> data) {
        isTrue(data.size() == 1, "malformed model");
        assertStringNotEmpty(data.get("password"),
                "password should be given as string");

        String password = (String) data.get("password");
        User user = auth.authenticate(username, password);

        String token = jwtService.getToken(user);
        addToCookie(response, token);
    }

    private void addToCookie(HttpServletResponse reponse, String token) {
        Cookie cookie = new Cookie("jwt-token", token);
        cookie.setHttpOnly(true);
        reponse.addCookie(cookie);
    }

    @RequestMapping(value = "/{username}/logout", method = RequestMethod.POST)
    public String logout() {
        return null;
    }

}
