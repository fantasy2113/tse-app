package com.roqqio.tselicence.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
public class InfoController {
    @GetMapping(value = "/client", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> client(HttpServletRequest req) throws Exception {
        String xForwardedFor = req.getHeader("X-Forwarded-For");
        xForwardedFor = xForwardedFor != null && xForwardedFor.contains(",") ? xForwardedFor.split(",")[0] : xForwardedFor;
        String remoteHost = req.getRemoteHost();
        String remoteAddr = req.getRemoteAddr();
        int remotePort = req.getRemotePort();
        String msg = remoteHost + " (" + remoteAddr + ":" + remotePort + ") X-Forwarded-For=" + xForwardedFor;
        return ResponseEntity.status(200).body(msg);
    }

    @GetMapping(value = "/header", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> header(HttpServletRequest req) throws Exception {
        Map<String, String> result = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        Enumeration headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = req.getHeader(key);
            result.put(key, value);
            sb.append(key + ":" + value + "\n");
        }

        return ResponseEntity.status(200).body(sb.toString());
    }

    @GetMapping(value = "/time", produces = MediaType.TEXT_PLAIN_VALUE)
    public String time() {
        return LocalDateTime.now().toString();
    }
}
