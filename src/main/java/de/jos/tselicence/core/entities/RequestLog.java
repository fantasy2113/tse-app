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
    @Column(name = "licence_detail_id")
    private long licenceDetailId;
    @Column(name = "request_type")
    private String requestType;
    @Column(name = "request_date")
    private LocalDateTime requestDate;

    public RequestLog(long id, long licenceDetailId, String requestType, LocalDateTime requestDate) {
        this.id = id;
        this.licenceDetailId = licenceDetailId;
        this.requestType = requestType;
        this.requestDate = requestDate;
    }

    public RequestLog(long licenceDetailId, String requestType, LocalDateTime requestDate) {
        this.id = -1;
        this.licenceDetailId = licenceDetailId;
        this.requestType = requestType;
        this.requestDate = requestDate;
    }

    public RequestLog() {
        this.id = -1;
        this.requestDate = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLicenceDetailId() {
        return licenceDetailId;
    }

    public void setLicenceDetailId(long licenceId) {
        this.licenceDetailId = licenceId;
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
