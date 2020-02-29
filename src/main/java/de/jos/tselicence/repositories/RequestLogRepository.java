package de.jos.tselicence.repositories;

import de.jos.tselicence.core.Comp;
import de.jos.tselicence.core.entities.RequestLog;
import de.jos.tselicence.core.interfaces.repositories.IRequestLogRepository;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component(Comp.Request_Log_REP)
public class RequestLogRepository implements IRequestLogRepository {
    private final RequestLogHiRepository requestLogHiRepository;

    @Autowired
    public RequestLogRepository(RequestLogHiRepository requestLogHiRepository) {
        this.requestLogHiRepository = requestLogHiRepository;
    }

    @Override
    public Optional<RequestLog> get(long id) {
        return requestLogHiRepository.findById(id);
    }

    @Override
    public List<RequestLog> all() {
        return IterableUtils.toList(requestLogHiRepository.findAll());
    }

    @Override
    public Optional<RequestLog> find(RequestLog params) {
        return Optional.empty();
    }

    @Override
    public Optional<RequestLog> update(RequestLog item) {
        // not impl
        return Optional.empty();
    }

    @Override
    public Optional<RequestLog> save(RequestLog item) {
        return Optional.of(requestLogHiRepository.save(item));
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
    public boolean existsById(long id) {
        return false;
    }

    @Override
    public List<RequestLog> getAll(long licenceId) {
        return IterableUtils.toList(requestLogHiRepository.findAllByLicenceId(licenceId));
    }
}
