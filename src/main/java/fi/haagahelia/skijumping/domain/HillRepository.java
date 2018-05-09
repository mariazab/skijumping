package fi.haagahelia.skijumping.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface HillRepository extends CrudRepository<Hill, Long> {

	Hill findByName(String name);
	Hill findByHillRecord(HillRecord hillRecord);
	List<Hill> findAllByOrderByName();
}
