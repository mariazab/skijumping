package fi.haagahelia.skijumping.domain;

import org.springframework.data.repository.CrudRepository;

public interface WcPointRepository extends CrudRepository<WcPoint, Integer> {
	WcPoint findByPoints(Integer points);
	WcPoint findByPosition(Integer position);
}
