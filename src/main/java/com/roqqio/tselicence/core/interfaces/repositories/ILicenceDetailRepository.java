package com.roqqio.tselicence.core.interfaces.repositories;

import com.roqqio.tselicence.core.entities.LicenceDetail;

import java.util.Optional;

public interface ILicenceDetailRepository extends IRepository<LicenceDetail>, IAllByLicenceId<LicenceDetail>, IFilter<LicenceDetail>, IDeleteByLicenceId, IExists {

    long countByLicenceId(long licenceId);

    Optional<LicenceDetail> find(long licenceId, int branchNumber, int tillExternalId);
}
