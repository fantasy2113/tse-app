package com.roqqio.tselicence.core.interfaces.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface IEntityData {
    @JsonIgnore
    default String getDataAsStr() {
        return "";
    }
}
