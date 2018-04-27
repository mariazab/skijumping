package fi.haagahelia.skijumping;


import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@SpringBootApplication
public class SkijumpingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkijumpingApplication.class, args);
		
	}
	
	/*@Bean
	public CommandLineRunner clr(UserRepository userRepository) {
		return (args) -> {
			
			//Create users
			User admin = new User("admin", "", "admin", "admin@bookstore.com", "ADMIN");
			User user = new User("user", "", "test user", "user@bookstore.com", "USER");
			userRepository.save(admin);
			userRepository.save(user);
			
		};
	}*/
	
	@Bean
	public JavaMailSender getJavaMailSender() {
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost("smtp.gmail.com");
	    mailSender.setPort(587);
	     
	    mailSender.setUsername("worldofskijumping@gmail.com");
	    mailSender.setPassword("jsifristmopfbvdp");
	     
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
	     
	    return mailSender;
	}
	
}
