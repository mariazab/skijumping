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
import fi.haagahelia.skijumping.domain.Hill;
import fi.haagahelia.skijumping.domain.HillRecord;
import fi.haagahelia.skijumping.domain.HillRecordRepository;
import fi.haagahelia.skijumping.domain.HillRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
public class HillRepositoryTest {

	@Autowired
	private HillRepository repository;
	
	@Autowired
	private HillRecordRepository recordRepository;
	
	@Autowired
	private AthleteRepository athleteRepository;
	
	//Test creating new hill
	@Test
	public void createNewHill() {
		Hill hill = new Hill("Skocznia", "USA", "LA", 123, 120, 1988);
		repository.save(hill);
		assertThat(hill.getId()).isNotNull();
	}
	
	//Test creating new hill record
	@Test
	public void createNewHillRecord() {
		Hill hill = new Hill("Skocznia", "USA", "LA", 123, 120, 1988);
		repository.save(hill);
		
		Athlete athlete = new Athlete("Adam", "Malysz", "Poland", 1977);
		athleteRepository.save(athlete);
		
		HillRecord record = new HillRecord(hill, athlete, 120, 2000);
		recordRepository.save(record);
		assertThat(record.getId()).isNotNull();
	}
	
	//Test finding hill by name
	@Test
	public void findByName() {
		Hill newHill = new Hill("Skocznia", "USA", "LA", 123, 120, 1988);
		repository.save(newHill);
		
		Hill hill = repository.findByName("Skocznia");
		assertThat(hill.getCountry()).isEqualTo("USA");
	}
	
	//Test finding hill record by hill id
	@Test
	public void findByHillId() {
		//Creating and saving new hill
		Hill hill = new Hill("Skocznia", "USA", "LA", 123, 120, 1988);
		repository.save(hill);
		
		//Creating and saving new athlete
		Athlete athlete = new Athlete("Adam", "Malysz", "Poland", 1977);
		athleteRepository.save(athlete);
		
		//Creating and saving new record
		HillRecord newRecord = new HillRecord(hill, athlete, 120, 2000);
		recordRepository.save(newRecord);
		
		//Finding record by hill id
		HillRecord record = recordRepository.findByHillId(hill.getId());
		assertThat(record.getLength()).isEqualTo(120);
	}
	
	//Test deleting hill
	@Test
	public void deleteHill() {
		Hill newHill = new Hill("Skocznia", "USA", "LA", 123, 120, 1988);
		repository.save(newHill);
		
		Hill hill = repository.findByName("Skocznia");
		repository.delete(hill);
		
		List<Hill> hills = (List<Hill>) repository.findAll();
		assertThat(hills.get(hills.size() - 1).getName()).isNotEqualTo("Skocznia");
	}
	
	//Test deleting hill record
	@Test
	public void deleteHillRecord() {
		Hill hill = new Hill("Skocznia", "USA", "LA", 123, 120, 1988);
		repository.save(hill);
		
		Athlete athlete = new Athlete("Adam", "Malysz", "Poland", 1977);
		athleteRepository.save(athlete);
		
		HillRecord newRecord = new HillRecord(hill, athlete, 120, 2000);
		recordRepository.save(newRecord);
		
		HillRecord record = recordRepository.findByHillId(hill.getId());
		recordRepository.delete(record);
		
		List<HillRecord> records = (List<HillRecord>) recordRepository.findAll();
		assertThat(records.get(0).getLength()).isNotEqualTo(120);
	}
}
