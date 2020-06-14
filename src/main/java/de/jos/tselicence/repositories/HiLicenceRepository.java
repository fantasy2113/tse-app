package de.jos.tselicence.repositories;

import de.jos.tselicence.core.Comp;
import de.jos.tselicence.core.entities.Licence;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component(Comp.HI_LIC_REP)
interface HiLicenceRepository extends CrudRepository<Licence, Long> {
    Optional<Licence> findByLicenceNumberAndTseType(@Param("licence_number") String licenceNumber, @Param("tse_type") String tseType);
}
