package de.jos.tselicence.core.interfaces.repositories;

import de.jos.tselicence.core.entities.Licence;

import java.util.List;

public interface ILicenceRepository extends IRepository<Licence> {
    List<Licence> filter(Licence licence);
}
