package fi.haagahelia.skijumping;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import fi.haagahelia.skijumping.domain.Athlete;
import fi.haagahelia.skijumping.domain.AthleteRepository;
import fi.haagahelia.skijumping.domain.FavAthlete;
import fi.haagahelia.skijumping.domain.FavAthleteRepository;
import fi.haagahelia.skijumping.domain.User;
import fi.haagahelia.skijumping.domain.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AthleteRepositoryTest {

	@Autowired
	private AthleteRepository repository;

	@Autowired
	private FavAthleteRepository favRepository;

	@Autowired
	private UserRepository userRepository;

	
	// Test creating new athlete
	@Test
	public void createNewAthlete() {
		Athlete athlete = new Athlete("Adam", "Malysz", "Poland", 1977);
		repository.save(athlete);
		assertThat(athlete.getId()).isNotNull();
	}

	
	// Test creating new favorite athlete
	@Test
	public void createNewFavAthlete() {
		
		//Create new user
		User newUser = new User("test", "$2a$10$VRlfA/vqlj1XJPEFUNclAOn84wZNbuKJIY22IXlWZLHlW3w2O0I.2", "test", "test@test.com", "USER");
		userRepository.save(newUser);

		//Create new athlete
		Athlete newAthlete = new Athlete("Adam", "Malysz", "Poland", 1977);
		repository.save(newAthlete);

		//Create new fav athlete
		FavAthlete newFavAthlete = new FavAthlete(newUser, newAthlete);
		favRepository.save(newFavAthlete);
		assertThat(newFavAthlete.getId()).isNotNull();
	}

	// Test deleting athlete
	@Test
	public void deleteAthlete() {
		//Create new athlete
		Athlete newAthlete = new Athlete("Adam", "Malysz", "Poland", 1977);
		repository.save(newAthlete);
		
		List<Athlete> athlete = repository.findByLastName("Malysz");
		repository.delete(athlete);
		List<Athlete> athletes = (List<Athlete>) repository.findAll();

		assertThat(athletes.get(athletes.size() - 1).getLastName()).isNotEqualTo("Malysz");
	}

	// Test deleting favorite athlete
	@Test
	public void deleteFavAthlete() {
		
		//Create new user
		User newUser = new User("test", "$2a$10$VRlfA/vqlj1XJPEFUNclAOn84wZNbuKJIY22IXlWZLHlW3w2O0I.2", "test", "test@test.com", "USER");
		userRepository.save(newUser);

		//Create new athlete
		Athlete newAthlete = new Athlete("Adam", "Malysz", "Poland", 1977);
		repository.save(newAthlete);

		//Create new fav athlete
		FavAthlete newFavAthlete = new FavAthlete(newUser, newAthlete);
		favRepository.save(newFavAthlete);

		List<FavAthlete> favAthlete = favRepository.findByUserId(userRepository.findByUsername("test").getId());
		favRepository.delete(favAthlete);

		List<FavAthlete> favAthletes = (List<FavAthlete>) favRepository.findAll();
		assertThat(favAthletes.get(favAthletes.size() - 1).getUser().getUsername()).isNotEqualTo("test");
	}

	// Test finding an athlete by last name
	@Test
	public void findbyLastName() {
		
		//Create new athlete
		Athlete newAthlete = new Athlete("Adam", "Malysz", "Poland", 1977);
		repository.save(newAthlete);
		
		List<Athlete> athlete = repository.findByLastName("Malysz");
		assertThat(athlete.size()).isEqualTo(1);
	}

	// Test finding favorite athlete by user id
	@Test
	public void findByUserId() {
		
		//Create new user
		User newUser = new User("test", "$2a$10$VRlfA/vqlj1XJPEFUNclAOn84wZNbuKJIY22IXlWZLHlW3w2O0I.2", "test",	"test@test.com", "USER");
		userRepository.save(newUser);

		//Create new athlete
		Athlete newAthlete = new Athlete("Adam", "Malysz", "Poland", 1977);
		repository.save(newAthlete);

		//Create new fav athlete
		FavAthlete newFavAthlete = new FavAthlete(newUser, newAthlete);
		favRepository.save(newFavAthlete);

		List<FavAthlete> favAthlete = favRepository.findByUserId(userRepository.findByUsername("test").getId());
		assertThat(favAthlete.size()).isEqualTo(1);
	}
}
