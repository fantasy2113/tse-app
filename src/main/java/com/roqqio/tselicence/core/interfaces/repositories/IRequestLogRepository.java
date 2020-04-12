package com.roqqio.tselicence.core.interfaces.repositories;

import com.roqqio.tselicence.core.entities.RequestLog;

import java.util.List;

public interface IRequestLogRepository extends IRepository<RequestLog> {
    List<RequestLog> allByLicenceDetailId(long licenceDetailId);

    void deleteByLicenceDetailId(long licenceDetailId);
}
