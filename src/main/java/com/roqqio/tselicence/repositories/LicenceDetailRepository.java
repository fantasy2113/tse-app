package com.roqqio.tselicence.repositories;

import com.roqqio.tselicence.core.Comp;
import com.roqqio.tselicence.core.entities.LicenceDetail;
import com.roqqio.tselicence.core.interfaces.entities.Guards;
import com.roqqio.tselicence.core.interfaces.repositories.ILicenceDetailRepository;
import com.roqqio.tselicence.core.interfaces.repositories.Repository;
import org.apache.commons.collections4.IterableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component(Comp.LIC_DE_REP)
public class LicenceDetailRepository extends Repository<LicenceDetail> implements ILicenceDetailRepository, Guards {
    private static final Logger LOGGER = LoggerFactory.getLogger(LicenceDetailRepository.class.getName());
    private final HiLicenceDetailRepository hiLicenceDetailRepository;

    @Autowired
    public LicenceDetailRepository(@Qualifier(Comp.HI_LIC_DE_REP) HiLicenceDetailRepository hiLicenceDetailRepository, EntityManager entityManager) {
        super(entityManager, LicenceDetail.class);
        this.hiLicenceDetailRepository = hiLicenceDetailRepository;
    }

    @Override
    public List<LicenceDetail> allByLicenceId(long licenceId) {
        return hiLicenceDetailRepository.findAllByLicenceId(licenceId);
    }

    @Override
    public Optional<LicenceDetail> get(long id) {
        return hiLicenceDetailRepository.findById(id);
    }

    @Override
    public List<LicenceDetail> all() {
        return IterableUtils.toList(hiLicenceDetailRepository.findAll());
    }

    @Override
    public Optional<LicenceDetail> update(LicenceDetail item) {
        item.setModified(LocalDateTime.now());
        return updateOrSave(item);
    }

    @Override
    public Optional<LicenceDetail> save(LicenceDetail item) {
        LocalDateTime now = LocalDateTime.now();
        item.setDateRegistered(now);
        item.setModified(now);
        return updateOrSave(item);
    }

    @Override
    public List<LicenceDetail> save(List<LicenceDetail> items) {
        LocalDateTime now = LocalDateTime.now();
        items.forEach(l -> {
            l.setDateRegistered(now);
            l.setModified(now);
        });
        return IterableUtils.toList(hiLicenceDetailRepository.saveAll(items));
    }

    @Override
    public void delete(long id) {
        hiLicenceDetailRepository.deleteById(id);
    }

    @Override
    public void delete(LicenceDetail item) {
        hiLicenceDetailRepository.delete(item);
    }

    @Override
    public void deleteByLicenceId(long licenceId) {
        hiLicenceDetailRepository.deleteByLicenceId(licenceId);
    }

    @Override
    public long countByLicenceId(long licenceId) {
        return hiLicenceDetailRepository.countByLicenceId(licenceId);
    }

    @Override
    public Optional<LicenceDetail> find(long licenceId, String branchNumber, String tillExternalId) {
        return hiLicenceDetailRepository.findByLicenceIdAndBranchNumberAndTillExternalId(licenceId, branchNumber, tillExternalId);
    }

    private Optional<LicenceDetail> updateOrSave(LicenceDetail item) {
        try {
            crossInjectGuard(item);
            parameterGuard(item);
            return Optional.of(hiLicenceDetailRepository.save(item));
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    protected List<Predicate> getPredicates(LicenceDetail item, CriteriaBuilder criteriaBuilder, Root<LicenceDetail> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (isAdd(item.getLicenceId())) {
            predicates.add(criteriaBuilder.equal(root.get("licenceId"), item.getLicenceId()));
        }
        if (isAdd(item.getBranchNumber())) {
            predicates.add(criteriaBuilder.like(root.get("branchNumber"), contains(item.getBranchNumber())));
        }
        if (isAdd(item.getTillExternalId())) {
            predicates.add(criteriaBuilder.like(root.get("tillExternalId"), contains(item.getTillExternalId())));
        }
        return predicates;
    }

    private void parameterGuard(LicenceDetail item) throws SQLException {
        if (item.getBranchNumber() == null || item.getBranchNumber().trim().isEmpty()
                || item.getTillExternalId() == null || item.getTillExternalId().trim().isEmpty()
                || item.getLicenceId() < 1) {
            throw new SQLException("Entity: " + item.toString() + " has bad parameters");
        }
    }

    private void alreadyExistGuard(LicenceDetail item) throws SQLException {
        if (hiLicenceDetailRepository
                .findByLicenceIdAndBranchNumberAndTillExternalId(item.getLicenceId(), item.getBranchNumber(), item.getTillExternalId()).isPresent()) {
            throw new SQLException("Entity: " + item.toString() + " already exists");
        }
    }

    @Override
    public boolean exists(long id) {
        return hiLicenceDetailRepository.existsById(id);
    }

    @Override
    @Transactional
    public List<LicenceDetail> filter(LicenceDetail filter) {
        return super.filterItems(filter, 250);
    }
}
