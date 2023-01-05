package me.nerminsehic.groupevent.service;

import java.util.List;
import java.util.Optional;

public interface Service<T, ID> {
    Optional<T> findById(ID id);

    List<T> findAll();

    T create(T entity);

    void deleteById(ID id);

    void delete(T entity);

    T updateById(ID id, T entity);

}
