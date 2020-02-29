package de.jos.tselicence.core.entities;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "request_logs")
public class RequestLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = IDENTITY)
    private long id;
    @Column(name = "licence_id")
    private long licenceId;
    @Column(name = "request_type")
    private String requestType;
    @Column(name = "request_date")
    private LocalDateTime requestDate;

    public RequestLog(long id, long licenceId, String requestType, LocalDateTime requestDate) {
        this.id = id;
        this.licenceId = licenceId;
        this.requestType = requestType;
        this.requestDate = requestDate;
    }

    public RequestLog(long licenceId, String requestType, LocalDateTime requestDate) {
        this.licenceId = licenceId;
        this.requestType = requestType;
        this.requestDate = requestDate;
        this.id = -1;
    }

    public RequestLog() {
        this.id = -1;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLicenceId() {
        return licenceId;
    }

    public void setLicenceId(long licenceId) {
        this.licenceId = licenceId;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime accessDate) {
        this.requestDate = accessDate;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
}
