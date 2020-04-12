package com.roqqio.tselicence.core.interfaces.repositories;

import java.util.Optional;

public interface IFind<T> {
    Optional<T> find(T params);
}
