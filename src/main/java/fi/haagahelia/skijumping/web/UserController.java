package fi.haagahelia.skijumping.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fi.haagahelia.skijumping.domain.SignupForm;
import fi.haagahelia.skijumping.domain.User;
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
	
	//Sign up
	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("signupform", new SignupForm());
		return "signup";
	}
	
	//Creating user and checking if it already exists
	@RequestMapping(value="/saveuser", method=RequestMethod.POST)
	public String save(@Valid @ModelAttribute("signupform") SignupForm signupForm, BindingResult bindingResult) {
		// Validation errors
		if (!bindingResult.hasErrors()) { 
    		// Checking if passwords match
			if (signupForm.getPassword().equals(signupForm.getPasswordCheck())) { 		
				String password = signupForm.getPassword();
		    	BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
		    	String hashPassword = bc.encode(password);
	
		    	User newUser = new User();
		    	newUser.setPasswordHash(hashPassword);
		    	newUser.setUsername(signupForm.getUsername());
		    	newUser.setName(signupForm.getName());
		    	newUser.setEmail(signupForm.getEmail());
		    	newUser.setRole("USER");
		    	// Checking if user exists
		    	if (repository.findByUsername(signupForm.getUsername()) == null) { 
		    		repository.save(newUser);
		    	}
		    	else {
	    			bindingResult.rejectValue("username", "err.username", "Username already exists");    	
	    			return "signup";		    		
		    	}
    		}
    		else {
    			bindingResult.rejectValue("passwordCheck", "err.passCheck", "Passwords does not match");    	
    			return "signup";
    		}
    	}
    	else {
    		return "signup";
    	}
    	return "redirect:/login"; 
	}

}
