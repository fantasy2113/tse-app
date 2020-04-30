package com.roqqio.tselicence.controller.responeses;

import com.roqqio.tselicence.core.entities.Licence;
import com.roqqio.tselicence.core.entities.LicenceDetail;

import java.time.LocalDateTime;

public class LicenceResponse {
    private String info;
    private String licenceNumber;
    private String tseType;
    private String branchNumber;
    private String tillExternalId;
    private LocalDateTime dateRegistered;

    public LicenceResponse(Licence licence, LicenceDetail licenceDetail) {
        this.licenceNumber = licence.getLicenceNumber();
        this.tseType = licence.getTseType();
        this.branchNumber = licenceDetail.getBranchNumber();
        this.tillExternalId = licenceDetail.getTillExternalId();
        this.dateRegistered = licenceDetail.getDateRegistered();
    }

    public LicenceResponse(String info) {
        this.info = info;
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

    public String getBranchNumber() {
        return branchNumber;
    }

    public void setBranchNumber(String branchNumber) {
        this.branchNumber = branchNumber;
    }

    public String getTillExternalId() {
        return tillExternalId;
    }

    public void setTillExternalId(String tillExternalId) {
        this.tillExternalId = tillExternalId;
    }

    public LocalDateTime getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(LocalDateTime dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "LicenceResponse{" +
                "licenceNumber='" + licenceNumber + '\'' +
                ", tseType='" + tseType + '\'' +
                ", branchNumber=" + branchNumber +
                ", tillExternalId=" + tillExternalId +
                '}';
    }
}
