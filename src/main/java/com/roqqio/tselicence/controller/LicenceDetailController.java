package com.roqqio.tselicence.controller;

import com.roqqio.tselicence.core.Comp;
import com.roqqio.tselicence.core.entities.LicenceDetail;
import com.roqqio.tselicence.core.interfaces.repositories.ILicenceDetailRepository;
import com.roqqio.tselicence.security.WithAuthentication;
import com.roqqio.tselicence.security.WithIpRestriction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class LicenceDetailController extends Controller {
    private final ILicenceDetailRepository licenceDetailRepository;

    @Autowired
    public LicenceDetailController(@Qualifier(Comp.LIC_DE_REP) ILicenceDetailRepository licenceDetailRepository) {
        this.licenceDetailRepository = licenceDetailRepository;
    }

    @WithIpRestriction
    @WithAuthentication
    @GetMapping("internal/licences/detail/{licence_id}")
    public ResponseEntity<List<LicenceDetail>> licences(@PathVariable("licence_id") long licenceId) {
        return statusOk(licenceDetailRepository.allByLicenceId(licenceId));
    }

    @WithIpRestriction
    @WithAuthentication
    @PutMapping("internal/licence/detail/filter")
    public ResponseEntity<List<LicenceDetail>> filter(@RequestBody LicenceDetail licenceDetail) {
        return statusOk(licenceDetailRepository.filter(licenceDetail));
    }

    @WithIpRestriction
    @WithAuthentication
    @DeleteMapping("internal/licence/detail/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        licenceDetailRepository.delete(id);
        return statusDeleteOk();
    }

    @WithIpRestriction
    @WithAuthentication
    @DeleteMapping("internal/licence/detail/delete")
    public ResponseEntity<Void> delete(@RequestBody LicenceDetail licenceDetail) {
        licenceDetailRepository.delete(licenceDetail);
        return statusDeleteOk();
    }

    @WithIpRestriction
    @WithAuthentication
    @PutMapping("internal/licence/detail/update")
    public ResponseEntity<LicenceDetail> update(@RequestBody LicenceDetail licenceDetail) {
        Optional<LicenceDetail> updated = licenceDetailRepository.update(licenceDetail);
        return updated.isPresent() ? statusOk(updated.get()) : status404NoEntityFound(new LicenceDetail());
    }

    @WithIpRestriction
    @WithAuthentication
    @PostMapping("internal/licence/detail/save")
    public ResponseEntity<LicenceDetail> save(@RequestBody LicenceDetail licenceDetail) {
        Optional<LicenceDetail> saved = licenceDetailRepository.save(licenceDetail);
        return saved.isPresent() ? statusOk(saved.get()) : status404NoEntityFound(new LicenceDetail());
    }
}
