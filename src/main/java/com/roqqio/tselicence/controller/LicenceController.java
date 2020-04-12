package com.roqqio.tselicence.controller;

import com.roqqio.tselicence.core.Comp;
import com.roqqio.tselicence.core.entities.Licence;
import com.roqqio.tselicence.core.interfaces.repositories.ILicenceRepository;
import com.roqqio.tselicence.security.WithAuthentication;
import com.roqqio.tselicence.security.WithIpRestriction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public final class LicenceController extends Controller {
    private final ILicenceRepository licenceRepository;

    @Autowired
    public LicenceController(@Qualifier(Comp.LIC_REP) ILicenceRepository licenceRepository) {
        this.licenceRepository = licenceRepository;
    }

    @WithIpRestriction
    @WithAuthentication
    @GetMapping("internal/licence/{id}")
    public ResponseEntity<Licence> get(@PathVariable("id") long id) {
        Optional<Licence> optLic = licenceRepository.get(id);
        return optLic.isPresent() ? statusOk(optLic.get()) : status404NoEntityFound(new Licence());
    }

    @WithIpRestriction
    @WithAuthentication
    @GetMapping("internal/licences")
    public ResponseEntity<List<Licence>> licences() {
        return statusOk(licenceRepository.all());
    }

    @WithIpRestriction
    @WithAuthentication
    @PutMapping("internal/licence/filter")
    public ResponseEntity<List<Licence>> filter(@RequestBody Licence licence) {
        List<Licence> filter = licenceRepository.filter(licence);
        return statusOk(licenceRepository.filter(licence));
    }

    @WithIpRestriction
    @WithAuthentication
    @DeleteMapping("internal/licence/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        licenceRepository.delete(id);
        return statusDeleteOk();
    }

    @WithIpRestriction
    @WithAuthentication
    @DeleteMapping("internal/licence/delete")
    public ResponseEntity<Void> delete(@RequestBody Licence licence) {
        licenceRepository.delete(licence);
        return statusDeleteOk();
    }

    @WithIpRestriction
    @WithAuthentication
    @PutMapping("internal/licence/update")
    public ResponseEntity<Licence> update(@RequestBody Licence licence) {
        Optional<Licence> updated = licenceRepository.update(licence);
        return updated.isPresent() ? statusOk(updated.get()) : status404NoEntityFound(new Licence());
    }

    @WithIpRestriction
    @WithAuthentication
    @PostMapping("internal/licence/save")
    public ResponseEntity<Licence> save(@RequestBody Licence licence) {
        Optional<Licence> saved = licenceRepository.save(licence);
        return saved.isPresent() ? statusOk(saved.get()) : status404NoEntityFound(new Licence());
    }
}
