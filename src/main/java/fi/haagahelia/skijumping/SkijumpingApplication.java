package fi.haagahelia.skijumping;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import fi.haagahelia.skijumping.domain.Athlete;
import fi.haagahelia.skijumping.domain.AthleteRepository;

@SpringBootApplication
public class SkijumpingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkijumpingApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner clr(AthleteRepository repository) {
		return (args) -> {
			
			//Create and save athletes
			Athlete kStoch = new Athlete("Kamil", "Stoch", "Poland", 1987);
			Athlete rFreitag = new Athlete("Richard", "Freitag", "Germany", 1991);
			Athlete rJohansson = new Athlete("Robert", "Johansson", "Norway", 1990);
			
			repository.save(kStoch);
			repository.save(rFreitag);
			repository.save(rJohansson);
			
		};
	}
}
