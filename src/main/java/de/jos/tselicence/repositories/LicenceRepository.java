package de.jos.tselicence.repositories;

import de.jos.tselicence.core.Comp;
import de.jos.tselicence.core.entities.Licence;
import de.jos.tselicence.core.interfaces.entities.Guards;
import de.jos.tselicence.core.interfaces.repositories.ILicenceRepository;
import de.jos.tselicence.core.interfaces.repositories.Repository;
import de.jos.tselicence.core.interfaces.security.IEncryption;
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

@Component(Comp.LIC_REP)
public class LicenceRepository extends Repository<Licence> implements ILicenceRepository, Guards {
    private static final Logger LOGGER = LoggerFactory.getLogger(LicenceRepository.class.getName());
    private final HiLicenceRepository hiLicenceRepository;
    private final HiLicenceDetailRepository hiLicenceDetailRepository;
    private final IEncryption aes;

    @Autowired
    public LicenceRepository(@Qualifier(Comp.HI_LIC_REP) HiLicenceRepository hiLicenceRepository, EntityManager entityManager,
                             @Qualifier(Comp.URL_SAVE_AES) IEncryption aes, @Qualifier(Comp.HI_LIC_DE_REP) HiLicenceDetailRepository hiLicenceDetailRepository) {
        super(entityManager, Licence.class);
        this.hiLicenceRepository = hiLicenceRepository;
        this.hiLicenceDetailRepository = hiLicenceDetailRepository;
        this.aes = aes;
    }

    @Override
    public Optional<Licence> get(long id) {
        Optional<Licence> licence = hiLicenceRepository.findById(id);
        licence.ifPresent(lic -> lic.setTseAvailable(getAvailableTse(lic)));
        return licence;
    }

    @Override
    public List<Licence> all() {
        List<Licence> licences = IterableUtils.toList(hiLicenceRepository.findAll());
        licences.forEach(lic -> lic.setTseAvailable(getAvailableTse(lic)));
        return licences;
    }

    @Override
    public Optional<Licence> find(Licence params) {
        try {
            Optional<Licence> licence = hiLicenceRepository.findByLicenceNumberAndTseType(params.getLicenceNumber(), params.getTseType());
            licence.ifPresent(lic -> lic.setTseAvailable(getAvailableTse(lic)));
            return licence;
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Licence> update(Licence item) {
        try {
            updateGuard(item);
            item.setModified(LocalDateTime.now());
            return updateOrSave(item);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Licence> save(Licence item) {
        try {
            saveGuard(item);
            LocalDateTime now = LocalDateTime.now();
            item.setModified(now);
            item.setCreated(now);
            return updateOrSave(item);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public void delete(long id) {
        try {
            hiLicenceRepository.deleteById(id);
            hiLicenceDetailRepository.deleteByLicenceId(id);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void delete(Licence item) {
        try {
            hiLicenceRepository.delete(item);
            hiLicenceDetailRepository.deleteByLicenceId(item.getId());
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }

    @Override
    public Optional<Licence> findWithDecryption(Licence params) {
        Optional<String> decrypt = aes.decrypt(params.getLicenceNumber());
        if (decrypt.isPresent()) {
            params.setLicenceNumber(decrypt.get());
            Optional<Licence> licence = find(params);
            if (licence.isPresent()) {
                Optional<String> encrypt = aes.encrypt(licence.get().getLicenceNumber());
                if (encrypt.isPresent()) {
                    licence.get().setLicenceNumber(encrypt.get());
                    return licence;
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Licence> saveWithDecryption(Licence item) {
        Optional<String> decrypt = aes.decrypt(item.getLicenceNumber());
        if (decrypt.isPresent()) {
            item.setLicenceNumber(decrypt.get());
            return save(item);
        }
        return Optional.empty();
    }

    @Override
    public boolean exists(long id) {
        return hiLicenceRepository.existsById(id);
    }

    @Override
    public List<Licence> save(List<Licence> licences) {
        LocalDateTime now = LocalDateTime.now();
        licences.forEach(l -> {
            l.setCreated(now);
            l.setModified(now);
        });
        return IterableUtils.toList(hiLicenceRepository.saveAll(licences));
    }

    @Override
    @Transactional
    public List<Licence> filter(Licence item) {
        if (!isAdd(item.getLicenceNumber()) && !isAdd(item.getTseType())) {
            return new ArrayList<>();
        }
        List<Licence> licences = super.filterItems(item, 250);
        licences.forEach(l -> l.setTseAvailable(getAvailableTse(l)));
        return licences;
    }

    @Override
    protected List<Predicate> getPredicates(Licence item, CriteriaBuilder criteriaBuilder, Root<Licence> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (isAdd(item.getLicenceNumber())) {
            predicates.add(criteriaBuilder.like(root.get("licenceNumber"), contains(item.getLicenceNumber())));
        }
        if (isAdd(item.getTseType())) {
            predicates.add(criteriaBuilder.like(root.get("tseType"), contains(item.getTseType())));
        }
        return predicates;
    }

    private Optional<Licence> updateOrSave(Licence item) throws SQLException {
        crossInjectGuard(item);
        parameterGuard(item);
        Optional<Licence> licence = Optional.of(hiLicenceRepository.save(item));
        licence.ifPresent(l -> l.setTseAvailable(getAvailableTse(l)));
        return licence;
    }

    private void saveGuard(Licence item) throws SQLException {
        if (hiLicenceRepository.existsById(item.getId())) {
            throw new SQLException("Entity: " + item.toString() + " already exists");
        }
    }

    private void parameterGuard(Licence item) throws SQLException {
        if (item.getTseType().trim().isEmpty() || item.getLicenceNumber().trim().isEmpty() || item.getNumberOfTse() < 0) {
            throw new SQLException("Entity: " + item.toString() + " has bad parameters");
        }
    }

    private void updateGuard(Licence item) throws SQLException {
        if (!hiLicenceRepository.existsById(item.getId())) {
            throw new SQLException("Entity: " + item + " not exists!");
        }
    }

    private void alreadyExistGuard(Licence item) throws SQLException {
        if (hiLicenceRepository
                .findByLicenceNumberAndTseType(item.getLicenceNumber(), item.getTseType()).isPresent()) {
            throw new SQLException("Entity: " + item.toString() + " already exists");
        }
    }

    private long getAvailableTse(Licence licence) {
        return licence.getNumberOfTse() - hiLicenceDetailRepository.countByLicenceId(licence.getId());
    }
}
