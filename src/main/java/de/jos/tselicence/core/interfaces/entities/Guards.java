package de.jos.tselicence.core.interfaces.entities;

import java.sql.SQLException;

public interface Guards {
    default void crossInjectGuard(IEntityData item) throws SQLException {
        final String data = item.getDataAsStr();
        if (data.contains("{") || data.contains("}") || data.contains("(") || data.contains(")")
                || data.contains("[") || data.contains("]") || data.contains("<") || data.contains(">") || data.contains(";")) {
            throw new SQLException("Cross-Site-Scripting");
        }
    }
}
