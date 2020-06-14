package de.jos.tselicence.core.interfaces.repositories;

import java.util.List;

public interface IFilter<T> {
    List<T> filter(T filter);
}
