package fi.haagahelia.skijumping;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import fi.haagahelia.skijumping.domain.WcPoint;
import fi.haagahelia.skijumping.domain.WcPointRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class WcPointRepositoryTest {

	@Autowired
	private WcPointRepository repository;

	// Test creating new World Cup Points
	@Test
	public void createNewWcPoint() {
		WcPoint wcPoint = new WcPoint(35, 0);
		repository.save(wcPoint);
		assertThat(wcPoint.getPosition()).isNotNull();
	}

	// Test finding wcPoint by points
	@Test
	public void findByPoints() {

		// Create new wc point
		WcPoint newWcPoint = new WcPoint(35, 500);
		repository.save(newWcPoint);

		WcPoint wcPoint = repository.findByPoints(500);
		assertThat(wcPoint.getPosition()).isEqualTo(35);
	}

	// Test deleting wcPoint
	@Test
	public void deleteWcPoint() {

		// Create new wc point
		WcPoint newWcPoint = new WcPoint(135, 0);
		repository.save(newWcPoint);
		WcPoint wcPoint = repository.findOne(135);
		repository.delete(wcPoint);
		List<WcPoint> points = (List<WcPoint>) repository.findAll();
		assertThat(points.get(points.size() - 1).getPosition()).isNotEqualTo(135);
	}
}
