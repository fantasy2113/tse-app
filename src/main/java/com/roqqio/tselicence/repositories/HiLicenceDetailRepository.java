package com.roqqio.tselicence.repositories;

import com.roqqio.tselicence.core.Comp;
import com.roqqio.tselicence.core.entities.LicenceDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component(Comp.HI_LIC_DE_REP)
interface HiLicenceDetailRepository extends CrudRepository<LicenceDetail, Long> {
    Optional<LicenceDetail> findByLicenceIdAndBranchNumberAndTillExternalId(@Param("licence_id") long licenceId,
                                                                            @Param("branch_number") String branchNumber,
                                                                            @Param("till_external_id") String tillExternalId);

    List<LicenceDetail> findAllByLicenceId(@Param("licence_id") long licenceId);

    void deleteByLicenceId(@Param("licence_id") long licenceId);

    long countByLicenceId(@Param("licence_id") long licenceId);

}
