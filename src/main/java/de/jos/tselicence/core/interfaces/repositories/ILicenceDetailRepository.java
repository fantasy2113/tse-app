package de.jos.tselicence.core.interfaces.repositories;

import de.jos.tselicence.core.entities.LicenceDetail;

import java.util.Optional;

public interface ILicenceDetailRepository extends IRepository<LicenceDetail>, IAllByLicenceId<LicenceDetail>, IFilter<LicenceDetail>, IDeleteByLicenceId, IExists {

    long countByLicenceId(long licenceId);

    Optional<LicenceDetail> find(long licenceId, String branchNumber, String tillExternalId);
}
