package fi.haagahelia.skijumping;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import fi.haagahelia.skijumping.domain.Competition;
import fi.haagahelia.skijumping.domain.CompetitionRepository;
import fi.haagahelia.skijumping.domain.Hill;
import fi.haagahelia.skijumping.domain.HillRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CompetitionRepositoryTest {

	@Autowired
	private CompetitionRepository repository;

	@Autowired
	private HillRepository hillRepository;

	// Test creating new competition
	@Test
	public void createNewCompetition() {
		// Creating new Hill
		Hill hill = new Hill("Skocznia", "USA", "LA", 123, 120, 1988);

		// Creating new Date
		Calendar date = Calendar.getInstance();

		// Creating and saving new Competition
		Competition competition = new Competition((long) 11999, hill, date, "test");
		repository.save(competition);
		assertThat(competition.getId()).isNotNull();
	}

	// Test finding competition by hill id
	@Test
	public void findByHillId() {
		// Creating and saving new hill
		Hill hill = new Hill("Skocznia", "USA", "LA", 123, 120, 1988);
		hillRepository.save(hill);

		// Creating new Date
		Calendar date = Calendar.getInstance();

		// Creating and saving new Competition
		Competition newCompetition = new Competition((long) 11999, hill, date, "test");
		repository.save(newCompetition);

		// Finding competition by hill id
		List<Competition> competitions = repository.findByHillId(hill.getId());
		assertThat(competitions.size()).isEqualTo(1);
	}

	// Test deleting competition
	@Test
	public void deleteCompetition() {
		// Creating and saving new hill
		Hill hill = new Hill("Skocznia", "USA", "LA", 123, 120, 1988);
		hillRepository.save(hill);

		// Creating new Date
		Calendar date = Calendar.getInstance();

		// Creating and saving new Competition
		Competition newCompetition = new Competition((long) 11999, hill, date, "test");
		repository.save(newCompetition);

		Competition competition = repository.findOne((long) 11999);
		repository.delete(competition);

		List<Competition> competitions = (List<Competition>) repository.findAll();
		assertThat(competitions.get(competitions.size() - 1).getType()).isNotEqualTo("test");
	}
}
