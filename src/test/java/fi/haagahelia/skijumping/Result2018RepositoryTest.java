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

import fi.haagahelia.skijumping.domain.Athlete;
import fi.haagahelia.skijumping.domain.AthleteRepository;
import fi.haagahelia.skijumping.domain.Competition;
import fi.haagahelia.skijumping.domain.CompetitionRepository;
import fi.haagahelia.skijumping.domain.Hill;
import fi.haagahelia.skijumping.domain.HillRepository;
import fi.haagahelia.skijumping.domain.Result2018;
import fi.haagahelia.skijumping.domain.Result2018Repository;
import fi.haagahelia.skijumping.domain.WcPoint;
import fi.haagahelia.skijumping.domain.WcPointRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class Result2018RepositoryTest {

	@Autowired
	private Result2018Repository repository;

	@Autowired
	private AthleteRepository athleteRepository;

	@Autowired
	private HillRepository hillRepository;

	@Autowired
	private CompetitionRepository competitionRepository;

	@Autowired
	private WcPointRepository wcPointRepository;

	// Test creating new Result2018
	@Test
	public void createNewResult() {
		// Create new hill
		Hill hill = new Hill("Skocznia", "USA", "LA", 123, 120, 1988);
		hillRepository.save(hill);

		// Creating new Date
		Calendar date = Calendar.getInstance();

		// Create new competition
		Competition competition = new Competition((long) 11999, hill, date, "test");
		competitionRepository.save(competition);

		// Create new athlete
		Athlete athlete = new Athlete("Adam", "Malysz", "Poland", 1977);
		athleteRepository.save(athlete);

		// Create new world cup point
		WcPoint wcPoint = new WcPoint(35, 0);
		wcPointRepository.save(wcPoint);

		Result2018 result = new Result2018(competition, athlete, 123.5, 122.0, 256.9, wcPoint);
		repository.save(result);
		assertThat(result.getId()).isNotNull();
	}

	// Test find by athlete id
	@Test
	public void findByAthleteId() {
		// Create new hill
		Hill hill = new Hill("Skocznia", "USA", "LA", 123, 120, 1988);
		hillRepository.save(hill);

		// Creating new Date
		Calendar date = Calendar.getInstance();

		// Create new competition
		Competition competition = new Competition((long) 11999, hill, date, "test");
		competitionRepository.save(competition);

		// Create new athlete
		Athlete athlete = new Athlete("Adam", "Malysz", "Poland", 1977);
		athleteRepository.save(athlete);

		// Create new world cup point
		WcPoint wcPoint = new WcPoint(35, 0);
		wcPointRepository.save(wcPoint);

		Result2018 newResult = new Result2018(competition, athlete, 123.5, 122.0, 256.9, wcPoint);
		repository.save(newResult);

		List<Result2018> results = repository.findByAthleteId(athlete.getId());
		assertThat(results.size()).isEqualTo(1);
	}

	// Test delete result
	@Test
	public void deleteResult() {
		// Create new hill
		Hill hill = new Hill("Skocznia", "USA", "LA", 123, 120, 1988);
		hillRepository.save(hill);

		// Creating new Date
		Calendar date = Calendar.getInstance();

		// Create new competition
		Competition competition = new Competition((long) 11999, hill, date, "test");
		competitionRepository.save(competition);

		// Create new athlete
		Athlete athlete = new Athlete("Adam", "Malysz", "Poland", 1977);
		athleteRepository.save(athlete);

		// Create new world cup point
		WcPoint wcPoint = new WcPoint(35, 0);
		wcPointRepository.save(wcPoint);

		Result2018 newResult = new Result2018(competition, athlete, 23.5, 122.0, 256.9, wcPoint);
		repository.save(newResult);

		Result2018 result = repository.findOne(newResult.getId());
		repository.delete(result);

		List<Result2018> results = (List<Result2018>) repository.findAll();
		assertThat(results.get(results.size() - 1).getJump1()).isNotEqualTo(23.5);
	}
}
