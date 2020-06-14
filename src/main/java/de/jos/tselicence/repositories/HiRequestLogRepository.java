package de.jos.tselicence.repositories;

import de.jos.tselicence.core.Comp;
import de.jos.tselicence.core.entities.RequestLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(Comp.HI_REQUEST_LOG_REP)
interface HiRequestLogRepository extends CrudRepository<RequestLog, Long> {
    List<RequestLog> findAllByLicenceDetailId(@Param("licence_detail_id") long licenceDetailId);

    void deleteByLicenceDetailId(@Param("licence_detail_id") long licenceDetailId);
}
