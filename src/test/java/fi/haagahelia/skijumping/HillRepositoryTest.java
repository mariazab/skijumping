package fi.haagahelia.skijumping;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import fi.haagahelia.skijumping.domain.Hill;
import fi.haagahelia.skijumping.domain.HillRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
public class HillRepositoryTest {

	@Autowired
	HillRepository repository;
	
	//Test creating new hill
	@Test
	public void createNewHill() {
		Hill hill = new Hill("Skocznia", "USA", "LA", 123, 120, 1988);
		repository.save(hill);
		assertThat(hill.getId()).isNotNull();
	}
	
	//Test finding hill by name
	@Test
	public void findByName() {
		Hill newHill = new Hill("Skocznia", "USA", "LA", 123, 120, 1988);
		repository.save(newHill);
		Hill hill = repository.findByName("Skocznia");
		assertThat(hill.getCountry()).isEqualTo("USA");
	}
	
	//Test deleting hill
	@Test
	public void deleteHill() {
		Hill newHill = new Hill("Skocznia", "USA", "LA", 123, 120, 1988);
		repository.save(newHill);
		Hill hill = repository.findByName("Skocznia");
		repository.delete(hill);
		List<Hill> hills = (List<Hill>) repository.findAll();
		assertThat(hills.get(0).getName()).isNotEqualTo("Skocznia");
	}
}
