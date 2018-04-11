package fi.haagahelia.skijumping.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface Result2018Repository extends CrudRepository<Result2018, Long>{
	List<Result2018> findByAthleteId(Long athleteId);
	List<Result2018> findByCompetitionIdOrderByWcPoint(Long competitionId);
}
