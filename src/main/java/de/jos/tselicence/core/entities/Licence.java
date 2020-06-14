package de.jos.tselicence.core.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.jos.tselicence.core.interfaces.entities.IEntityData;
import de.jos.tselicence.core.interfaces.entities.IModified;
import de.jos.tselicence.core.util.Toolbox;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "licences")
public class Licence implements IEntityData, IModified, Serializable {
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
    @Column(name = "is_active")
    private boolean active;
    @Column(name = "created")
    @JsonIgnore
    private LocalDateTime created;
    @Column(name = "modified")
    @JsonIgnore
    private LocalDateTime modified;
    @Transient
    private long tseAvailable;
    @Transient
    private String info;

    public Licence() {
        this.id = -1;
        this.created = LocalDateTime.now();
        this.modified = LocalDateTime.now();
        this.active = true;
    }

    public Licence(String info) {
        this.id = -1;
        this.created = LocalDateTime.now();
        this.modified = LocalDateTime.now();
        this.active = true;
        this.info = info;
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
        this.licenceNumber = Toolbox.trim(licenceNumber);
    }

    public String getTseType() {
        return tseType;
    }

    public void setTseType(String tseType) {
        this.tseType = Toolbox.trim(tseType);
    }

    public int getNumberOfTse() {
        return numberOfTse;
    }

    public void setNumberOfTse(int numberOfTse) {
        this.numberOfTse = numberOfTse;
    }

    public long getTseAvailable() {
        return tseAvailable;
    }

    public void setTseAvailable(long tseAvailable) {
        this.tseAvailable = tseAvailable;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public LocalDateTime getModified() {
        return modified;
    }

    @Override
    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
    public String toString() {
        final StringBuffer sb = new StringBuffer("Licence{");
        sb.append("id=").append(id);
        sb.append(", licenceNumber='").append(licenceNumber).append('\'');
        sb.append(", tseType='").append(tseType).append('\'');
        sb.append(", numberOfTse=").append(numberOfTse);
        sb.append(", active=").append(active);
        sb.append(", tseAvailable=").append(tseAvailable);
        sb.append('}');
        return sb.toString();
    }

    @Override
    @JsonIgnore
    public String getDataAsStr() {
        return licenceNumber + tseType;
    }
}
