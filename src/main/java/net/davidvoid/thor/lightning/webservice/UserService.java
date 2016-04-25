package net.davidvoid.thor.lightning.webservice;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by david on 3/20/16.
 */
@RestController
@RequestMapping("/users")
public class UserService {
	
	@RequestMapping(method = RequestMethod.GET)
	public String welcome(HttpServletRequest req) {
		return "hello world";
	}
	
	@RequestMapping(value = "/{username}", method = RequestMethod.PUT)
	public String register() {
	    return null;
	}
	
	@RequestMapping(value = "/{username}/login", method = RequestMethod.POST)
	public String login() {
	    return null;
	}
	
	@RequestMapping(value = "/{username}/logout", method = RequestMethod.POST)
	public String logout() {
	    return null;
	}
	
	@RequestMapping(value = "/{username}", method = RequestMethod.GET)
	public String getUser() {
	    return null;
	}
}
