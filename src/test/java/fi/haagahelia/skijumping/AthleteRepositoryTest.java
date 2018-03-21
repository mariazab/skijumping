package fi.haagahelia.skijumping;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import fi.haagahelia.skijumping.domain.Athlete;
import fi.haagahelia.skijumping.domain.AthleteRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AthleteRepositoryTest {

	@Autowired
	private AthleteRepository repository;
	
/*	@Test
	public void createNewAthlete() {
		Athlete athlete = new Athlete("Adam", "Malysz", "Poland", 1977);
		repository.save(athlete);
		assertThat(athlete.getId()).isNotNull();
	}*/
	
	/*@Test
	public void deleteAthlete() {
		List<Athlete> athlete = repository.findByLastName("Stoch");
		repository.delete(athlete);
		List<Athlete> athletes = (List<Athlete>) repository.findAll();
		assertThat(athletes.get(0).getLastName()).isNotEqualTo("Stoch");
	}*/
}
