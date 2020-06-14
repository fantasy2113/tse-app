package de.jos.tselicence.core.interfaces.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface IEntityData {
    @JsonIgnore
    default String getDataAsStr() {
        return "";
    }
}
