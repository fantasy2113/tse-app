package de.jos.tselicence.core.interfaces.repositories;

import java.util.List;
import java.util.Optional;

public interface IRepository<T> {
    Optional<T> get(long id);

    List<T> all();

    Optional<T> find(T params);

    Optional<T> update(T item);

    Optional<T> save(T item);

    void delete(long id);

    void delete(T item);

    boolean existsById(long id);
}
