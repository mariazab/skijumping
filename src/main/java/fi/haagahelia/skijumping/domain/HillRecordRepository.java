package fi.haagahelia.skijumping.domain;

import org.springframework.data.repository.CrudRepository;

public interface HillRecordRepository extends CrudRepository<HillRecord, Long> {

	HillRecord findByHillId(Long hillId);
}
