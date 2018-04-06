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

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
public class AthleteRepositoryTest {

	@Autowired
	private AthleteRepository repository;
	
	@Test
	public void createNewAthlete() {
		Athlete athlete = new Athlete("Adam", "Malysz", "Poland", 1977);
		repository.save(athlete);
		assertThat(athlete.getId()).isNotNull();
	}
	
	@Test
	public void deleteAthlete() {
		Athlete newAthlete = new Athlete("Adam", "Malysz", "Poland", 1977);
		repository.save(newAthlete);
		List<Athlete> athlete = repository.findByLastName("Malysz");
		repository.delete(athlete);
		List<Athlete> athletes = (List<Athlete>) repository.findAll();
		assertThat(athletes.get(0).getLastName()).isNotEqualTo("Malysz");
	}
	
	@Test
	public void findbyLastName() {
		Athlete newAthlete = new Athlete("Adam", "Malysz", "Poland", 1977);
		repository.save(newAthlete);
		List<Athlete> athlete = repository.findByLastName("Malysz");
		assertThat(athlete.size()).isEqualTo(1);
	}
}
