package de.jos.tselicence.core.interfaces.repositories;

import de.jos.tselicence.core.entities.RequestLog;

import java.util.List;

public interface IRequestLogRepository extends IRepository<RequestLog> {

    List<RequestLog> getAll(long licenceId);
}
