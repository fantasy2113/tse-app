package com.roqqio.tselicence.repositories;

import com.roqqio.tselicence.core.Comp;
import com.roqqio.tselicence.core.entities.RequestLog;
import com.roqqio.tselicence.core.interfaces.repositories.IRequestLogRepository;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component(Comp.REQUEST_LOG_REP)
public class RequestLogRepository implements IRequestLogRepository {
    private final HiRequestLogRepository hiRequestLogRepository;

    @Autowired
    public RequestLogRepository(@Qualifier(Comp.HI_REQUEST_LOG_REP) HiRequestLogRepository hiRequestLogRepository) {
        this.hiRequestLogRepository = hiRequestLogRepository;
    }

    @Override
    public Optional<RequestLog> get(long id) {
        return hiRequestLogRepository.findById(id);
    }

    @Override
    public List<RequestLog> all() {
        return IterableUtils.toList(hiRequestLogRepository.findAll());
    }

    @Override
    public Optional<RequestLog> update(RequestLog item) {
        // not impl
        return Optional.empty();
    }

    @Override
    public Optional<RequestLog> save(RequestLog item) {
        return Optional.of(hiRequestLogRepository.save(item));
    }

    @Override
    public void delete(long id) {
        // not impl
    }

    @Override
    public void delete(RequestLog item) {
        // not impl
    }

    @Override
    public List<RequestLog> allByLicenceDetailId(long licenceDetailId) {
        return IterableUtils.toList(hiRequestLogRepository.findAllByLicenceDetailId(licenceDetailId));
    }

    @Override
    public List<RequestLog> save(List<RequestLog> requestLogs) {
        return IterableUtils.toList(hiRequestLogRepository.saveAll(requestLogs));
    }

    @Override
    public void deleteByLicenceDetailId(long licenceId) {
        hiRequestLogRepository.deleteByLicenceDetailId(licenceId);
    }
}
