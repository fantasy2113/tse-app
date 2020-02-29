package de.jos.tselicence.repositories;

import de.jos.tselicence.core.Comp;
import de.jos.tselicence.core.entities.Licence;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component(Comp.LIC_HI_REP)
interface LicenceHiRepository extends CrudRepository<Licence, Long> {
    Optional<Licence> findByLicenceNumberAndTseTypeAndBranchNumberAndTillExternalId(@Param("licence_number") String licenceNumber,
                                                                                    @Param("tse_type") String tseType,
                                                                                    @Param("branch_number") int branchNumber,
                                                                                    @Param("till_external_id") int tillExternalId);
}
