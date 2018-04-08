package fi.haagahelia.skijumping;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

import fi.haagahelia.skijumping.web.AthleteController;
import fi.haagahelia.skijumping.web.CompetitionController;
import fi.haagahelia.skijumping.web.HillController;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SkijumpingApplicationTests {

	@Autowired
	AthleteController athleteController;
	
	@Autowired 
	HillController hillController;
	
	@Autowired
	CompetitionController competitionController;
	
	@Test
	public void contextLoads() {
		assertThat(athleteController).isNotNull();
		assertThat(hillController).isNotNull();
		assertThat(competitionController).isNotNull();
	}

}
