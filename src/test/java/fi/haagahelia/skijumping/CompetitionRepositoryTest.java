package fi.haagahelia.skijumping;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Date;
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
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
public class CompetitionRepositoryTest {

	@Autowired
	CompetitionRepository repository;
	
	@Autowired
	HillRepository hillRepository;
	
	//Test creating new competition
	@Test
	public void createNewCompetition() {
		//Creating new Hill
		Hill hill = new Hill("Skocznia", "USA", "LA", 123, 120, 1988);
		
		//Creating new Date
		Date date = new Date(19990101);
		
		//Creating and saving new Competition
		Competition competition = new Competition((long) 11999, hill, date, "12:00", "test");
		repository.save(competition);
		assertThat(competition.getId()).isNotNull();
	}
	
	//Test finding competition by hill id
	@Test
	public void findByHillId() {
		//Creating and saving new hill
		Hill hill = new Hill("Skocznia", "USA", "LA", 123, 120, 1988);
		hillRepository.save(hill);
		
		//Creating new Date
		Date date = new Date(19990101);
				
		//Creating and saving new Competition
		Competition newCompetition = new Competition((long) 11999, hill, date, "12:00", "test");
		repository.save(newCompetition);
		
		//Finding competition by hill id
		List<Competition> competitions = repository.findByHillId(hill.getId());
		assertThat(competitions.size()).isEqualTo(1);
	}
	
	//Test deleting competition
	@Test
	public void deleteCompetition() {
		//Creating and saving new hill
		Hill hill = new Hill("Skocznia", "USA", "LA", 123, 120, 1988);
		hillRepository.save(hill);
				
		//Creating new Date
		Date date = new Date(19990101);
						
		//Creating and saving new Competition
		Competition newCompetition = new Competition((long) 11999, hill, date, "12:00", "test");
		repository.save(newCompetition);
		
		Competition competition = repository.findOne((long) 11999);
		repository.delete(competition);
		
		List<Competition> competitions = (List<Competition>) repository.findAll();
		assertThat(competitions.get(0).getType()).isNotEqualTo("test");
	}
}
