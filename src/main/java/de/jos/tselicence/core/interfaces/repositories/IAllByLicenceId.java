package de.jos.tselicence.core.interfaces.repositories;

import java.util.List;

public interface IAllByLicenceId<T> {
    List<T> allByLicenceId(long licenceId);
}
