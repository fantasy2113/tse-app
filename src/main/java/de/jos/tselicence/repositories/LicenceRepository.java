package de.jos.tselicence.repositories;

import de.jos.tselicence.core.Comp;
import de.jos.tselicence.core.entities.Licence;
import de.jos.tselicence.core.interfaces.repositories.ILicenceRepository;
import org.apache.commons.collections4.IterableUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component(Comp.LIC_REP)
public class LicenceRepository implements ILicenceRepository {

    private final LicenceHiRepository licenceHiRepository;
    private final EntityManager entityManager;

    @Autowired
    public LicenceRepository(@Qualifier(Comp.LIC_HI_REP) LicenceHiRepository licenceHiRepository, EntityManager entityManager) {
        this.licenceHiRepository = licenceHiRepository;
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Licence> get(long id) {
        return licenceHiRepository.findById(id);
    }

    @Override
    public List<Licence> all() {
        return IterableUtils.toList(licenceHiRepository.findAll());
    }

    @Override
    public Optional<Licence> find(Licence params) {
        try {
            return licenceHiRepository.findByLicenceNumberAndTseTypeAndBranchNumberAndTillExternalId(
                    params.getLicenceNumber(),
                    params.getTseType(),
                    params.getBranchNumber(),
                    params.getTillExternalId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Licence> update(Licence item) {
        try {
            updateGuard(item);
            return Optional.of(licenceHiRepository.save(item));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Licence> save(Licence item) {
        try {
            saveGuard(item);
            return Optional.of(licenceHiRepository.save(item));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void delete(long id) {
        licenceHiRepository.deleteById(id);
    }

    @Override
    public void delete(Licence item) {
        licenceHiRepository.delete(item);
    }

    @Override
    public boolean existsById(long id) {
        return licenceHiRepository.existsById(id);
    }

    private void saveGuard(Licence item) throws SQLException {
        if (licenceHiRepository.existsById(item.getId())) {
            throw new SQLException("Entity: " + item + " already exists!");
        }
    }

    private void updateGuard(Licence item) throws SQLException {
        if (!licenceHiRepository.existsById(item.getId())) {
            throw new SQLException("Entity: " + item + " not exists!");
        }
    }

    @Override
    @Transactional
    public List<Licence> filter(Licence licence) {
        Session session = entityManager.unwrap(Session.class);
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Licence> criteriaQuery = criteriaBuilder.createQuery(Licence.class);
        Root<Licence> root = criteriaQuery.from(Licence.class);
        criteriaQuery.select(root).where(getPredicates(licence, criteriaBuilder, root).toArray(new Predicate[]{}));
        Query<Licence> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    private List<Predicate> getPredicates(Licence licence, CriteriaBuilder criteriaBuilder, Root<Licence> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (isAdd(licence.getLicenceNumber())) {
            predicates.add(criteriaBuilder.like(root.get("licenceNumber"), contains(licence.getLicenceNumber())));
        }
        if (isAdd(licence.getTseType())) {
            predicates.add(criteriaBuilder.like(root.get("tseType"), contains(licence.getTseType())));
        }
        if (isAdd(licence.getId())) {
            predicates.add(criteriaBuilder.equal(root.get("id"), licence.getId()));
        }
        if (isAdd(licence.getNumberOfTse())) {
            predicates.add(criteriaBuilder.equal(root.get("numberOfTse"), licence.getNumberOfTse()));
        }
        if (isAdd(licence.getBranchNumber())) {
            predicates.add(criteriaBuilder.equal(root.get("branchNumber"), licence.getBranchNumber()));
        }
        if (isAdd(licence.getTillExternalId())) {
            predicates.add(criteriaBuilder.equal(root.get("tillExternalId"), licence.getTillExternalId()));
        }
        return predicates;
    }

    private String contains(String value) {
        return "%" + value + "%";
    }

    private boolean isAdd(int value) {
        return value > 0;
    }

    private boolean isAdd(long value) {
        return value > 0;
    }

    private boolean isAdd(String value) {
        return value != null && !value.isEmpty();
    }
}
