package fi.haagahelia.skijumping;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
}
