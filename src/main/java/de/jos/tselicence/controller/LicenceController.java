package de.jos.tselicence.controller;

import de.jos.tselicence.core.Comp;
import de.jos.tselicence.core.entities.Licence;
import de.jos.tselicence.core.interfaces.repositories.ILicenceRepository;
import de.jos.tselicence.security.WithAuthentication;
import de.jos.tselicence.security.WithIpRestriction;
import io.swagger.annotations.ApiImplicitParam;
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
    @ApiImplicitParam(paramType = "header", name = "htoken")
    public ResponseEntity<Licence> get(@PathVariable("id") long id) {
        Optional<Licence> optLic = licenceRepository.get(id);
        return optLic.isPresent() ? statusOk(optLic.get()) : status404NotFound(new Licence());
    }

    @WithIpRestriction
    @WithAuthentication
    @GetMapping("internal/licences")
    @ApiImplicitParam(paramType = "header", name = "htoken")
    public ResponseEntity<List<Licence>> licences() {
        return statusOk(licenceRepository.all());
    }

    @WithIpRestriction
    @WithAuthentication
    @PutMapping("internal/licence/filter")
    @ApiImplicitParam(paramType = "header", name = "htoken")
    public ResponseEntity<List<Licence>> filter(@RequestBody Licence licence) {
        return statusOk(licenceRepository.filter(licence));
    }

    @WithIpRestriction
    @WithAuthentication
    @DeleteMapping("internal/licence/delete/{id}")
    @ApiImplicitParam(paramType = "header", name = "htoken")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        licenceRepository.delete(id);
        return statusDeleteOk();
    }

    @WithIpRestriction
    @WithAuthentication
    @DeleteMapping("internal/licence/delete")
    @ApiImplicitParam(paramType = "header", name = "htoken")
    public ResponseEntity<Void> delete(@RequestBody Licence licence) {
        licenceRepository.delete(licence);
        return statusDeleteOk();
    }

    @WithIpRestriction
    @WithAuthentication
    @PutMapping("internal/licence/update")
    @ApiImplicitParam(paramType = "header", name = "htoken")
    public ResponseEntity<Licence> update(@RequestBody Licence licence) {
        Optional<Licence> updated = licenceRepository.update(licence);
        return updated.isPresent() ? statusOk(updated.get()) : status404NotFound(new Licence("Lizenz ist schon vorhanden oder ungültige Eingaben!"));
    }

    @WithIpRestriction
    @WithAuthentication
    @PostMapping("internal/licence/save")
    @ApiImplicitParam(paramType = "header", name = "htoken")
    public ResponseEntity<Licence> save(@RequestBody Licence licence) {
        Optional<Licence> saved = licenceRepository.save(licence);
        return saved.isPresent() ? statusOk(saved.get()) : status404NotFound(new Licence("Lizenz ist schon vorhanden oder ungültige Eingaben!"));
    }
}
