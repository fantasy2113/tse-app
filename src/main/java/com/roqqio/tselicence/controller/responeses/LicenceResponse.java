package com.roqqio.tselicence.controller.responeses;

import com.roqqio.tselicence.core.entities.Licence;
import com.roqqio.tselicence.core.entities.LicenceDetail;

import java.time.LocalDateTime;

public class LicenceResponse {
    private String licenceNumber;
    private String tseType;
    private int branchNumber;
    private int tillExternalId;
    private LocalDateTime dateRegistered;
    private boolean active;

    public LicenceResponse(Licence licence, LicenceDetail licenceDetail) {
        this.licenceNumber = licence.getLicenceNumber();
        this.tseType = licence.getTseType();
        this.branchNumber = licenceDetail.getBranchNumber();
        this.tillExternalId = licenceDetail.getTillExternalId();
        this.dateRegistered = licenceDetail.getDateRegistered();
        this.active = licenceDetail.isActive();
    }

    public LicenceResponse() {
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public String getTseType() {
        return tseType;
    }

    public void setTseType(String tseType) {
        this.tseType = tseType;
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

    @Override
    public String toString() {
        return "LicenceResponse{" +
                "licenceNumber='" + licenceNumber + '\'' +
                ", tseType='" + tseType + '\'' +
                ", branchNumber=" + branchNumber +
                ", tillExternalId=" + tillExternalId +
                ", active=" + active +
                '}';
    }
}
