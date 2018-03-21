package fi.haagahelia.skijumping;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

import fi.haagahelia.skijumping.web.AthleteController;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SkijumpingApplicationTests {

	@Autowired
	AthleteController athleteController;
	
	@Test
	public void contextLoads() {
		assertThat(athleteController).isNotNull();
	}

}
