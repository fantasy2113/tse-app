package de.jos.tselicence.controller;

import de.jos.tselicence.core.Comp;
import de.jos.tselicence.core.interfaces.repositories.ILogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class InfoController {
    private final ILogRepository logRepository;

    @Autowired
    public InfoController(@Qualifier(Comp.LOG_REP) ILogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @GetMapping(value = "/time", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> time() {
        return ResponseEntity.ok().body(LocalDateTime.now().toString());
    }

    @GetMapping(value = "/logs", produces = MediaType.TEXT_HTML_VALUE)
    public final ResponseEntity<String> logs() {
        return ResponseEntity.ok().body(logs(logRepository.getLogs()));
    }

    @GetMapping(value = "/ip", produces = MediaType.TEXT_HTML_VALUE)
    public final ResponseEntity<String> ip(HttpServletRequest request) {
        String clientInfo = request.getRemoteHost() + " Host (" + request.getRemoteAddr() + " Addr : " + request.getRemotePort() + " Port)";
        return ResponseEntity.ok().body(clientInfo);
    }

    private String logs(final List<String> logs) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><title>Logs</title><body>");
        html.append("<table style=\"text-align:left;\">");
        html.append("<tr>");
        html.append("<th style=\"border:1px solid black\">Logs:</th>");
        html.append("</tr>");
        logs.stream().map(log -> "<tr><td style=\"border:1px solid black;text-align:left;\">" + log + "</td></tr>")
                .forEach(html::append);
        html.append("</table></body></html>");
        return html.toString();
    }
}
