package com.internetbanking.service;

public interface CrudOperation <T> {
    T save(T t);

    void delete(T t);

    void delete(Long id);

    T update(T t);

    T findById(Long id);
}
