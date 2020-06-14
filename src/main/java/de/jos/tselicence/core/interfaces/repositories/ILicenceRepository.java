package de.jos.tselicence.core.interfaces.repositories;

import de.jos.tselicence.core.entities.Licence;

import java.util.Optional;

public interface ILicenceRepository extends IRepository<Licence>, IFind<Licence>, IFilter<Licence>, IExists {

    Optional<Licence> findWithDecryption(Licence params);

    Optional<Licence> saveWithDecryption(Licence item);
}
