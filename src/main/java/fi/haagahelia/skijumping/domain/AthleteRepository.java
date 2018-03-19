package fi.haagahelia.skijumping.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface AthleteRepository extends CrudRepository<Athlete, Long> {
	List<Athlete> findByLastName(String lastName);
}
