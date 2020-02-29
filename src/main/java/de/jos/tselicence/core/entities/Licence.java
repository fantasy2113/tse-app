package de.jos.tselicence.core.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "licences")
public class Licence implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = IDENTITY)
    private long id;
    @Column(name = "licence_number")
    private String licenceNumber;
    @Column(name = "tse_type")
    private String tseType;
    @Column(name = "number_of_tse")
    private int numberOfTse;
    @Column(name = "branch_number")
    private int branchNumber;
    @Column(name = "till_external_id")
    private int tillExternalId;
    @Column(name = "date_registered")
    private LocalDateTime dateRegistered;
    @Column(name = "is_active")
    private boolean active = true;

    public Licence(long id, String licenceNumber, String tseType, int numberOfTse, int branchNumber, int tillExternalId, LocalDateTime dateRegistered) {
        this.id = id;
        this.licenceNumber = licenceNumber;
        this.tseType = tseType;
        this.numberOfTse = numberOfTse;
        this.branchNumber = branchNumber;
        this.tillExternalId = tillExternalId;
        this.dateRegistered = dateRegistered;
    }

    public Licence(String licenceNumber, String tseType, int numberOfTse, int branchNumber, int tillExternalId, LocalDateTime dateRegistered) {
        this.id = -1;
        this.licenceNumber = licenceNumber;
        this.tseType = tseType;
        this.numberOfTse = numberOfTse;
        this.branchNumber = branchNumber;
        this.tillExternalId = tillExternalId;
        this.dateRegistered = dateRegistered;
    }

    public Licence() {
        this.id = -1;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public int getNumberOfTse() {
        return numberOfTse;
    }

    public void setNumberOfTse(int numberOfTse) {
        this.numberOfTse = numberOfTse;
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
        return "Licence{" +
                "id=" + id +
                ", licenceNumber='" + licenceNumber + '\'' +
                ", tseType='" + tseType + '\'' +
                ", numberOfTse=" + numberOfTse +
                ", branchNumber=" + branchNumber +
                ", tillExternalId=" + tillExternalId +
                ", active=" + active +
                '}';
    }
}
