package fi.haagahelia.skijumping.domain;

import org.springframework.data.repository.CrudRepository;

public interface HillRepository extends CrudRepository<Hill, Long> {

	Hill findByName(String name);
}
