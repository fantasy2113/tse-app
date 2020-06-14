package de.jos.tselicence.core.interfaces.entities;

import java.time.LocalDateTime;

public interface IModified {
    LocalDateTime getModified();

    void setModified(LocalDateTime modified);
}
