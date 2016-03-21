package net.davidvoid.thor.lightning.webservice;

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
	public String welcome() {
		return "hello world";
	}
}
