package com.roqqio.tselicence.controller;

import com.roqqio.tselicence.core.Comp;
import com.roqqio.tselicence.core.entities.RequestLog;
import com.roqqio.tselicence.core.interfaces.repositories.IRequestLogRepository;
import com.roqqio.tselicence.security.WithAuthentication;
import com.roqqio.tselicence.security.WithIpRestriction;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RequestLogController extends Controller {

    private final IRequestLogRepository requestLogRepository;

    @Autowired
    public RequestLogController(@Qualifier(Comp.REQUEST_LOG_REP) IRequestLogRepository requestLogRepository) {
        this.requestLogRepository = requestLogRepository;
    }

    @WithIpRestriction
    @WithAuthentication
    @GetMapping("internal/licence/detail/requests/{licence_detail_id}")
    @ApiImplicitParam(paramType = "header", name = "htoken")
    public ResponseEntity<List<RequestLog>> requests(@PathVariable("licence_detail_id") long licenceDetailId) {
        return statusOk(requestLogRepository.allByLicenceDetailId(licenceDetailId));
    }
}
