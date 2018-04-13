package fi.haagahelia.skijumping.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fi.haagahelia.skijumping.domain.UserRepository;

@Controller
public class UserController {

	@Autowired
	private UserRepository repository;
	
	// Login 
		@RequestMapping("/login")
		public String login() {
			return "login";
		}

}
