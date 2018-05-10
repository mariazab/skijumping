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
import fi.haagahelia.skijumping.mail.EmailService;
import fi.haagahelia.skijumping.mail.Mail;

@Controller
public class UserController {

	@Autowired
	private UserRepository repository;

	@Autowired
	private EmailService emailService;

	// Login
	@RequestMapping("/login")
	public String login() {
		return "login";
	}

	// Sign up
	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("signupform", new SignupForm());
		return "signup";
	}

	// Creating user and checking if it already exists
	@RequestMapping(value = "/saveuser", method = RequestMethod.POST)
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
					sendWelcomeMail(newUser);
				} else {
					bindingResult.rejectValue("username", "err.username", "Username already exists");
					return "signup";
				}
			} else {
				bindingResult.rejectValue("passwordCheck", "err.passCheck", "Passwords does not match");
				return "signup";
			}
		} else {
			return "signup";
		}
		return "redirect:/login";
	}

	//Sending welcoming email
	public void sendWelcomeMail(User user) {

		String name = user.getName();
		String email = user.getEmail();

		// Set the content of the email
		String content = "Hi " + name + "!\n\nWelcome to the World of Ski Jumping!\n"
				+ "\nIt is very exciting to have you on board! (or maybe on hill? :))"
				+ "\nOn this website you can check out the latest results and world cup standings,"
				+ " information about next competition and the Twitter Feed of FIS Ski Jumping."
				+ "\nYou can also add ski jumpers to your favorites and see their results on your dashboard!\n\n"
				+ "If you have any questions or feedback, please don't hesitate to send an email!\n\n\n"
				+ "Happy cheering!\n\nMay the wind be in your favour! :)\n\n\n\n"
				+ "This email has been sent automatically by worldofskijumping@gmail.com";

		Mail mail = new Mail();
		mail.setFrom("worldofskijumping@gmail.com");
		mail.setTo(email);
		mail.setSubject(name + ", welcome to the World of Ski Jumping!");
		mail.setContent(content);

		emailService.sendSimpleMessage(mail);
	}

}
