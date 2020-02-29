package de.jos.tselicence.controller;

import de.jos.tselicence.core.Comp;
import de.jos.tselicence.core.entities.Licence;
import de.jos.tselicence.core.entities.RequestLog;
import de.jos.tselicence.core.interfaces.repositories.ILicenceRepository;
import de.jos.tselicence.core.interfaces.repositories.IRequestLogRepository;
import de.jos.tselicence.core.interfaces.security.IAuthenticator;
import de.jos.tselicence.core.interfaces.security.IWhitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public final class LicenceController extends Controller {
    private final ILicenceRepository licenceRepository;
    private final IRequestLogRepository requestLogRepository;

    @Autowired
    public LicenceController(@Qualifier(Comp.AUTH) IAuthenticator authenticator,
                             @Qualifier(Comp.LIC_REP) ILicenceRepository licenceRepository,
                             @Qualifier(Comp.Request_Log_REP) IRequestLogRepository requestLogRepository,
                             @Qualifier(Comp.WHITELIST) IWhitelist whitelist) {
        super(authenticator, whitelist);
        this.licenceRepository = licenceRepository;
        this.requestLogRepository = requestLogRepository;
    }

    @GetMapping("/licence/{id}")
    public ResponseEntity<Licence> get(@PathVariable("id") long id,
                                       @RequestHeader(value = "external", defaultValue = "true") boolean external,
                                       @RequestHeader(value = "htoken", required = false) String htoken,
                                       @CookieValue(value = "ctoken", required = false) String ctoken,
                                       @RequestParam(value = "ptoken", required = false) String ptoken) {

        if (getAuthenticator().isFail(htoken, ctoken, ptoken)) {
            return ResponseEntity.status(403).body(new Licence());
        }

        logExternalAccess(id, external);
        Optional<Licence> optLic = licenceRepository.get(id);

        return optLic.isPresent() ? statusOk(optLic.get()) : status404NoEntityFound(new Licence());
    }

    @GetMapping("/licence/requests/{licence_id}")
    public ResponseEntity<List<RequestLog>> requests(@PathVariable("licence_id") long licenceId,
                                                     @RequestHeader(value = "htoken", required = false) String htoken,
                                                     @CookieValue(value = "ctoken", required = false) String ctoken,
                                                     @RequestParam(value = "ptoken", required = false) String ptoken) {

        if (getAuthenticator().isFail(htoken, ctoken, ptoken)) {
            return statusUnauthorized(new ArrayList<>());
        }

        return statusOk(requestLogRepository.getAll(licenceId));
    }

    @GetMapping("/licences")
    public ResponseEntity<List<Licence>> licences(@RequestHeader(value = "htoken", required = false) String htoken,
                                                  @CookieValue(value = "ctoken", required = false) String ctoken,
                                                  @RequestParam(value = "ptoken", required = false) String ptoken) {

        if (getAuthenticator().isFail(htoken, ctoken, ptoken)) {
            return statusUnauthorized(new ArrayList<>());
        }

        return statusOk(licenceRepository.all());
    }

    @GetMapping("/licence/find")
    public ResponseEntity<Licence> find(@RequestParam String licenceNumber, @RequestParam String tseType, @RequestParam int branchNumber, @RequestParam int tillExternalId,
                                        @RequestHeader(value = "external", defaultValue = "true") boolean external,
                                        @RequestHeader(value = "htoken", required = false) String htoken,
                                        @CookieValue(value = "ctoken", required = false) String ctoken,
                                        @RequestParam(value = "ptoken", required = false) String ptoken) {

        if (getAuthenticator().isFail(htoken, ctoken, ptoken)) {
            return statusUnauthorized(new Licence());
        }

        Licence params = new Licence();
        params.setLicenceNumber(licenceNumber);
        params.setTseType(tseType);
        params.setBranchNumber(branchNumber);
        params.setTillExternalId(tillExternalId);

        Optional<Licence> optLic = licenceRepository.find(params);

        if (optLic.isPresent()) {
            logExternalAccess(optLic.get().getId(), external);
            return statusOk(optLic.get());
        }

        return status404NoEntityFound(new Licence());
    }

    @PutMapping("/licence/filter")
    public ResponseEntity<List<Licence>> filter(@RequestBody Licence licence,
                                                @RequestHeader(value = "htoken", required = false) String htoken,
                                                @CookieValue(value = "ctoken", required = false) String ctoken,
                                                @RequestParam(value = "ptoken", required = false) String ptoken) {

        if (getAuthenticator().isFail(htoken, ctoken, ptoken)) {
            statusUnauthorized(new ArrayList<>());
        }

        return statusOk(licenceRepository.filter(licence));
    }

    @DeleteMapping("/licence/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id,
                                       @RequestHeader(value = "htoken", required = false) String htoken,
                                       @CookieValue(value = "ctoken", required = false) String ctoken,
                                       @RequestParam(value = "ptoken", required = false) String ptoken) {

        if (getAuthenticator().isFail(htoken, ctoken, ptoken)) {
            return statusUnauthorized();
        }

        licenceRepository.delete(id);

        return statusDeleteOk();
    }

    @DeleteMapping("/licence/delete")
    public ResponseEntity<Void> delete(@RequestBody Licence licence,
                                       @RequestHeader(value = "htoken", required = false) String htoken,
                                       @CookieValue(value = "ctoken", required = false) String ctoken,
                                       @RequestParam(value = "ptoken", required = false) String ptoken) {

        if (getAuthenticator().isFail(htoken, ctoken, ptoken)) {
            return statusUnauthorized();
        }

        licenceRepository.delete(licence);

        return statusDeleteOk();
    }

    @PutMapping("/licence/update")
    public ResponseEntity<Licence> update(@RequestBody Licence licence,
                                          @RequestHeader(value = "htoken", required = false) String htoken,
                                          @CookieValue(value = "ctoken", required = false) String ctoken,
                                          @RequestParam(value = "ptoken", required = false) String ptoken) {

        if (getAuthenticator().isFail(htoken, ctoken, ptoken)) {
            return statusUnauthorized(new Licence());
        }

        Optional<Licence> optLic = licenceRepository.update(licence);

        return optLic.isPresent() ? statusOk(optLic.get()) : status404NoEntityFound(new Licence());
    }

    @PostMapping("/licence/save")
    public ResponseEntity<Licence> save(@RequestBody Licence licence,
                                        @RequestHeader(value = "htoken", required = false) String htoken,
                                        @CookieValue(value = "ctoken", required = false) String ctoken,
                                        @RequestParam(value = "ptoken", required = false) String ptoken) {

        if (getAuthenticator().isFail(htoken, ctoken, ptoken)) {
            return statusUnauthorized(new Licence());
        }

        Optional<Licence> optLic = licenceRepository.save(licence);

        return optLic.isPresent() ? statusOk(optLic.get()) : status404NoEntityFound(new Licence());
    }

    private void logExternalAccess(long id, boolean external) {
        if (external) {
            run(() -> requestLogRepository.save(new RequestLog(id, "external", LocalDateTime.now())));
        }
    }
}
