package com.roqqio.tselicence.core.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roqqio.tselicence.core.interfaces.entities.IEntityData;
import com.roqqio.tselicence.core.interfaces.entities.IModified;
import com.roqqio.tselicence.core.util.Toolbox;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "licences_detail")
public class LicenceDetail implements IModified, Serializable, IEntityData {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = IDENTITY)
    private long id;
    @Column(name = "licence_id")
    private long licenceId;
    @Column(name = "branch_number")
    private String branchNumber;
    @Column(name = "till_external_id")
    private String tillExternalId;
    @Column(name = "date_registered")
    private LocalDateTime dateRegistered;
    @Column(name = "is_active")
    private boolean active;
    @Column(name = "modified")
    @JsonIgnore
    private LocalDateTime modified;
    @Transient
    private String info;

    public LicenceDetail() {
        this.id = -1;
        this.active = true;
        this.dateRegistered = LocalDateTime.now();
        this.modified = LocalDateTime.now();
        this.info = "";
    }

    /**
     * Constructor.
     *
     * @param info
     */
    public LicenceDetail(String info) {
        this.id = -1;
        this.active = true;
        this.dateRegistered = LocalDateTime.now();
        this.modified = LocalDateTime.now();
        this.info = info;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBranchNumber() {
        return branchNumber;
    }

    public void setBranchNumber(String branchNumber) {
        this.branchNumber = Toolbox.trim(branchNumber);
    }

    public String getTillExternalId() {
        return tillExternalId;
    }

    public void setTillExternalId(String tillExternalId) {
        this.tillExternalId = Toolbox.trim(tillExternalId);
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

    /**
     * Returns the info.
     *
     * @return the info
     */
    public String getInfo() {
        return info;
    }

    /**
     * Sets the info field with given info.
     *
     * @param info the info to set
     */
    public void setInfo(String info) {
        this.info = info;
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
        return "LicenceDetail{" + "id=" + id + ", licenceId=" + licenceId + ", branchNumber=" + branchNumber + ", tillExternalId="
                + tillExternalId + ", active=" + active + '}';
    }

    @Override
    @JsonIgnore
    public String getDataAsStr() {
        return tillExternalId + branchNumber;
    }
}
