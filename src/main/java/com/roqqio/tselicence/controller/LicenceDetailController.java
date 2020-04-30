package com.roqqio.tselicence.controller;

import com.roqqio.tselicence.core.Comp;
import com.roqqio.tselicence.core.entities.Licence;
import com.roqqio.tselicence.core.entities.LicenceDetail;
import com.roqqio.tselicence.core.interfaces.repositories.ILicenceDetailRepository;
import com.roqqio.tselicence.core.interfaces.repositories.ILicenceRepository;
import com.roqqio.tselicence.security.WithAuthentication;
import com.roqqio.tselicence.security.WithIpRestriction;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class LicenceDetailController extends Controller {
    private final ILicenceDetailRepository licenceDetailRepository;
    private final ILicenceRepository licenceRepository;

    @Autowired
    public LicenceDetailController(@Qualifier(Comp.LIC_DE_REP) ILicenceDetailRepository licenceDetailRepository,
                                   @Qualifier(Comp.LIC_REP) ILicenceRepository licenceRepository) {
        this.licenceDetailRepository = licenceDetailRepository;
        this.licenceRepository = licenceRepository;
    }

    @WithIpRestriction
    @WithAuthentication
    @GetMapping("internal/licences/detail/{licence_id}")
    @ApiImplicitParam(paramType = "header", name = "htoken")
    public ResponseEntity<List<LicenceDetail>> licences(@PathVariable("licence_id") long licenceId) {
        return statusOk(licenceDetailRepository.allByLicenceId(licenceId));
    }

    @WithIpRestriction
    @WithAuthentication
    @PutMapping("internal/licence/detail/filter")
    @ApiImplicitParam(paramType = "header", name = "htoken")
    public ResponseEntity<List<LicenceDetail>> filter(@RequestBody LicenceDetail licenceDetail) {
        return statusOk(licenceDetailRepository.filter(licenceDetail));
    }

    @WithIpRestriction
    @WithAuthentication
    @DeleteMapping("internal/licence/detail/delete/{id}")
    @ApiImplicitParam(paramType = "header", name = "htoken")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        licenceDetailRepository.delete(id);
        return statusDeleteOk();
    }

    @WithIpRestriction
    @WithAuthentication
    @DeleteMapping("internal/licence/detail/delete")
    @ApiImplicitParam(paramType = "header", name = "htoken")
    public ResponseEntity<Void> delete(@RequestBody LicenceDetail licenceDetail) {
        licenceDetailRepository.delete(licenceDetail);
        return statusDeleteOk();
    }

    @WithIpRestriction
    @WithAuthentication
    @PutMapping("internal/licence/detail/update")
    @ApiImplicitParam(paramType = "header", name = "htoken")
    public ResponseEntity<LicenceDetail> update(@RequestBody LicenceDetail licenceDetail) {
        Optional<LicenceDetail> updated = licenceDetailRepository.update(licenceDetail);
        return updated.isPresent() ? statusOk(updated.get())
                : status404NotFound(new LicenceDetail("Kasse schon registriert!"));
    }

    @WithIpRestriction
    @WithAuthentication
    @PostMapping("internal/licence/detail/save")
    @ApiImplicitParam(paramType = "header", name = "htoken")
    public ResponseEntity<LicenceDetail> save(@RequestBody LicenceDetail licenceDetail) {
        Optional<Licence> licence = licenceRepository.get(licenceDetail.getLicenceId());

        if (!licence.isPresent()) {
            return status404NotFound(new LicenceDetail("Lizenz ist nicht vorhanden!"));
        }

        if (!licence.get().isActive()) {
            return status404NotFound(new LicenceDetail("Lizenz ist deaktiviert!"));
        }

        if (licence.get().getNumberOfTse() < licenceDetailRepository.countByLicenceId(licence.get().getId())) {
            return status404NotFound(new LicenceDetail("Alle Lizenzen sind bereits vergeben!"));
        }

        Optional<LicenceDetail> saved = licenceDetailRepository.save(licenceDetail);
        return saved.isPresent() ? statusOk(saved.get())
                : status404NotFound(new LicenceDetail("LizenzDetail ist schon vorhanden oder ungültige Eingaben!"));
    }
}
