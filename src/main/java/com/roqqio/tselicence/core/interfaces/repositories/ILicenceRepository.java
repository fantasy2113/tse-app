package com.roqqio.tselicence.core.interfaces.repositories;

import com.roqqio.tselicence.core.entities.Licence;

import java.util.Optional;

public interface ILicenceRepository extends IRepository<Licence>, IFind<Licence>, IFilter<Licence>, IExists {

    Optional<Licence> findWithEncoding(Licence params);

    Optional<Licence> saveWithEncoding(Licence item);


}
