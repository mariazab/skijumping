package fi.haagahelia.skijumping.domain;

import org.springframework.data.repository.CrudRepository;

public interface WcStanding2018Repository extends CrudRepository<WcStanding2018, Long>{

	WcStanding2018 findByAthleteId(Long athleteId);
	
}
