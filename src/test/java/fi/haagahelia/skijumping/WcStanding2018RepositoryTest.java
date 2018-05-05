package fi.haagahelia.skijumping;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import fi.haagahelia.skijumping.domain.Athlete;
import fi.haagahelia.skijumping.domain.AthleteRepository;
import fi.haagahelia.skijumping.domain.WcStanding2018;
import fi.haagahelia.skijumping.domain.WcStanding2018Repository;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
public class WcStanding2018RepositoryTest {

	@Autowired
	private WcStanding2018Repository repository;
	
	@Autowired
	private AthleteRepository athleteRepository;
	
	//Testing creating new world cup standing
	@Test
	public void createNewStanding() {
		//Create new athlete
		Athlete athlete = new Athlete("Adam", "Malysz", "Poland", 1977);
		athleteRepository.save(athlete);
		
		WcStanding2018 standing = new WcStanding2018(athlete, 100);
		repository.save(standing);
		assertThat(standing.getId()).isNotNull();
	}
	
	//Testing finding standing by athlete id
	@Test
	public void findByAthleteId() {
		//Create new athlete
		Athlete athlete = new Athlete("Adam", "Malysz", "Poland", 1977);
		athleteRepository.save(athlete);
				
		WcStanding2018 newStanding = new WcStanding2018(athlete, 100);
		repository.save(newStanding);
		
		WcStanding2018 standing = repository.findByAthleteId(athlete.getId());
		assertThat(standing.getPoints()).isEqualTo(100);
	}
	
	//Testing deleting standing
	@Test
	public void deleteStanding() {
		//Create new athlete
		Athlete athlete = new Athlete("Adam", "Malysz", "Poland", 1977);
		athleteRepository.save(athlete);
				
		WcStanding2018 newStanding = new WcStanding2018(athlete, 100);
		repository.save(newStanding);
		
		WcStanding2018 standing = repository.findByAthleteId(athlete.getId());
		repository.delete(standing);
		
		List<WcStanding2018> standings = (List<WcStanding2018>) repository.findAll();
		assertThat(standings.get(standings.size() - 1).getPoints()).isNotEqualTo(100);
	}
}
