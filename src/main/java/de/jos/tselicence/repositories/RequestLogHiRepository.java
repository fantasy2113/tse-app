package de.jos.tselicence.repositories;

import de.jos.tselicence.core.Comp;
import de.jos.tselicence.core.entities.RequestLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(Comp.ACCESS_HI_REP)
interface RequestLogHiRepository extends CrudRepository<RequestLog, Long> {
    List<RequestLog> findAllByLicenceId(@Param("licence_id") long licenceId);
}
