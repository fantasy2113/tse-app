package com.roqqio.tselicence.core.interfaces.repositories;

import java.util.List;
import java.util.Optional;

public interface IRepository<T> {
    Optional<T> get(long id);

    List<T> all();

    Optional<T> update(T item);

    Optional<T> save(T item);

    List<T> save(List<T> items);

    void delete(long id);

    void delete(T item);
}
