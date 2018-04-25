package fi.haagahelia.skijumping.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface FavAthleteRepository extends CrudRepository<FavAthlete, Long>{
	List<FavAthlete> findByUserId(Long userId);
}
