package fi.haagahelia.skijumping.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface CompetitionRepository extends CrudRepository<Competition, Long> {

	List<Competition> findByHillId(long hillId);
	List<Competition> findAllByOrderById();
}
