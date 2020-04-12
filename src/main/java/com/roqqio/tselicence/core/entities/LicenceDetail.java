package com.roqqio.tselicence.core.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roqqio.tselicence.core.interfaces.entities.IModified;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "licences_detail")
public class LicenceDetail implements IModified, Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = IDENTITY)
    private long id;
    @Column(name = "licence_id")
    private long licenceId;
    @Column(name = "branch_number")
    private int branchNumber;
    @Column(name = "till_external_id")
    private int tillExternalId;
    @Column(name = "date_registered")
    private LocalDateTime dateRegistered;
    @Column(name = "is_active")
    private boolean active;
    @Column(name = "modified")
    @JsonIgnore
    private LocalDateTime modified;

    public LicenceDetail() {
        this.id = -1;
        this.active = true;
        this.dateRegistered = LocalDateTime.now();
        this.modified = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getBranchNumber() {
        return branchNumber;
    }

    public void setBranchNumber(int branchNumber) {
        this.branchNumber = branchNumber;
    }

    public int getTillExternalId() {
        return tillExternalId;
    }

    public void setTillExternalId(int tillExternalId) {
        this.tillExternalId = tillExternalId;
    }

    public LocalDateTime getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(LocalDateTime dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getLicenceId() {
        return licenceId;
    }

    public void setLicenceId(long licenceId) {
        this.licenceId = licenceId;
    }

    @Override
    public LocalDateTime getModified() {
        return modified;
    }

    @Override
    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

    @Override
    public String toString() {
        return "LicenceDetail{" +
                "id=" + id +
                ", licenceId=" + licenceId +
                ", branchNumber=" + branchNumber +
                ", tillExternalId=" + tillExternalId +
                ", active=" + active +
                '}';
    }
}
