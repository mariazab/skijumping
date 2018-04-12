package fi.haagahelia.skijumping.domain;



import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface WcStanding2018Repository extends CrudRepository<WcStanding2018, Long>{

	WcStanding2018 findByAthleteId(Long athleteId);
	List<WcStanding2018> findAllByOrderByPointsDesc();
	
}
