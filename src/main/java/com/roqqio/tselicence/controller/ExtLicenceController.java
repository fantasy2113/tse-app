package com.roqqio.tselicence.controller;

import com.roqqio.tselicence.controller.responeses.LicenceResponse;
import com.roqqio.tselicence.core.Comp;
import com.roqqio.tselicence.core.entities.Licence;
import com.roqqio.tselicence.core.entities.LicenceDetail;
import com.roqqio.tselicence.core.entities.RequestLog;
import com.roqqio.tselicence.core.interfaces.repositories.ILicenceDetailRepository;
import com.roqqio.tselicence.core.interfaces.repositories.ILicenceRepository;
import com.roqqio.tselicence.core.interfaces.repositories.IRequestLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
public class ExtLicenceController extends Controller {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExtLicenceController.class.getName());
    private final ILicenceRepository licenceRepository;
    private final ILicenceDetailRepository licenceDetailRepository;
    private final IRequestLogRepository requestLogRepository;

    @Autowired
    public ExtLicenceController(@Qualifier(Comp.LIC_REP) ILicenceRepository licenceRepository,
                                @Qualifier(Comp.REQUEST_LOG_REP) IRequestLogRepository requestLogRepository,
                                @Qualifier(Comp.LIC_DE_REP) ILicenceDetailRepository licenceDetailRepository) {
        this.licenceRepository = licenceRepository;
        this.requestLogRepository = requestLogRepository;
        this.licenceDetailRepository = licenceDetailRepository;
    }

    @GetMapping("/external/licence/free")
    public ResponseEntity<Long> free(@RequestParam String licenceNumber, @RequestParam String tseType) {
        LOGGER.info("get - free");

        Optional<Licence> licence = findLicence(licenceNumber, tseType);

        if (licence.isPresent()) {
            return statusOk(licence.get().getNumberOfTse() - licenceDetailRepository.countByLicenceId(licence.get().getId()));
        }

        return status404NotFound((long) -1);
    }

    @GetMapping("/external/licence/find")
    public ResponseEntity<LicenceResponse> find(@RequestParam String licenceNumber, @RequestParam String tseType,
                                                @RequestParam String branchNumber, @RequestParam String tillExternalId) {
        LOGGER.info("get");

        Optional<Licence> licence = findLicence(licenceNumber, tseType);

        if (licence.isPresent() && licence.get().isActive()) {
            Optional<LicenceDetail> found = licenceDetailRepository.find(licence.get().getId(), branchNumber, tillExternalId);
            if (found.isPresent()) {
                createLog(found.get(), "Angefragt");
                return statusOk(new LicenceResponse(licence.get(), found.get()));
            } else {
                return status204NoContent(new LicenceResponse());
            }
        }

        return status404NotFound(new LicenceResponse());
    }

    @PostMapping("/external/licence/save/licence_detail")
    public ResponseEntity<LicenceResponse> save(@RequestParam String licenceNumber, @RequestParam String tseType,
                                                @RequestParam String branchNumber, @RequestParam String tillExternalId) {
        LOGGER.info("add");

        Optional<Licence> licence = findLicence(licenceNumber, tseType);
        if (!licence.isPresent()) {
            return status404NotFound(new LicenceResponse("unavailable"));
        }
        if (!licence.get().isActive()) {
            return status404NotFound(new LicenceResponse("deactivated"));
        }
        if (licence.get().getNumberOfTse() < licenceDetailRepository.countByLicenceId(licence.get().getId())) {
            return status404NotFound(new LicenceResponse("all already taken"));
        }

        LicenceDetail licenceDetail = new LicenceDetail();
        licenceDetail.setBranchNumber(branchNumber);
        licenceDetail.setTillExternalId(tillExternalId);
        Optional<LicenceDetail> saved = licenceDetailRepository
                .save(getLicenceDetail(licence.get().getId(), branchNumber, tillExternalId));
        if (saved.isPresent()) {
            createLog(saved.get(), "Hinzugefügt");
            return statusOk(new LicenceResponse(licence.get(), saved.get()));
        }
        return status404NotFound(new LicenceResponse("already registered"));
    }

    private LicenceDetail getLicenceDetail(long licenceId, String branchNumber, String tillExternalId) {
        LicenceDetail licenceDetail = new LicenceDetail();
        licenceDetail.setBranchNumber(branchNumber);
        licenceDetail.setTillExternalId(tillExternalId);
        licenceDetail.setLicenceId(licenceId);
        return licenceDetail;
    }

    private Optional<Licence> findLicence(String licenceNumber, String tseType) {
        Licence params = new Licence();
        params.setLicenceNumber(licenceNumber);
        params.setTseType(tseType);
        return licenceRepository.findWithDecryption(params);
    }

    private void createLog(LicenceDetail licenceDetail, String type) {
        run(() -> requestLogRepository.save(new RequestLog(licenceDetail.getId(), type, LocalDateTime.now())));
    }
}
